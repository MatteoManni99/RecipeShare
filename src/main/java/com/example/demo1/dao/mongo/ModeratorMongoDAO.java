package com.example.demo1.dao.mongo;

import com.example.demo1.Configuration;

import com.example.demo1.model.Moderator;
import com.example.demo1.persistence.MongoDBDriver;
import com.mongodb.MongoException;
import com.mongodb.client.*;

import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class ModeratorMongoDAO {

    public static boolean checkModeratorName(String name) throws MongoException{
        return MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_MODERATOR)
                .find(eq("moderatorName", name)).iterator().hasNext();
    }

    public static boolean tryLogin(String name, String password) throws MongoException{
        MongoCursor<Document> cursorModerator = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_MODERATOR).find(eq("moderatorName", name)).iterator();
        if (cursorModerator.hasNext()) {
            Document currentModerator = cursorModerator.next();
            if (currentModerator.get("password").equals(password)) {
                System.out.println("TROVATO MOD");
                return true;
            }
            return false;
        } else {
            System.out.println("NON TROVATO MOD");
            return false;
        }
    }

    public static boolean checkRegistration(Moderator moderator) throws MongoException{
        MongoCursor<Document> cursorModerator = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_MODERATOR).find(eq("moderatorName", moderator.getName())).iterator();
        MongoCollection<Document> moderatorCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_MODERATOR);
        if (cursorModerator.hasNext()) {
            System.out.println("NICKNAME GIA USATO");
            return false;
        }
        else {
            System.out.println("NICKNAME VALIDO");
            moderatorCollection.insertOne(new Document("_id", new ObjectId()).append("moderatorName", moderator.getName())
                    .append("password", moderator.getPassword()));

            return true;
        }
    }

}

