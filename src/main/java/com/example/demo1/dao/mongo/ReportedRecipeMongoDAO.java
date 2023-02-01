package com.example.demo1.dao.mongo;

import com.example.demo1.Configuration;
import com.example.demo1.model.Author;
import com.example.demo1.model.ReportedRecipe;
import com.example.demo1.persistence.MongoDBDriver;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.*;

public class ReportedRecipeMongoDAO {

    public static boolean addReportedRecipe(ReportedRecipe reportedRecipe) throws MongoException {
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
    private static Document fromReportedRecipeToDocument(ReportedRecipe reportedRecipe){
        return new Document("name",reportedRecipe.getName()).append("authorName",reportedRecipe.getAuthorName())
                .append("reporterName",reportedRecipe.getReporterName()).append("dateReporting",reportedRecipe.getDateReporting())
                .append("image",reportedRecipe.getImage());
    }
    private static ReportedRecipe fromDocToReportedRecipe(Document doc){
        return new ReportedRecipe(doc.getString("name"),doc.getString("authorName"),
                doc.getString("reporterName"),doc.getString("dateReporting"), doc.getString("images"));
    }

    public static List<ReportedRecipe> getListReportedRecipes() throws MongoException {
        List<ReportedRecipe> listaReportedRecipes = new ArrayList<>();
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_REPORTED_RECIPE).find()
                .forEach(document -> listaReportedRecipes.add(fromDocToReportedRecipe(document)));
        return listaReportedRecipes;
    }

    public static List<Author> onHighestRatioQueryClick() throws MongoException{
        Map<String, Double> map = new TreeMap<>();
        List<Author> authorList = new ArrayList<>();
        List<String> reportedAuthor = new ArrayList<>();
        Bson group1 = new Document("$group", new Document("_id", "$authorName")
                .append("count",new Document("$count",new Document())));
        Bson match1 = match(gte("count", 1));
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_REPORTED_RECIPE).aggregate(List.of(group1,match1))
                .forEach(document -> {
                    map.put(document.getString("_id"), document.getInteger("count").doubleValue());
                    reportedAuthor.add(document.getString("_id"));
                });

        Bson group2 = new Document("$group", new Document("_id", "$AuthorName")
                .append("count",new Document("$count",new Document())));
        Bson match2 = match(in("AuthorName", reportedAuthor));
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE).aggregate(List.of(match2,group2))
                .forEach(document -> {
                    String authorName = document.getString("_id");
                    Double score = document.getInteger("count") / map.get(authorName);
                    authorList.add(new Author(authorName,score));
                });

        authorList.sort(Comparator.comparingDouble(Author::getScore));

        System.out.println(authorList.get(0).getName());
        return authorList;
    }

}

