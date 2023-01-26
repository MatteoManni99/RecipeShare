package com.example.demo1.dao;

import com.example.demo1.Configuration;
import com.example.demo1.model.Recipe;
import com.example.demo1.model.RecipeReducted;
import com.example.demo1.model.Review;
import com.example.demo1.persistence.MongoDBDriver;
import com.mongodb.client.*;
import javafx.event.ActionEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.limit;
public class RecipeDAO {

    public void deleteRecipe(Recipe recipe) {
        MongoCollection<Document> collectionRecipe = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
        try{
            collectionRecipe.deleteOne(new Document("Name", recipe.getName()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Recipe getRecipeByName(String name) {
        MongoCollection<Document> collectionRecipe = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
        try{
            return transformDocToRec(Objects.requireNonNull(collectionRecipe.aggregate(List.of(new Document("Name", name))).first()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addReview(String name,String reviewer,Integer rating,String review) {
        Bson match = new Document("Name", name);
        Bson updates = new Document("$push", new Document("Reviews", new Document("AuthorName", reviewer)
                .append("Rating", rating).append("Review", review)));
        MongoCollection<Document> collectionRecipe = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
        try{
            collectionRecipe.updateOne(match, updates);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void addToDatabase(Recipe recipe) {
        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_RECIPE);
            Document recipeToAdd = new Document().append("Name", recipe.getName()).append("AuthorName",recipe.getAuthorName())
                    .append("TotalTime",recipe.getTotalTime()).append("DatePublished", recipe.getDatePublished())
                    .append("Description",recipe.getDescription()).append("Images",recipe.getImages())
                    .append("RecipeCategory",recipe.getRecipeCategory()).append("Keywords",recipe.getKeywords())
                    .append("RecipeIngredientParts",recipe.getRecipeIngredientParts()).append("AggregatedRating",recipe.getAggregatedRating())
                    .append("Calories",recipe.getCalories()).append("RecipeServings",recipe.getRecipeServings())
                    .append("RecipeInstructions",recipe.getReviews()).append("Reviews",new ArrayList<String>());
            if(collection.insertOne(recipeToAdd).wasAcknowledged()){
                System.out.println("Recipe successfully added");
            }else System.out.println("Recipe was not added for some reason...");
        }
    }

    public boolean checkIfNameIsAvailable(String name){
        MongoCollection<Document> collectionRecipe = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
        try{
            return collectionRecipe.find(new Document("Name",name)).cursor().hasNext();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<RecipeReducted> getRecipeFromAuthor(String authorName, Integer pageNumber){
        List<RecipeReducted> recipeReducted = new ArrayList<RecipeReducted>();
        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_RECIPE);
            Bson match = match(new Document("AuthorName", new Document("$regex", authorName).append("$options", "i")));
            Bson project = project(new Document("Name", 1).append("AuthorName", 1)
                    .append("Images", new Document("$first", "$Images")));
            MongoCursor<Document> cursor;
            if(authorName == null){
                cursor = collection.aggregate(Arrays.asList(skip(10*pageNumber),limit(10),project)).iterator();
            }else{
                cursor = collection.aggregate(Arrays.asList(match,skip(10*pageNumber),limit(10),project)).iterator();
            }
            while (cursor.hasNext()){
                Document recipeDoc = cursor.next();
                recipeReducted.add(new RecipeReducted(recipeDoc.getString("Name"),
                        recipeDoc.getString("AuthorName"), recipeDoc.getString("Images")));
            }
        }
        return recipeReducted;
    }

    public Review transformDocToRev(Document doc){
        return new Review(doc.getString("AuthorName"), doc.getInteger("Rating"), doc.getString("Review"));
    }

    public Recipe transformDocToRec(Document doc){
        return new Recipe(doc.getString("Name"), doc.getString("AuthorName"), doc.getInteger("TotalTime"),
                doc.getString("DatePublished"), doc.getString("Description"), doc.getList("Images", String.class),
                doc.getString("RecipeCategory"), doc.getList("Keywords", String.class), doc.getList("RecipeIngredientParts", String.class),
                doc.getDouble("AggregatedRating"), doc.getDouble("Calories"), doc.getDouble("RecipeServings"),
                doc.getList("RecipeInstructions", String.class), fromDocListToRevList(doc.getList("Reviews", Document.class)));
    }

    public List<Review> fromDocListToRevList(List<Document> listDoc){
        List<Review> listReview = new ArrayList<>();
        for(Document doc: listDoc)
            listReview.add(transformDocToRev(doc));
        return listReview;
    }

    public List<Recipe> onTopRecipesForEachCategory(ActionEvent actionEvent) {
        Bson match1 = match(new Document("Reviews.19", new Document("$exists", true)));
        Bson sort = new Document("$sort", new Document("AggregatedRating", -1));
        Bson group = new Document("$group", new Document("_id", "$RecipeCategory")
                .append("Name", new Document("$first", "$Name"))
                .append("AggregatedRating", new Document("$first", "$AggregatedRating"))
                .append("Images", new Document("$first", "$Images")));
        List<Recipe> listRecipe = new ArrayList<>();
        try (MongoCursor<Document> cursor = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE)
                .aggregate(Arrays.asList(match1, sort, group)).iterator()) {
            while (cursor.hasNext()){
                Document doc = cursor.next();
                List<String> images = new ArrayList<>();
                images.add(doc.getString("Images"));
                listRecipe.add(new Recipe(doc.getString("Name"), null, null, null,null,
                        images, doc.getString("RecipeCategory"), null, null,
                                doc.getDouble("AggregatedRating"), null, null, null, null));
            }
        }
        return listRecipe;
    }


}
