package com.example.demo1.gui;

import com.example.demo1.model.Recipe;
import com.example.demo1.service.RecipeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.bson.Document;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;

public class LoggatoAnalyticsController implements Initializable {


    @FXML
    private AnchorPane anchorPane;

    private static Consumer<Document> printDocuments() {
        return doc -> System.out.println(doc.toJson());
    }

    public void onTopRecipesForRangesOfPreparationTimeClick(ActionEvent actionEvent) {
        TableViewRecipeTime tableView = new TableViewRecipeTime();
        tableView.initializeTableView();
        tableView.setEventForTableCells();
        tableView.resetObservableArrayList();

        RecipeService.findTopRecipesForRangesOfPreparationTime(0,30,1,3)
                .forEach(recipe -> tableView.addToObservableArrayList(new RowRecipeTime(
                        recipe.getName(), recipe.getTotalTime(), recipe.getAggregatedRating(),
                        new ImageView(recipe.getImages().get(0)))));
        RecipeService.findTopRecipesForRangesOfPreparationTime(31,90,1,3)
                .forEach(recipe -> tableView.addToObservableArrayList(new RowRecipeTime(
                        recipe.getName(), recipe.getTotalTime(), recipe.getAggregatedRating(),
                        new ImageView(recipe.getImages().get(0)))));
        RecipeService.findTopRecipesForRangesOfPreparationTime(90,-1,1,3)
                .forEach(recipe -> tableView.addToObservableArrayList(new RowRecipeTime(
                        recipe.getName(), recipe.getTotalTime(), recipe.getAggregatedRating(),
                        new ImageView(recipe.getImages().get(0)))));

        tableView.setItems();
        tableView.setTable();
        anchorPane.getChildren().add(tableView.getTable());
    }

    public void onMostUsedIngredientsClick(ActionEvent actionEvent) {
        System.out.println("***********************QUERY SUI MOST USED INGREDIENTS***************************");
        HashMap<String,Integer> map = RecipeService.findMostUsedIngredients(10,3);
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }

        // INGREDIENTS COUNT


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
        TableViewRecipeRating tableView = new TableViewRecipeRating();
        tableView.initializeTableView();
        tableView.setEventForTableCells();
        tableView.resetObservableArrayList();

        RecipeService.findRecipesWithHighestRating(10,3)
                .forEach(recipe -> tableView.addToObservableArrayList(
                new RowRecipeRating(recipe.getName(), recipe.getAggregatedRating(), new ImageView(recipe.getImages().get(0)))));

        tableView.setItems();
        tableView.setTableDBRating();
        anchorPane.getChildren().add(tableView.getTabellaDB());
    }

    //{$sort: {'color': 1, value: -1}},
    //{$group: {_id: '$color', value: {$first: '$value'}}}
    public void onTopRecipesForEachCategory(ActionEvent actionEvent) {
        TableViewRecipeCategory tableView = new TableViewRecipeCategory();
        tableView.initializeTableView();
        tableView.setEventForTableCells();
        tableView.resetObservableArrayList();

        RecipeService.findTopRecipesForEachCategory(3).forEach(recipe ->
                tableView.addToObservableArrayList(new RowRecipeCategory(recipe.getName(), recipe.getRecipeCategory(),
                        recipe.getAggregatedRating(), new ImageView(recipe.getImages().get(0)))));

        tableView.setItems();
        tableView.setTable();
        anchorPane.getChildren().add(tableView.getTable());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
