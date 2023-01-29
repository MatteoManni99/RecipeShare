package com.example.demo1.dao.mongo;

import com.example.demo1.Configuration;
import com.example.demo1.model.ReportedRecipe;
import com.example.demo1.persistence.MongoDBDriver;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Indexes.descending;

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
                doc.getString("reporterName"),doc.getString("dateReporting"), doc.getString("image"));
    }

    public static List<ReportedRecipe> getListReportedRecipes() throws MongoException {
        List<ReportedRecipe> listaReportedRecipes = new ArrayList<>();
        MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_REPORTED_RECIPE).find()
                .forEach(document -> listaReportedRecipes.add(fromDocToReportedRecipe(document)));
        return listaReportedRecipes;
    }

    public static Map<String, Double> onHighestRatioQueryClick(){
        Map<String, Integer> map = new TreeMap<>();
        Map<String, Double> mapRatio = new TreeMap<>();
        MongoCollection<Document> collectionRecipe = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_REPORTED_RECIPE);
        MongoCollection<Document> collectionReportedRecipes = MongoDBDriver.getDriver().getCollection(Configuration.MONGODB_RECIPE);
        Bson group1 = new Document("$group", new Document("_id", "$AuthorName")
                .append("count",new Document("$count",new Document())));
        Bson group2 = new Document("$group", new Document("_id", "$AuthorName")
                .append("count",new Document("$count",new Document())));
        //Bson match = match(gt("count", 1));
        Bson sort = sort(descending("_id"));

        collectionRecipe.aggregate(List.of(group1)).forEach(document ->
                map.put(String.valueOf(document.getString("_id")),document.getInteger("count")));

        for (Document document : collectionReportedRecipes.aggregate(List.of(group2))) {
            String authorName = document.getString("_id");
            if (map.containsKey(authorName) && map.get(authorName)>1) //con questo tolgo quelli che hanno creato 1 ricetta (si può aumentare la soglia o togliere)
                mapRatio.put(authorName,((double)document.getInteger("count"))/map.get(authorName));
        }
        return sortByValue(mapRatio,false);
    }

    private static Map<String, Double> sortByValue(Map<String, Double> unsortMap, final boolean order)
    {
        List<Map.Entry<String, Double>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }
}

