package com.example.demo1.dao;

import com.example.demo1.Configuration;

import com.example.demo1.model.Author;
import com.example.demo1.persistence.MongoDBDriver;
import com.mongodb.MongoException;
import com.mongodb.client.*;

import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Filters.eq;

public class AuthorDAO {

    public boolean tryLogin(String name,String password) {
        try (MongoCursor<Document> cursorAuthor = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_AUTHOR).find(eq("authorName", name)).iterator()) {
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
        }catch (MongoException me){
            return false;
        }
    }

    public boolean registration(String authorName, String password, Integer image, Integer standardPromotionValue) {
        MongoCollection authorCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR);
        if (getAuthor(authorName) != null){
            return false;
        } else {
            System.out.println("NICKNAME VALIDO");
            try {
                authorCollection.insertOne(new Document()
                        .append("_id", new ObjectId())
                        .append("authorName", authorName)
                        .append("password", password)
                        .append("promotion", standardPromotionValue)
                        .append("image", image));
                return true;
            } catch (MongoException me) {
                System.err.println("Unable to insert due to an error: " + me);
                return false;
            }
        }
    }

    public boolean updateImage(String authorName, Integer newImageIndex) {
        MongoCollection<Document> authorCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR);
        Document query = new Document().append("authorName", authorName);
        Bson updates = Updates.combine(Updates.set("image", newImageIndex));
        try {
            UpdateResult result = authorCollection.updateOne(query, updates);
            System.out.println("Modified document count: " + result.getModifiedCount());
            return true;
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
            return false;
        }
    }
    public boolean updatePromotion(String authorName, Integer newPromotionValue) {
        MongoCollection authorCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR);
        Document query = new Document().append("authorName", authorName);
        Bson updates = Updates.combine(Updates.set("promotion",newPromotionValue));
        UpdateOptions options = new UpdateOptions().upsert(true);
        try {
            authorCollection.updateOne(query, updates, options);
            System.out.println("Promotion updated");
            return true;
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
            return false;
        }
    }

    public boolean changeAuthorName(String newAuthorName, Author authorName) {
        MongoCollection<Document> authorCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR);
        MongoCollection<Document> recipeCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);

        if (getAuthor(newAuthorName) != null){
            return false;
        }else {
            Document query = new Document("authorName", authorName);
            Bson updates = Updates.combine(Updates.set("authorName", newAuthorName));
            Document queryRecipe = new Document("AuthorName", authorName);
            Bson updatesRecipe = Updates.combine(Updates.set("AuthorName", newAuthorName));
            try {
                authorCollection.updateOne(query, updates);
                recipeCollection.updateMany(queryRecipe, updatesRecipe);
                System.out.println("PARAMETRO CAMBIATO");
                return true;
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
                return false;
            }
        }
    }
    public boolean changePassword(String newPassword, Author author) {
        MongoCollection<Document> authorCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR);
        Document query = new Document().append("authorName", author.getName());
        Bson updates = Updates.combine(Updates.set("password", newPassword));
        try {
            authorCollection.updateOne(query, updates);
            System.out.println("PARAMETRO CAMBIATO");
            return true;
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
            return false;
        }
    }
    public Author getAuthor(String authorName) {
        try (MongoCursor<Document> cursor = MongoDBDriver.getDriver().
                getCollection(Configuration.MONGODB_AUTHOR).find(eq("authorName", authorName)).iterator()) {
            if (cursor.hasNext()) {
                Document doc = cursor.next();
                return new Author(doc.getString("authorName"),doc.getString("password"),
                        doc.getInteger("image"),doc.getInteger("promotion"));
            } else return null;
        }
    }

    public ArrayList<Author> searchAuthors(String nameToSearch, int elementsToSkip, int elementsToLimit){
        ArrayList<Author> authors = new ArrayList<Author>();
        MongoCollection<Document> collection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_AUTHOR);
        MongoCursor<Document> cursor;
        Bson filter = new Document("Name",new Document("$regex",nameToSearch).append("$options","i"));
        Bson match = match(filter);
        Bson project = project(new Document("authorName",1).append("promotion",1).append("image", 1));
        if(nameToSearch == null) {
            cursor = collection.aggregate(Arrays.asList(skip(elementsToSkip),limit(elementsToLimit),project)).iterator();
        }else {
            cursor = collection.aggregate(Arrays.asList(match, skip(elementsToSkip),limit(elementsToLimit),project)).iterator();
        }
        while(cursor.hasNext()){
            Document authorDoc = cursor.next();
            Author author = new Author(authorDoc.getString("name"),null,
                    authorDoc.getInteger("image"), authorDoc.getInteger("promotion"));
            authors.add(author);
        }
        return authors;
    }
}

