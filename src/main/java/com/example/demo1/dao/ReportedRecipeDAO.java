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

public class ReportedRecipeDAO {

    public boolean addRecipeToReportedRecipe(ReportedRecipe reportedRecipe) throws MongoException {
        MongoCollection<Document> reportedCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_REPORTED_RECIPE);
        Bson filter = Filters.and(
                Filters.eq("name", reportedRecipe.getName()),
                Filters.eq("reporterName", reportedRecipe.getReporterName()));
        if (reportedCollection.find(filter).cursor().hasNext()) {
            System.out.println("Avevi già Reportato questa Recipe");
            return false;
        }else{
            if(reportedCollection.insertOne(fromReportedRecipeToDocument(reportedRecipe)).wasAcknowledged()){
                System.out.println("Recipe Reportata");
                return true;
            }
        }
        return false;
    }
    private Document fromReportedRecipeToDocument(ReportedRecipe reportedRecipe){
        return new Document("name",reportedRecipe.getName()).append("authorName",reportedRecipe.getAuthorName())
                .append("reporterName",reportedRecipe.getReporterName()).append("dateReporting",reportedRecipe.getDateReporting())
                .append("image",reportedRecipe.getImage());
    }
    private ReportedRecipe fromDocToReportedRecipe(Document doc){
        return new ReportedRecipe(doc.getString("name"),doc.getString("authorName"),
                doc.getString("reporterName"),doc.getString("dateReporting"), doc.getString("image"));
    }

    public List<ReportedRecipe> getListReportedRecipes() throws MongoException {
        List<ReportedRecipe> listaReportedRecipes = new ArrayList<>();
        MongoCollection<Document> reportedCollection = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_REPORTED_RECIPE);
        for (Document document : reportedCollection.find())
            listaReportedRecipes.add(fromDocToReportedRecipe(document));

        return listaReportedRecipes;
    }
}

