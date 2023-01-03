package com.example.demo1;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import javafx.event.ActionEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;
import java.util.function.Consumer;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

public class LoggatoAnalyticsController {

    public void onTopRecipesForRangesOfPreparationTimeClick(ActionEvent actionEvent) {
        String uri = "mongodb://localhost:27017";
        System.out.println(StringToTime("PT1H24M"));
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            //gestione del total time
            String totalTime;
            MongoDatabase database = mongoClient.getDatabase("RecipeShare");
            MongoCollection<Document> collection = database.getCollection("recipe");
            collection.aggregate(
                    Arrays.asList(

                            match(size("Reviews",5))
                            //qui volendo altri Aggregates
                    )
            ).forEach(printDocuments());
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
