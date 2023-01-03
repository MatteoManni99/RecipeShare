package com.example.demo1;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import org.bson.Document;
import org.bson.conversions.Bson;

public class LoggatoAnalyticsController {

    public void onTopRecipesForRangesOfPreparationTimeClick(ActionEvent actionEvent) {
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("RecipeShare");
            System.out.println("TEST");
        }
    }
}
