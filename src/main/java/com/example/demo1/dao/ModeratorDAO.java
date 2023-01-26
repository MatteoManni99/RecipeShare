package com.example.demo1.dao;

import com.example.demo1.Configuration;

import com.example.demo1.model.Moderator;
import com.example.demo1.persistence.MongoDBDriver;
import com.mongodb.MongoException;
import com.mongodb.client.*;

import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class ModeratorDAO {

    public boolean checkModeratorName(String name) throws MongoException{
        return MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_MODERATOR)
                .find(eq("moderatorName", name)).iterator().hasNext();
    }

    public boolean tryLogin(Moderator moderator) throws MongoException{
        MongoCursor<Document> cursorModerator = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_MODERATOR).find(eq("moderatorName", moderator.getName())).iterator();
        if (cursorModerator.hasNext()) {
            Document currentModerator = cursorModerator.next();
            if (currentModerator.get("password").equals(moderator.getPassword())) {
                System.out.println("TROVATO MOD");
                return true;
            }
            return false;
        } else {
            System.out.println("NON TROVATO MOD");
            return false;
        }
    }

    public void checkRegistration(Moderator moderator) throws MongoException{
        MongoCursor<Document> cursorModerator = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_MODERATOR).find(eq("moderatorName", moderator.getName())).iterator();
        MongoCollection<Document> authorCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_MODERATOR);
        if (cursorModerator.hasNext())
            System.out.println("NICKNAME GIA USATO");
        else {
            System.out.println("NICKNAME VALIDO");
            authorCollection.insertOne(new Document("_id", new ObjectId()).append("moderatorName", moderator.getName())
                    .append("password", moderator.getPassword()));
        }
    }

}

