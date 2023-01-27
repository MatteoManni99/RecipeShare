package com.example.demo1.dao.mongo;

import com.example.demo1.Configuration;
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
import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Indexes.descending;
import static com.mongodb.client.model.Projections.include;

public class RecipeMongoDAO {

    public void deleteRecipe(Recipe recipe) throws MongoException{
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE).deleteOne(new Document("Name", recipe.getName()));
    }

    public Recipe getRecipeByName(String name) throws MongoException{
        MongoCollection<Document> collectionRecipe = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
        return transformDocToRec(Objects.requireNonNull(collectionRecipe.aggregate(List.of(new Document("Name", name))).first()));
    }

    public void addReview(String name,String reviewer,Integer rating,String review) throws MongoException {
        Bson match = new Document("Name", name);
        Bson updates = new Document("$push", new Document("Reviews", new Document("AuthorName", reviewer)
                .append("Rating", rating).append("Review", review)));
        MongoCollection<Document> collectionRecipe = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
        collectionRecipe.updateOne(match, updates);
    }

    public void addRecipe(Recipe recipe) throws MongoException {
        Document recipeToAdd = new Document().append("Name", recipe.getName()).append("AuthorName",recipe.getAuthorName())
                .append("TotalTime",recipe.getTotalTime()).append("DatePublished", recipe.getDatePublished())
                .append("Description",recipe.getDescription()).append("Images",recipe.getImages())
                .append("RecipeCategory",recipe.getRecipeCategory()).append("Keywords",recipe.getKeywords())
                .append("RecipeIngredientParts",recipe.getRecipeIngredientParts()).append("AggregatedRating",recipe.getAggregatedRating())
                .append("Calories",recipe.getCalories()).append("RecipeServings",recipe.getRecipeServings())
                .append("RecipeInstructions",recipe.getReviews()).append("Reviews",new ArrayList<String>());
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE).insertOne(recipeToAdd);
    }

    public boolean checkIfNameIsAvailable(String name) throws MongoException{
        MongoCollection<Document> collectionRecipe = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
        return !collectionRecipe.find(new Document("Name",name)).cursor().hasNext();
    }

    public List<RecipeReducted> getRecipeFromAuthor(String authorName, Integer elementToSkip, Integer elementsToLimit) throws MongoException{
        List<RecipeReducted> recipeReducted = new ArrayList<RecipeReducted>();
        MongoCollection<Document> collection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
        Bson match = match(new Document("AuthorName", new Document("$regex", authorName).append("$options", "i")));
        Bson project = project(new Document("Name", 1).append("AuthorName", 1)
                .append("Images", new Document("$first", "$Images")));
        MongoCursor<Document> cursor;
        if(authorName == null){
            cursor = collection.aggregate(Arrays.asList(skip(elementToSkip),limit(elementsToLimit),project)).iterator();
        }else{
            cursor = collection.aggregate(Arrays.asList(match,skip(elementToSkip),limit(elementsToLimit),project)).iterator();
        }
        while (cursor.hasNext()){
            Document recipeDoc = cursor.next();
            recipeReducted.add(new RecipeReducted(recipeDoc.getString("Name"),
                    recipeDoc.getString("AuthorName"), recipeDoc.getString("Images")));
        }
        return recipeReducted;
    }

    public Review transformDocToRev(Document doc) throws MongoException{
        return new Review(doc.getString("AuthorName"), doc.getInteger("Rating"), doc.getString("Review"));
    }

    public Recipe transformDocToRec(Document doc) throws MongoException{
        return new Recipe(doc.getString("Name"), doc.getString("AuthorName"), doc.getInteger("TotalTime"),
                doc.getString("DatePublished"), doc.getString("Description"), doc.getList("Images", String.class),
                doc.getString("RecipeCategory"), doc.getList("Keywords", String.class), doc.getList("RecipeIngredientParts", String.class),
                doc.getDouble("AggregatedRating"), doc.getDouble("Calories"), doc.getDouble("RecipeServings"),
                doc.getList("RecipeInstructions", String.class), fromDocListToRevList(doc.getList("Reviews", Document.class)));
    }

    public List<Review> fromDocListToRevList(List<Document> listDoc) throws MongoException{
        List<Review> listReview = new ArrayList<>();
        for(Document doc: listDoc)
            listReview.add(transformDocToRev(doc));
        return listReview;
    }

    /// methods for analytics ///
    public List<Recipe> findTopRecipesForRangesOfPreparationTime(Integer lowerLimit, Integer upperLimit, Integer minNumeberReviews,
                                                                 Integer limitRecipes) throws MongoException{
        List<Recipe> listRecipe = new ArrayList<>();
        Bson match = match(new Document("TotalTime", new Document("$gt",lowerLimit).append("$lte", upperLimit)));
        Bson matchR = match(new Document("Reviews." + minNumeberReviews, new Document("$exists",true)));
        Bson sort = sort(descending("AggregatedRating"));
        Bson limit = limit(limitRecipes);
        Bson project = project(new Document("Name", 1).append("TotalTime", 1).append("AggregatedRating",1).append("Images", new Document("$first", "$Images")));
        for (Document doc : MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE)
                .aggregate(Arrays.asList(matchR,match,sort,limit,project))) {
            List<String> images = new ArrayList<>();
            images.add(doc.getString("Images"));
            listRecipe.add(new Recipe(doc.getString("Name"), null, doc.getInteger("TotalTime"), null, null,
                    images, null, null, null,
                    Double.valueOf(String.valueOf(doc.get("AggregatedRating"))), null, null, null, null));
        }
        return listRecipe;
    }
    public HashMap<String,Integer> findMostUsedIngredients(Integer limitIngredients, Integer minNumeberReviews) throws MongoException{
        Bson matchR = match(new Document("Reviews." + minNumeberReviews, new Document("$exists",true)));
        Bson unwind = new Document("$unwind",new Document("path","$RecipeIngredientParts"));
        Bson group = new Document("$group", new Document("_id", "$RecipeIngredientParts").
                append("count",new Document("$count",new Document())));
        Bson project = project(include("RecipeIngredientParts"));
        Bson limit = limit(limitIngredients);
        Bson sort = sort(descending("count"));
        HashMap<String,Integer> mapIngredient = new HashMap<>();
        for (Document doc : MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE)
                .aggregate(Arrays.asList(matchR,project,unwind,group,sort,limit))) {
            mapIngredient.put(doc.getString("_id"),doc.getInteger("count"));
        }
        return mapIngredient;
    }
    public List<Recipe> findRecipesWithHighestRating(Integer limitRecipes, Integer minNumeberReviews) throws MongoException{
        Bson matchR = match(new Document("Reviews." + minNumeberReviews, new Document("$exists", true)));
        Bson limit = limit(limitRecipes);
        Bson sort = sort(descending("AggregatedRating"));
        Bson project = project(new Document("Name", 1).append("AggregatedRating",1).append("Images", new Document("$first", "$Images")));
        List<Recipe> listRecipe = new ArrayList<>();
        for (Document doc : MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE)
                .aggregate(Arrays.asList(matchR,sort,limit,project))) {
            List<String> images = new ArrayList<>();
            images.add(doc.getString("Images"));
            listRecipe.add(new Recipe(doc.getString("Name"), null, null, null, null,
                    images, null, null, null,
                    Double.valueOf(String.valueOf(doc.get("AggregatedRating"))), null, null, null, null));
        }
        return listRecipe;
    }
    public List<Recipe> findTopRecipesForEachCategory(Integer minNumeberReviews) throws MongoException{
        List<Recipe> listRecipe = new ArrayList<>();
        Bson match = match(new Document("Reviews." + minNumeberReviews, new Document("$exists", true)));
        Bson sort = new Document("$sort", new Document("AggregatedRating", -1));
        Bson group = new Document("$group", new Document("_id", "$RecipeCategory")
                .append("Name", new Document("$first", "$Name"))
                .append("AggregatedRating", new Document("$first", "$AggregatedRating"))
                .append("Images", new Document("$first", "$Images")));
        for (Document doc : MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE)
                .aggregate(Arrays.asList(match, sort, group))) {
            List<String> images = new ArrayList<>();
            images.add(doc.getList("Images", String.class).get(0));
            listRecipe.add(new Recipe(doc.getString("Name"), null, null, null, null,
                    images, doc.getString("_id"), null, null,
                    Double.valueOf(String.valueOf(doc.get("AggregatedRating"))), null, null, null, null));
        }
        return listRecipe;
    }
}
