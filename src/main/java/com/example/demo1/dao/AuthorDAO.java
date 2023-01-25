package com.example.demo1.dao;

import com.example.demo1.Configuration;
import com.example.demo1.DataSingleton;
import com.example.demo1.persistence.MongoDBDriver;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.IOException;

import static com.mongodb.client.model.Filters.eq;

public class AuthorDAO {

    public boolean checkAuthorName(String name) {
        try (MongoCursor<Document> cursorAuthor = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_AUTHOR).find(eq("authorName", name)).iterator()) {
            if (cursorAuthor.hasNext()) return true;
            else return false;
        }
    }

    public void tryLogin(String name,String password) {
        try (MongoCursor<Document> cursorAuthor = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_AUTHOR).find(eq("authorName", name)).iterator()) {
            if (cursorAuthor.hasNext()) {
                Document currentAuthor = cursorAuthor.next();
                if (currentAuthor.get("password").equals(password)) {
                    int avatarIndex = (int) currentAuthor.get("image");
                    DataSingleton.getInstance().setAvatar(avatarIndex);
                    DataSingleton.getInstance().setAuthorPromotion((Integer) currentAuthor.get("promotion"));
                    DataSingleton.getInstance().setTypeOfUser("author");
                    System.out.println("TROVATO AUTHOR");
                    //nomePagina = "Loggato.fxml";
                }
            }
            else
                System.out.println("NON TROVATO AUTHOR");
        }
    }

    public void checkRegistration(String name,String password,int image) {
        try (MongoCursor<Document> cursorAuthor = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_AUTHOR).find(eq("authorName", name)).iterator()) {

            MongoCollection authorCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR);
            if (cursorAuthor.hasNext())
                System.out.println("NICKNAME GIA USATO");

            else {
                System.out.println("NICKNAME VALIDO");
                if (DataSingleton.getInstance().getTypeOfUser().equals("author")) {
                    try {
                        authorCollection.insertOne(new Document()
                                .append("_id", new ObjectId())
                                .append("authorName", name)
                                .append("password", password)
                                .append("promotion", 0)
                                .append("image", image));
                    }
                    catch (MongoException me) {
                        System.err.println("Unable to insert due to an error: " + me);
                    }
                }
                DataSingleton.getInstance().setAuthorName(name);
                DataSingleton.getInstance().setPassword(password);
            }
        }
    }

    public void updateImage(int index,String name) {
        try (MongoCursor<Document> cursorAuthor = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_AUTHOR).find(eq("authorName", name)).iterator()) {
            MongoCollection authorCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR);
            Document query = new Document().append("authorName", name);
            Bson updates = Updates.combine(
                    Updates.set("image", index)
            );
            UpdateOptions options = new UpdateOptions().upsert(true);
            try {
                UpdateResult result = authorCollection.updateOne(query, updates, options);
                System.out.println("Modified document count: " + result.getModifiedCount());
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }
    }

    public void changeParameter(String parameterToChange,String parameterNewValue,String name) {
        try (MongoCursor<Document> cursorAuthor = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_AUTHOR).find(eq("authorName", name)).iterator()) {
            MongoCollection authorCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR);
            if (parameterToChange.equals("authorName")) {
                if (checkAuthorName(parameterNewValue) == true) return; //esiste già il nome
                Document query = new Document().append("authorName", name);
                Bson updates = Updates.combine(
                        Updates.set(parameterToChange, parameterNewValue)
                );

                try {
                    UpdateResult result = authorCollection.updateOne(query, updates);
                    System.out.println("Modified document count: " + result.getModifiedCount());
                } catch (MongoException me) {
                    System.err.println("Unable to update due to an error: " + me);
                }
                System.out.println("PARAMETRO CAMBIATO");
                MongoCollection recipeCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
                Document queryRecipe = new Document().append("AuthorName", name);
                Bson updatesRecipe = Updates.combine(
                        Updates.set("AuthorName", parameterNewValue));

                try {
                    UpdateResult result = recipeCollection.updateMany(queryRecipe, updatesRecipe);
                    System.out.println("Modified document count: " + result.getModifiedCount());
                } catch (MongoException me) {
                    System.err.println("Unable to update due to an error: " + me);
                }
            }
            else {
                Document query = new Document().append("authorName", name);
                Bson updates = Updates.combine(
                        Updates.set(parameterToChange, parameterNewValue)
                );
                UpdateOptions options = new UpdateOptions().upsert(true);

                try {
                    UpdateResult result = authorCollection.updateOne(query, updates, options);
                    System.out.println("Modified document count: " + result.getModifiedCount());
                } catch (MongoException me) {
                    System.err.println("Unable to update due to an error: " + me);
                }
                System.out.println("PARAMETRO CAMBIATO");
            }
        }
    }
}

