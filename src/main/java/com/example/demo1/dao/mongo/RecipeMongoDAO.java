package com.example.demo1.dao.mongo;

import com.example.demo1.Configuration;
import com.example.demo1.model.Author;
import com.example.demo1.model.Recipe;
import com.example.demo1.model.RecipeReducted;
import com.example.demo1.model.Review;
import com.example.demo1.persistence.MongoDBDriver;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Indexes.descending;
import static com.mongodb.client.model.Projections.include;

public class RecipeMongoDAO {

    public static void deleteRecipe(Recipe recipe) throws MongoException{
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE).deleteOne(new Document("Name", recipe.getName()));
    }

    public static Recipe getRecipeByName(String name) throws MongoException{
        return transformDocToRec(Objects.requireNonNull(MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_RECIPE).find(new Document("Name", name)).first()));
    }

    public static Double calculateAggregatedRating(String recipeName) throws MongoException{
        Bson match = match(new Document("Name",recipeName));
        Bson unwind = new Document("$unwind","$Reviews");
        Bson project = project(new Document("Reviews",1));
        Bson group = new Document("$group", new Document("_id", null).append("average", new Document("$avg","$Reviews.Rating")));
        return safeExecutionDouble(Objects.requireNonNull(MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE)
                .aggregate(Arrays.asList(match, unwind, project, group)).first()),"average");

    }

    public static void setAggregatedRating(String recipeName, Double newAggregatedRating) throws MongoException {
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE)
                .updateOne(new Document("Name", recipeName), new Document("$set", new Document("AggregatedRating", newAggregatedRating)));
    }
    public static boolean checkIfReviewSpaceIsFull(String recipeName, Integer maxSizeOfReviews) throws MongoException{
        Bson match = match(new Document("Name",recipeName));
        Bson project = new Document("$project",new Document("numberOfReviews",new Document("$size", "$Reviews" )));
        return !(MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE)
                .aggregate(Arrays.asList(match, project)).first().getInteger("numberOfReviews") < maxSizeOfReviews);
    }

    public static void addReview(String recipeName, String reviewer, Integer rating, String review) throws MongoException {
        MongoCollection<Document> collection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
        //entra solo quando reviews è piano e toglie la prima
        if(checkIfReviewSpaceIsFull(recipeName,500)){
            collection.updateOne(new Document("Name", recipeName), new Document("$pop", new Document("Reviews", -1)));
        }

        collection.updateOne(new Document("Name", recipeName), new Document("$push", new Document("Reviews",
                new Document("AuthorName", reviewer).append("Rating", rating).append("Review", review))));

    }

    public static void addRecipe(Recipe recipe) throws MongoException {
        Document recipeToAdd = new Document().append("Name", recipe.getName()).append("AuthorName",recipe.getAuthorName())
                .append("TotalTime",recipe.getTotalTime()).append("DatePublished", recipe.getDatePublished())
                .append("Description",recipe.getDescription()).append("Images",recipe.getImages())
                .append("RecipeCategory",recipe.getRecipeCategory()).append("Keywords",recipe.getKeywords())
                .append("RecipeIngredientParts",recipe.getRecipeIngredientParts()).append("AggregatedRating",recipe.getAggregatedRating())
                .append("Calories",recipe.getCalories()).append("RecipeServings",recipe.getRecipeServings())
                .append("RecipeInstructions",recipe.getReviews()).append("Reviews",new ArrayList<Document>());
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE).insertOne(recipeToAdd);
    }

    public static boolean checkIfNameIsAvailable(String name) throws MongoException{
        /*return !MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE).
                find(new Document("Name",name)).cursor().hasNext();*/
        return !MongoDBDriver.getDriver().getCollectionCP(Configuration.MONGODB_RECIPE).
                find(new Document("Name",name)).cursor().hasNext();
    }

    public static List<RecipeReducted> getRecipeFromAuthor(String authorName, Integer elementToSkip, Integer elementsToLimit) throws MongoException{
        List<RecipeReducted> recipeReducted = new ArrayList<>();
        MongoCollection<Document> collection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
        Bson match = match(new Document("AuthorName", authorName));
        Bson project = project(new Document("Name", 1).append("AuthorName", 1)
                .append("Images", new Document("$first", "$Images")));
        MongoCursor<Document> cursor = (authorName == null) ?
                collection.aggregate(Arrays.asList(skip(elementToSkip),limit(elementsToLimit),project)).iterator() :
                collection.aggregate(Arrays.asList(match,skip(elementToSkip),limit(elementsToLimit),project)).iterator();
        cursor.forEachRemaining(recipeDoc -> recipeReducted.add(new RecipeReducted(recipeDoc.getString("Name"),
                recipeDoc.getString("AuthorName"), recipeDoc.getString("Images"))));
        return recipeReducted;
    }

    public static List<RecipeReducted> getRecipeFromName(String name, Integer elementToSkip, Integer elementsToLimit) throws MongoException{
        List<RecipeReducted> recipesList = new ArrayList<>();
        MongoCollection<Document> collection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
        Bson match = match(new Document("Name",new Document("$regex",name).append("$options","i")));
        Bson project = project(new Document("Name",1).append("AuthorName",1).append("Images", new Document("$first","$Images")));
        MongoCursor<Document> cursor = (name == null) ?
                collection.aggregate(Arrays.asList(skip(elementToSkip),limit(elementsToLimit),project)).iterator() :
                collection.aggregate(Arrays.asList(match,skip(elementToSkip),limit(elementsToLimit),project)).iterator();
        cursor.forEachRemaining(recipeDoc -> recipesList.add(new RecipeReducted(recipeDoc.getString("Name"),
                recipeDoc.getString("AuthorName"), recipeDoc.getString("Images"))));
        return recipesList;
    }

    private static Review transformDocToRev(Document doc) throws MongoException{
        return new Review(doc.getString("AuthorName"), doc.getInteger("Rating"), doc.getString("Review"));
    }

    private static Recipe transformDocToRec(Document doc) throws MongoException{
        return new Recipe(doc.getString("Name"), doc.getString("AuthorName"), safeExecutionInteger(doc, "TotalTime"),
                safeExecutionString(doc, "DatePublished"), safeExecutionString(doc, "Description"), safeExecutionList(doc, "Images"),
                safeExecutionString(doc, "RecipeCategory"), safeExecutionList(doc,"Keywords"), safeExecutionList(doc, "RecipeIngredientParts"),
                safeExecutionDouble(doc, "AggregatedRating"), safeExecutionDouble(doc, "Calories"),
                safeExecutionDouble(doc, "RecipeServings"), safeExecutionList(doc, "RecipeInstructions"),
                fromDocListToRevList(safeExecutionListDocument(doc, "Reviews")));
    }

    private static String safeExecutionString(Document doc, String string){
        try{return doc.getString(string);}catch(NumberFormatException e){return null;}
    }

    private static Double safeExecutionDouble(Document doc, String string){
        try{return Double.valueOf(String.valueOf(doc.get(string)));}catch(NumberFormatException e){return null;}
    }

    private static Integer safeExecutionInteger(Document doc, String string){
        try{return doc.getInteger(string);}catch(NumberFormatException e){return null;}
    }

    private static List<String> safeExecutionList(Document doc, String string){
        try{return doc.getList(string, String.class);}catch(NullPointerException e){return null;}
    }

    private static List<Document> safeExecutionListDocument(Document doc, String string){
        try{return doc.getList(string, Document.class);}catch(NullPointerException e){return null;}
    }

    private static List<Review> fromDocListToRevList(List<Document> listDoc) throws MongoException{
        if(listDoc == null)
            return null;
        else{
            List<Review> listReview = new ArrayList<>();
            listDoc.forEach(document -> listReview.add(transformDocToRev(document)));
            return listReview;
        }
    }

    /// methods for analytics ///
    public static List<Recipe> findTopRecipesForRangesOfPreparationTime(Integer lowerLimit, Integer upperLimit, Integer minNumberReviews,
                                                                 Integer limitRecipes) throws MongoException{
        List<Recipe> listRecipe = new ArrayList<>();
        Bson match = (upperLimit != -1) ? match(new Document("TotalTime", new Document("$gt",lowerLimit).append("$lte", upperLimit))) :
                match(new Document("TotalTime", new Document("$gt",lowerLimit))); //terzo range
        Bson matchR = match(new Document("Reviews." + minNumberReviews, new Document("$exists",true)));
        Bson sort = sort(descending("AggregatedRating"));
        Bson limit = limit(limitRecipes);
        Bson project = project(new Document("Name", 1).append("TotalTime", 1).append("AggregatedRating",1).append("Images", new Document("$first", "$Images")));
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE)
                .aggregate(Arrays.asList(matchR,match,sort,limit,project)).forEach(doc ->
                    listRecipe.add(new Recipe(doc.getString("Name"), null, doc.getInteger("TotalTime"),
                            null, null, new ArrayList<>(Collections.singleton(doc.getString("Images"))), null, null, null,
                            Double.valueOf(String.valueOf(doc.get("AggregatedRating"))), null, null, null, null)));
        return listRecipe;
    }
    public static Map<String,Integer> findMostUsedIngredients(Integer limitIngredients, Integer minNumberReviews) throws MongoException{
        Bson matchR = match(new Document("Reviews." + minNumberReviews, new Document("$exists",true)));
        Bson unwind = new Document("$unwind",new Document("path","$RecipeIngredientParts"));
        Bson group = new Document("$group", new Document("_id", "$RecipeIngredientParts").
                append("count",new Document("$count",new Document())));
        Bson project = project(include("RecipeIngredientParts"));
        Bson limit = limit(limitIngredients);
        Bson sort = sort(descending("count"));
        HashMap<String,Integer> mapIngredient = new HashMap<>();
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE)
                .aggregate(Arrays.asList(matchR,project,unwind,group,sort,limit))
                .forEach(doc -> mapIngredient.put(doc.getString("_id"),doc.getInteger("count")));
        List<Map.Entry<String, Integer>> list = new LinkedList<>(mapIngredient.entrySet()).stream().sorted((o1, o2) -> (o2.getValue()).compareTo(o1.getValue())).toList();
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        list.forEach(entry -> sortedMap.put(entry.getKey(), entry.getValue()));
        return sortedMap;
    }
    public static List<Recipe> findRecipesWithHighestRating(Integer limitRecipes, Integer minNumberReviews) throws MongoException{
        Bson matchR = match(new Document("Reviews." + minNumberReviews, new Document("$exists", true)));
        Bson limit = limit(limitRecipes);
        Bson sort = sort(descending("AggregatedRating"));
        Bson project = project(new Document("Name", 1).append("AggregatedRating",1).append("Images", new Document("$first", "$Images")));
        List<Recipe> listRecipe = new ArrayList<>();
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE)
                .aggregate(Arrays.asList(matchR,sort,limit,project)).forEach( doc ->
                    listRecipe.add(new Recipe(doc.getString("Name"), null, null, null,
                            null, new ArrayList<>(Collections.singleton(doc.getString("Images"))),
                            null, null, null, Double.valueOf(String.valueOf(doc.get("AggregatedRating"))),
                            null, null, null, null)));
        return listRecipe;
    }
    public static List<Recipe> findTopRecipesForEachCategory(Integer minNumberReviews) throws MongoException{
        List<Recipe> listRecipe = new ArrayList<>();
        Bson match = match(new Document("Reviews." + minNumberReviews, new Document("$exists", true)));
        Bson sort = new Document("$sort", new Document("AggregatedRating", -1));
        Bson group = new Document("$group", new Document("_id", "$RecipeCategory")
                .append("Name", new Document("$first", "$Name"))
                .append("AggregatedRating", new Document("$first", "$AggregatedRating"))
                .append("Images", new Document("$first", "$Images")));
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE)
                .aggregate(Arrays.asList(match, sort, group)).forEach(doc ->{
                        listRecipe.add(new Recipe(doc.getString("Name"), null, null,
                        null, null, new ArrayList<>(Collections.singleton(doc.getList("Images", String.class).get(0))),
                        doc.getString("_id"), null, null, Double.valueOf(String.valueOf(doc.get("AggregatedRating"))),
                        null, null, null, null));}
                );
        return listRecipe;
    }
}
