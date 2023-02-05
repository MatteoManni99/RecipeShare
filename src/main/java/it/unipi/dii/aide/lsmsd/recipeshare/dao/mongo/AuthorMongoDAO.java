package it.unipi.dii.aide.lsmsd.recipeshare.dao.mongo;

import it.unipi.dii.aide.lsmsd.recipeshare.Configuration;

import it.unipi.dii.aide.lsmsd.recipeshare.model.Author;
import it.unipi.dii.aide.lsmsd.recipeshare.persistence.MongoDBDriver;
import com.mongodb.MongoException;
import com.mongodb.client.*;

import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Filters.eq;

public class AuthorMongoDAO {

    public static boolean tryLogin(String name, String password) throws MongoException{
        MongoCursor<Document> cursorAuthor = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_AUTHOR).find(eq("authorName", name)).iterator();
        if (cursorAuthor.hasNext()) {
            Document currentAuthor = cursorAuthor.next();
            if (currentAuthor.get("password").equals(password)) {
                System.out.println("TROVATO AUTHOR");
                return true;
            }
        }
        else{
            System.out.println("NON TROVATO AUTHOR");
        }
        return false;
    }

    public static boolean registration(String authorName, String password, Integer image, Integer standardPromotionValue) throws MongoException {
        //MongoCollection<Document> authorCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR);
        MongoCollection<Document> authorCollection = MongoDBDriver.getDriver().getCollectionCP(Configuration.MONGODB_AUTHOR);
        if (!checkIfUsernameIsAvailable(authorName)){
            return false;
        } else {
            System.out.println("NICKNAME VALIDO");
            authorCollection.insertOne(new Document().append("_id", new ObjectId()).append("authorName", authorName)
                    .append("password", password).append("promotion", standardPromotionValue).append("image", image));
            return true;
        }
    }
    public static void deleteAuthor(String authorName) throws MongoException{
        MongoDBDriver.getDriver().getCollectionCP(Configuration.MONGODB_AUTHOR).deleteOne(new Document("authorName",authorName));
    }

    public static void changeAvatar(String authorName, Integer newImageIndex) throws MongoException{
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR)
                .updateOne(new Document("authorName", authorName),
                    Updates.combine(Updates.set("image", newImageIndex)));
    }

    public static void updatePromotion(String authorName, Integer newPromotionValue) throws MongoException{
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR)
                .updateOne(new Document().append("authorName", authorName),
                            Updates.combine(Updates.set("promotion",newPromotionValue)),
                                new UpdateOptions().upsert(true));
    }

    @Deprecated
    public static boolean changeAuthorName(String newAuthorName, Author currentAuthor) throws MongoException{
        MongoCollection<Document> authorCollection = MongoDBDriver.getDriver().getCollectionCP(Configuration.MONGODB_AUTHOR);
        MongoCollection<Document> recipeCollection = MongoDBDriver.getDriver().getCollectionCP(Configuration.MONGODB_RECIPE);
        if (!checkIfUsernameIsAvailable(newAuthorName)){
            return false;
        }else {
            Document query = new Document("authorName", currentAuthor.getName());
            Bson updates = Updates.combine(Updates.set("authorName", newAuthorName));
            Document queryRecipe = new Document("AuthorName", currentAuthor.getName());
            Bson updatesRecipe = Updates.combine(Updates.set("AuthorName", newAuthorName));
            authorCollection.updateOne(query, updates);
            recipeCollection.updateMany(queryRecipe, updatesRecipe);
            return true;
        }
    }

    public static void changePassword(String newPassword, Author author)  throws MongoException{
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR).updateOne(
                new Document().append("authorName", author.getName()),
                    Updates.combine(Updates.set("password", newPassword)));
        System.out.println("Pass CAMBIATA");
    }

    public static Author getAuthor(String authorName)  throws MongoException{
        MongoCursor<Document> cursor = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_AUTHOR).find(eq("authorName", authorName)).iterator();
        if (cursor.hasNext()) {
            Document doc = cursor.next();
            return new Author(doc.getString("authorName"),doc.getString("password"),
                    doc.getInteger("image"),doc.getInteger("promotion"));
        } else return null;
    }

    public static boolean checkIfUsernameIsAvailable(String authorName)  throws MongoException{ //uso CP
        MongoCursor<Document> cursor = MongoDBDriver.getDriver().
                getCollectionCP(Configuration.MONGODB_AUTHOR).find(eq("authorName", authorName)).iterator();
        return !cursor.hasNext();
    }

    public static ArrayList<Author> searchAuthors(String nameToSearch, Integer elementsToSkip, Integer elementsToLimit) throws MongoException{
        ArrayList<Author> authors = new ArrayList<Author>();
        MongoCollection<Document> collection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR);
        Bson filter = new Document("authorName",new Document("$regex",nameToSearch).append("$options","i"));
        Bson match = match(filter);
        Bson project = project(new Document("authorName",1).append("promotion",1).append("image", 1));
        MongoCursor<Document> cursor = (nameToSearch == null) ?
                collection.aggregate(Arrays.asList(skip(elementsToSkip),limit(elementsToLimit),project)).iterator() :
                collection.aggregate(Arrays.asList(match, skip(elementsToSkip),limit(elementsToLimit),project)).iterator();
        cursor.forEachRemaining(authorDoc -> authors.add(new Author(authorDoc.getString("authorName"),null,
                authorDoc.getInteger("image"), authorDoc.getInteger("promotion"))));
        return authors;
    }
}

