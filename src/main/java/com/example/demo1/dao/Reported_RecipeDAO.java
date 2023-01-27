package com.example.demo1.dao;

import com.example.demo1.Configuration;
import com.example.demo1.model.ReportedRecipe;
import com.example.demo1.persistence.MongoDBDriver;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class Reported_RecipeDAO {

    public boolean addRecipeToReportedRecipe(ReportedRecipe reportedRecipe) throws MongoException {
        MongoCollection reportedCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_REPORTED_RECIPE);
        Bson filter = Filters.and(
                Filters.eq("name", reportedRecipe.getName()),
                Filters.eq("reporterName", reportedRecipe.getReporterName());
        if (reportedCollection.find(filter).cursor().hasNext()) {
            System.out.println("Avevi già Reportato questa Recipe");
            return false;
        }else{
            if(reportedCollection.insertOne(reportedRecipe).wasAcknowledged()){
                System.out.println("Recipe Reportata");
                return true;
            }
        }
        return false;
    }

    public List<Document> getListReportedRecipes() throws MongoException {
        List<Document> listaReportedRecipes = new ArrayList<>();
        MongoCollection reportedCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_REPORTED_RECIPE);
        MongoCursor<Document> cursor = reportedCollection.find().iterator();
        while (cursor.hasNext())
            listaReportedRecipes.add(cursor.next());

        return listaReportedRecipes;
    }
}

