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
import static com.mongodb.client.model.Indexes.descending;
import static com.mongodb.client.model.Projections.*;

public class LoggatoAnalyticsController {

    public void onTopRecipesForRangesOfPreparationTimeClick(ActionEvent actionEvent) {
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            String totalTime;
            MongoDatabase database = mongoClient.getDatabase("RecipeShare");
            MongoCollection<Document> collection = database.getCollection("recipe");
            //at least n reviews
            Bson filter = new Document("Reviews.20",new Document("$exists",true));
            Bson match = match(filter);
            //range 1 - 30 of TotalTime
            Bson filter1 = new Document("TotalTime", new Document("$gt", 0).append("$lte", 30));
            Bson match1 = match(filter1);
            //range 31 - 90 of TotalTime
            Bson filter2 = new Document("TotalTime", new Document("$gt", 30).append("$lte", 90));
            Bson match2 = match(filter2);
            //range 91 - * of TotalTime
            Bson filter3 = new Document("TotalTime", new Document("$gt", 91));
            Bson match3 = match(filter3);
            //----
            Bson group = new Document("$group", new Document("_id", new Document("TotalTime","$TotalTime")));
            Bson sort = sort(descending("AggregatedRating"));
            Bson limit = limit(3);
            Bson project = project(include("RecipeId","Name","TotalTime","AggregatedRating"));
            System.out.println("Top Recipes with TotalTime: 1-30");
            collection.aggregate(Arrays.asList(match,match1,sort,limit,project)).forEach(printDocuments());
            System.out.println("Top Recipes with TotalTime: 31-90");
            collection.aggregate(Arrays.asList(match,match2,sort,limit,project)).forEach(printDocuments());
            System.out.println("Top Recipes with TotalTime: 91-*");
            collection.aggregate(Arrays.asList(match,match3,sort,limit,project)).forEach(printDocuments());
        }
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

    //{$sort: {'color': 1, value: -1}},
    //{$group: {_id: '$color', value: {$first: '$value'}}}
    public void onTopRecipesForEachCategory(ActionEvent actionEvent) {
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("RecipeShare");
            MongoCollection<Document> collection = database.getCollection("recipe");
            Bson filter1 = new Document("Reviews.4",new Document("$exists",true));
            Bson match1 = match(filter1);
            Bson sort = new Document("$sort", new Document("AggregatedRating",-1));
            Bson group = new Document("$group", new Document("_id", "$RecipeCategory")
                    .append("RecipeId",new Document("$first","$RecipeId"))
                    .append("Name",new Document("$first","$Name"))
                    .append("AggregatedRating",new Document("$first","$AggregatedRating"))
                    .append("Images",new Document("$first","$Images")));
            collection.aggregate(Arrays.asList(match1,sort,group)).forEach(printDocuments());

            /*MongoCursor<Document> cursor = collection.find().sort(Sorts.descending("AggregateRating")).limit(10).iterator();
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LoggatoAnalytics.fxml"));
                stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(fxmlLoader.load(), 600, 500);
                stage.setTitle("Hello "+ name);
                stage.setScene(scene);
                stage.show();
            }*/
        }
    }
}
