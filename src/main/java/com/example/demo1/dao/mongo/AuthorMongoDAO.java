package com.example.demo1.dao.mongo;

import com.example.demo1.Configuration;

import com.example.demo1.model.Author;
import com.example.demo1.persistence.MongoDBDriver;
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
        MongoCollection<Document> authorCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR);
        if (getAuthor(authorName) != null){
            return false;
        } else {
            System.out.println("NICKNAME VALIDO");
            authorCollection.insertOne(new Document().append("_id", new ObjectId()).append("authorName", authorName)
                    .append("password", password).append("promotion", standardPromotionValue).append("image", image));
            return true;
        }
    }

    public static void updateImage(String authorName, Integer newImageIndex) throws MongoException{
        MongoCollection<Document> authorCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR);
        Document query = new Document().append("authorName", authorName);
        Bson updates = Updates.combine(Updates.set("image", newImageIndex));
        authorCollection.updateOne(query, updates);
    }

    public static void updatePromotion(String authorName, Integer newPromotionValue) throws MongoException{
        Document query = new Document().append("authorName", authorName);
        Bson updates = Updates.combine(Updates.set("promotion",newPromotionValue));
        UpdateOptions options = new UpdateOptions().upsert(true);
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR).updateOne(query, updates, options);
    }

    public static boolean changeAuthorName(String newAuthorName, Author currentAuthor) throws MongoException{
        MongoCollection<Document> authorCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR);
        MongoCollection<Document> recipeCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);

        if (getAuthor(newAuthorName) != null){
            System.out.println("ESISTE GIA IL NICK");
            return false;
        }else {
            ///DA FARE IL CAMBIO ANCHE DELLE REVIEW/// quindi forse è meglio non permettere il cambio dell'author name?
            //perchè tanto costoso
            Document query = new Document("authorName", currentAuthor.getName());
            Bson updates = Updates.combine(Updates.set("authorName", newAuthorName));
            Document queryRecipe = new Document("AuthorName", currentAuthor.getName());
            Bson updatesRecipe = Updates.combine(Updates.set("AuthorName", newAuthorName));
            authorCollection.updateOne(query, updates);
            recipeCollection.updateMany(queryRecipe, updatesRecipe);
            System.out.println("PARAMETRO CAMBIATO");
            return true;
        }
    }
    public static void changePassword(String newPassword, Author author)  throws MongoException{
        Document query = new Document().append("authorName", author.getName());
        Bson updates = Updates.combine(Updates.set("password", newPassword));
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR).updateOne(query, updates);
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

    public static ArrayList<Author> searchAuthors(String nameToSearch, Integer elementsToSkip, Integer elementsToLimit) throws MongoException{
        ArrayList<Author> authors = new ArrayList<Author>();
        MongoCollection<Document> collection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR);
        MongoCursor<Document> cursor;
        Bson filter = new Document("authorName",new Document("$regex",nameToSearch).append("$options","i"));
        Bson match = match(filter);
        Bson project = project(new Document("authorName",1).append("promotion",1).append("image", 1));
        if(nameToSearch == null) {
            cursor = collection.aggregate(Arrays.asList(skip(elementsToSkip),limit(elementsToLimit),project)).iterator();
        }else {
            cursor = collection.aggregate(Arrays.asList(match, skip(elementsToSkip),limit(elementsToLimit),project)).iterator();
        }
        while(cursor.hasNext()){
            Document authorDoc = cursor.next();
            authors.add(new Author(authorDoc.getString("authorName"),null,
                    authorDoc.getInteger("image"), authorDoc.getInteger("promotion")));
        }
        return authors;
    }
}

