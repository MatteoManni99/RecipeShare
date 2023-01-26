package com.example.demo1.dao;

import com.example.demo1.Configuration;
import com.example.demo1.DataSingleton;
import com.example.demo1.model.Moderator;
import com.example.demo1.persistence.MongoDBDriver;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class ModeratorDAO {

    public boolean checkModeratorName(String name) {
        try (MongoCursor<Document> cursorModerator = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_MODERATOR).find(eq("moderatorName", name)).iterator()) {
            if (cursorModerator.hasNext()) return true;
            else return false;
        }
    }

    public void tryLogin(Moderator moderator) {
        try (MongoCursor<Document> cursorModerator = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_MODERATOR).find(eq("moderatorName", moderator.getName())).iterator()) {
            if (cursorModerator.hasNext()) {
                Document currentModerator = cursorModerator.next();
                if (currentModerator.get("password").equals(moderator.getPassword())) {
                    System.out.println("TROVATO AUTHOR");
                    //nomePagina = "Loggato.fxml";
                }
            }
            else
                System.out.println("NON TROVATO AUTHOR");
        }
    }

    public void checkRegistration(Moderator moderator) {
        try (MongoCursor<Document> cursorModerator = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_MODERATOR).find(eq("moderatorName", moderator.getName())).iterator()) {

            MongoCollection authorCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_MODERATOR);
            if (cursorModerator.hasNext())
                System.out.println("NICKNAME GIA USATO");

            else {
                System.out.println("NICKNAME VALIDO");
                try {
                    authorCollection.insertOne(new Document()
                            .append("_id", new ObjectId())
                            .append("moderatorName", moderator.getName())
                            .append("password", moderator.getPassword()));
                }
                catch (MongoException me) {
                    System.err.println("Unable to insert due to an error: " + me);
                }
            }
        }
    }
}

