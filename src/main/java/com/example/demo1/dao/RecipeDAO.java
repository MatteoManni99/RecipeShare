package com.example.demo1.dao;

import com.example.demo1.Configuration;
import com.example.demo1.Recipe;
import com.example.demo1.persistence.MongoDBDriver;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;

import static com.mongodb.client.model.Aggregates.match;
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

    public void getRecipeByName(String name) {
        try (MongoCursor<Document> cursorRecipe = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_RECIPE).find(eq("Name", name)).iterator()) {
            MongoCollection<Document> collectionRecipe = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
            Bson match = new Document("Name", name);
        }
    }

    public void updateRecipe(String name,String reviewer,int rating,String review) {
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

}
