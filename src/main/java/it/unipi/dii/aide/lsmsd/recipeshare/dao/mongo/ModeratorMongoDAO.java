package it.unipi.dii.aide.lsmsd.recipeshare.dao.mongo;

import it.unipi.dii.aide.lsmsd.recipeshare.Configuration;

import it.unipi.dii.aide.lsmsd.recipeshare.persistence.MongoDBDriver;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class ModeratorMongoDAO {

    public static boolean checkModeratorName(String name) throws MongoException{
        return MongoDBDriver.getDriver().getCollectionCP(Configuration.MONGODB_MODERATOR)
                .find(eq("moderatorName", name)).iterator().hasNext();
    }

    public static boolean tryLogin(String name, String password) throws MongoException{
        MongoCursor<Document> cursorModerator = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_MODERATOR).find(eq("moderatorName", name)).iterator();
        if (cursorModerator.hasNext()) {
            Document currentModerator = cursorModerator.next();
            return currentModerator.get("password").equals(password);
        }
        return false;
    }

    public static boolean checkRegistration(String name, String password) throws MongoException{
        MongoCursor<Document> cursorModerator = MongoDBDriver.getDriver().
                getCollectionCP(Configuration.MONGODB_MODERATOR).find(eq("moderatorName", name)).iterator();
        MongoCursor<Document> cursorAuthor = MongoDBDriver.getDriver().
                getCollectionCP(Configuration.MONGODB_AUTHOR).find(eq("authorName", name)).iterator();
        if (cursorModerator.hasNext() || cursorAuthor.hasNext()) {
            return false;
        }
        else {
            MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_MODERATOR)
                    .insertOne(new Document("_id", new ObjectId()).append("moderatorName", name)
                    .append("password",password));

            return true;
        }
    }

}

