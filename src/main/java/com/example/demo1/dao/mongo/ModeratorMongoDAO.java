package com.example.demo1.dao.mongo;

import com.example.demo1.Configuration;

import com.example.demo1.persistence.MongoDBDriver;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class ModeratorMongoDAO {

    public static boolean checkModeratorName(String name) throws MongoException{
        /*return MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_MODERATOR)
                .find(eq("moderatorName", name)).iterator().hasNext();*/
        return MongoDBDriver.getDriver().getCollectionCP(Configuration.MONGODB_MODERATOR)
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
        } else {
            System.out.println("NON TROVATO MOD");
        }
        return false;
    }

    public static boolean checkRegistration(String name, String password) throws MongoException{
        /*MongoCursor<Document> cursorModerator = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_MODERATOR).find(eq("moderatorName", name)).iterator();*/
        MongoCursor<Document> cursorModerator = MongoDBDriver.getDriver().
                getCollectionCP(Configuration.MONGODB_MODERATOR).find(eq("moderatorName", name)).iterator();
        if (cursorModerator.hasNext()) {
            System.out.println("NICKNAME GIA USATO");
            return false;
        }
        else {
            System.out.println("NICKNAME VALIDO");
            MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_MODERATOR)
                    .insertOne(new Document("_id", new ObjectId()).append("moderatorName", name)
                    .append("password",password));

            return true;
        }
    }

}

