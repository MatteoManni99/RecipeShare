package com.example.demo1;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import javafx.event.ActionEvent;
import com.mongodb.client.model.Sorts;
import javafx.fxml.FXMLLoader;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import java.util.Arrays;
import java.util.function.Consumer;

import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

public class LoggatoAnalyticsController {

    public void onTopRecipesForRangesOfPreparationTimeClick(ActionEvent actionEvent) {
        String uri = "mongodb://localhost:27017";
        System.out.println(StringToTime("PT1H24M"));
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            String totalTime;

            MongoDatabase database = mongoClient.getDatabase("RecipeShare");
            MongoCollection<Document> collection = database.getCollection("recipe");
            Bson limit1 = limit(5);
            Bson filter1 = new Document("Reviews.10",new Document("$exists",true));
            Bson match1 = match(filter1);
            Bson group1 = new Document("$group", new Document("_id", new Document("TotalTime","$TotalTime")))
                    .append("RecipeId", new Document("$addToSet", "$RecipeId"));
            collection.aggregate(Arrays.asList(match1)).forEach(printDocuments());
        }
    }
    private Integer StringToTime(String totalTimeString){
        Integer totalTimeInteger = 0;
        Integer indexOfH = totalTimeString.indexOf("H");
        Integer indexOfM = totalTimeString.indexOf("M");
        if (indexOfH>0 && indexOfM>0){
            totalTimeInteger = (Integer.valueOf(totalTimeString.substring(2,indexOfH))*60) +
                    Integer.valueOf(totalTimeString.substring(indexOfH+1,indexOfM));
        }else if(indexOfH>0  && indexOfM<0){
            totalTimeInteger = (Integer.valueOf(totalTimeString.substring(2,indexOfH))*60);
        }else if(indexOfH<0 && indexOfM>0){
            totalTimeInteger = (Integer.valueOf(totalTimeString.substring(2,indexOfM)));
        }
        return totalTimeInteger;
    }

    private static Consumer<Document> printDocuments() {
        return doc -> System.out.println(doc.toJson());
    }

    public void onRecipesWithHighestratingClick(ActionEvent actionEvent) {
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("RecipeShare");
            MongoCollection<Document> collection = database.getCollection("recipe");
            Bson filter1 = new Document("Reviews.4", new Document("$exists", true));
            collection.find(filter1).sort(Sorts.descending("AggregatedRating")).limit(10).iterator().forEachRemaining(printDocuments());
            /*MongoCursor<Document> cursor = collection.find(filter1).sort(Sorts.descending("AggregatedRating")).limit(10).iterator();
            while (cursor.hasNext()) {
                Document c = cursor.next();
                System.out.println(c);
                System.out.println(c.get("Name"));
                System.out.println(c.get("RecipeId"));
                System.out.println(c.get("AggregatedRating"));
                System.out.println(c.get("Reviews"));
                System.out.println("-----------------------");
            }*/
        }
    }

    public void onTopRecipesForEachCategory(ActionEvent actionEvent) {
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("RecipeShare");
            MongoCollection<Document> collection = database.getCollection("recipe");
            MongoCursor<Document> cursor = collection.find().sort(Sorts.descending("AggregateRating")).limit(10).iterator();
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LoggatoAnalytics.fxml"));
                /*stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load(), 600, 500);
                stage.setTitle("Hello "+ name);
                stage.setScene(scene);
                stage.show();*/
            }
        }
    }
}
