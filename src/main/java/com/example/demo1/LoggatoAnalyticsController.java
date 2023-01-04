package com.example.demo1;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import javafx.event.ActionEvent;
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
}
