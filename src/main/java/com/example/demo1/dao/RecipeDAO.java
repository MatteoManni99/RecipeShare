package com.example.demo1.dao;

import com.example.demo1.Configuration;
import com.example.demo1.model.Recipe;
import com.example.demo1.persistence.MongoDBDriver;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Filters.eq;

public class RecipeDAO {

    public void deleteRecipe(Recipe recipe) {
        try(MongoCursor<Document> cursorRecipe = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_RECIPE).find(eq("Name", recipe.getName())).iterator()) {
            MongoCollection<Document> collectionRecipe = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
            Bson match = new Document("Name", recipe.getName());
            collectionRecipe.deleteOne(match);
        }
    }

    public Recipe getRecipeByName(String name) {
        try (MongoCursor<Document> cursorRecipe = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_RECIPE).find(eq("Name", name)).iterator()) {
            MongoCollection<Document> collectionRecipe = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
            Document doc = collectionRecipe.aggregate(Arrays.asList(new Document("Name", name))).first();
            return new Recipe(doc.getString("Name"), doc.getString("AuthorName"), doc.getInteger("TotalTime"),
                    doc.getString("DatePublished"), doc.getString("Description"), doc.getList("Images", String.class),
                    doc.getString("RecipeCategory"), doc.getList("Keywords", String.class), doc.getList("RecipeIngredientParts", String.class),
                    doc.getDouble("AggregatedRating"), doc.getDouble("Calories"), doc.getDouble("RecipeServings"),
                    doc.getList("RecipeInstructions", String.class), doc.getList("Reviews", Document.class));

        }
    }

    public void updateRecipe(String name,String reviewer,Integer rating,String review) {
        try (MongoCursor<Document> cursorRecipe = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_RECIPE).find(eq("Name", name)).iterator()) {
            MongoCollection<Document> collectionRecipe = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
            Bson match = new Document("Name", name);
            Bson updates = new Document("$push", new Document("Reviews", new Document("AuthorName", reviewer)
                    .append("Rating", rating).append("Review", review)));
            collectionRecipe.updateOne(match, updates);
        }
    }

    public void addToDatabase(Document recipeToAdd) {
        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_RECIPE);
            if(collection.insertOne(recipeToAdd).wasAcknowledged()){
                System.out.println("Recipe successfully added");
                //clearFields();
            }else System.out.println("Recipe was not added for some reason...");
        }
    }

    public boolean checkIfNameIsAvailable(String name){
        MongoCollection<Document> collectionRecipe = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
        Bson filter = new Document("Name",name);
        return !collectionRecipe.find(filter).cursor().hasNext();
    }

    public MongoCursor<Document> getRecipeFromAuthor(String authorName, Integer pageNumber){
        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_RECIPE);
            Bson filter = new Document("AuthorName", new Document("$regex", authorName).append("$options", "i"));
            Bson match = match(filter);
            Bson project = project(new Document("Name", 1).append("AuthorName", 1)
                    .append("Images", new Document("$first", "$Images")));
            return authorName == null ?
                    collection.aggregate(Arrays.asList(skip(10 * pageNumber), limit(10), project)).iterator() :
                    collection.aggregate(Arrays.asList(match,skip(10*pageNumber),limit(10),project)).iterator();
        }
    }

    public AggregateIterable<Document> onTopRecipesForEachCategory(ActionEvent actionEvent) {
        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_RECIPE);
            Bson filter = new Document("Reviews.19", new Document("$exists", true));
            Bson match1 = match(filter);
            Bson sort = new Document("$sort", new Document("AggregatedRating", -1));
            Bson group = new Document("$group", new Document("_id", "$RecipeCategory")
                    .append("Name", new Document("$first", "$Name"))
                    .append("AggregatedRating", new Document("$first", "$AggregatedRating"))
                    .append("Images", new Document("$first", "$Images")));
            return collection.aggregate(Arrays.asList(match1, sort, group));
        }
    }


}
