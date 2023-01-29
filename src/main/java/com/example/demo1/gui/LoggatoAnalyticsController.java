package com.example.demo1.gui;

import com.example.demo1.model.Recipe;
import com.example.demo1.service.RecipeService;
import javafx.event.ActionEvent;
import org.bson.Document;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class LoggatoAnalyticsController {
    private static Consumer<Document> printDocuments() {
        return doc -> System.out.println(doc.toJson());
    }

    public void onTopRecipesForRangesOfPreparationTimeClick(ActionEvent actionEvent) {
        System.out.println("*************************QUERY SUL RANGE********************************");
        List<Recipe> listFirstRangeRecipes = RecipeService.findTopRecipesForRangesOfPreparationTime(0,30,1,3);
        List<Recipe> listSecondRangeRecipes = RecipeService.findTopRecipesForRangesOfPreparationTime(31,90,1,3);
        List<Recipe> listThirdRangeRecipes = RecipeService.findTopRecipesForRangesOfPreparationTime(90,-1,1,3);
        System.out.println("RANGE DA 0 A 30");
        for (Recipe recipe : listFirstRangeRecipes) System.out.println(recipe);
        System.out.println("RANGE DA 31 A 90");
        for (Recipe recipe : listSecondRangeRecipes) System.out.println(recipe);
        System.out.println("RANGE DA 91 IN POI");
        for (Recipe recipe : listThirdRangeRecipes) System.out.println(recipe);

        /*String uri = Configuration.MONGODB_URL;
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("RecipeShare");
            MongoCollection<Document> collection = database.getCollection("recipe");
            //range 1 - 30 of TotalTime
            Bson match1 = match(new Document("TotalTime", new Document("$gt", 0).append("$lte", 30)));
            //range 31 - 90 of TotalTime
            Bson match2 = match(new Document("TotalTime", new Document("$gt", 30).append("$lte", 90)));
            //range 91 - * of TotalTime
            Bson match3 = match(new Document("TotalTime", new Document("$gt", 91)));
            //Bson group = new Document("$group", new Document("_id","$TotalTime"));
            Bson matchR = match(new Document("Reviews.19",new Document("$exists",true)));
            Bson sort = sort(descending("AggregatedRating"));
            Bson limit = limit(3);
            Bson project = project(include("Name","TotalTime","AggregatedRating"));
            System.out.println("Top Recipes with TotalTime: 1-30:");
            collection.aggregate(Arrays.asList(matchR,match1,sort,limit,project)).forEach(printDocuments());
            System.out.println("Top Recipes with TotalTime: 31-90:");
            collection.aggregate(Arrays.asList(matchR,match2,sort,limit,project)).forEach(printDocuments());
            System.out.println("Top Recipes with TotalTime: 91-*:");
            collection.aggregate(Arrays.asList(matchR,match3,sort,limit,project)).forEach(printDocuments());
        }*/
    }

    public void onMostUsedIngredientsClick(ActionEvent actionEvent) {
        System.out.println("***********************QUERY SUI MOST USED INGREDIENTS***************************");
        HashMap<String,Integer> map = RecipeService.findMostUsedIngredients(10,3);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }
        /*String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("RecipeShare");
            MongoCollection<Document> collection = database.getCollection("recipe");
            Bson matchR = match(new Document("Reviews.19",new Document("$exists",true))); //questo non so se metterlo
            Bson unwind = new Document("$unwind",new Document("path","$RecipeIngredientParts"));
            Bson group = new Document("$group", new Document("_id", "$RecipeIngredientParts").
                    append("count",new Document("$count",new Document())));
            Bson project = project(include("RecipeIngredientParts"));
            Bson limit = limit(10);
            Bson sort = sort(descending("count"));
            System.out.println("Most used Ingredients:");
            collection.aggregate(Arrays.asList(project,unwind,group,sort,limit)).forEach(printDocuments());
        }*/
    }

    public void onRecipesWithHighestratingClick(ActionEvent actionEvent) {
        System.out.println("********************QUERY RECIPES WITH HIGHEST RATING************************");
        List<Recipe> listRecipesWithHighestrating = RecipeService.findRecipesWithHighestRating(10,3);
        for (Recipe recipe : listRecipesWithHighestrating) System.out.println(recipe);
        /*
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("RecipeShare");
            MongoCollection<Document> collection = database.getCollection("recipe");
            Bson matchR = match(new Document("Reviews.19", new Document("$exists", true)));
            Bson limit = limit(10);
            Bson sort = sort(descending("AggregatedRating"));
            Bson project = project(include("Name","AggregatedRating"));
            System.out.println("Top Recipes:");
            collection.aggregate(Arrays.asList(matchR,sort,limit,project)).forEach(printDocuments());
            //collection.find(filter1).sort(Sorts.descending("AggregatedRating")).limit(10).iterator().forEachRemaining(printDocuments());
            MongoCursor<Document> cursor = collection.find(filter1).sort(Sorts.descending("AggregatedRating")).limit(10).iterator();
            while (cursor.hasNext()) {
                Document c = cursor.next();
                System.out.println(c);
                System.out.println(c.get("Name"));
                System.out.println(c.get("RecipeId"));
                System.out.println(c.get("AggregatedRating"));
                System.out.println(c.get("Reviews"));
                System.out.println("-----------------------");
            }
        }*/
    }

    //{$sort: {'color': 1, value: -1}},
    //{$group: {_id: '$color', value: {$first: '$value'}}}
    public void onTopRecipesForEachCategory(ActionEvent actionEvent) {
        System.out.println("*****************QUERY TOP RECIPES FOR EACH CATEGORY*******************************");
        List<Recipe> listTopRecipes = RecipeService.findTopRecipesForEachCategory(3);
        for (Recipe recipe : listTopRecipes) System.out.println(listTopRecipes);
        /*
        String uri = Configuration.MONGODB_URL;
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_RECIPE);
            Bson filter = new Document("Reviews.19",new Document("$exists",true));
            Bson match1 = match(filter);
            Bson sort = new Document("$sort", new Document("AggregatedRating",-1));
            Bson group = new Document("$group", new Document("_id", "$RecipeCategory")
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
            }
        }*/
    }
}
