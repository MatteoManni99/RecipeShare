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

public class LoggatoAnalyticsController {
    @FXML
    private AnchorPane anchorPane;

    public void onTopRecipesForRangesOfPreparationTimeClick(ActionEvent actionEvent) {
        TableViewRecipeTime tableView = new TableViewRecipeTime();
        tableView.initializeTableView();
        tableView.setEventForTableCells();
        tableView.resetObservableArrayList();

        addTopRecipesForRangesOfPreparationTime(tableView,0, 30);
        addTopRecipesForRangesOfPreparationTime(tableView,31, 90);
        addTopRecipesForRangesOfPreparationTime(tableView,90, -1);

        tableView.setItems();
        tableView.setTable();
        anchorPane.getChildren().add(tableView.getTable());
    }

    private void addTopRecipesForRangesOfPreparationTime(TableViewRecipeTime tableView, Integer min, Integer max){
        RecipeService.findTopRecipesForRangesOfPreparationTime(min,max,1,3)
                .forEach(recipe -> tableView.addToObservableArrayList(new RowRecipeTime(
                        recipe.getName(), recipe.getTotalTime(), recipe.getAggregatedRating(),
                        new ImageView(recipe.getImages().get(0)))));
    }

    public void onMostUsedIngredientsClick(ActionEvent actionEvent) {
        TableViewRecipeIngredients tableView = new TableViewRecipeIngredients();
        tableView.initializeTableView();
        tableView.resetObservableArrayList();

        RecipeService.findMostUsedIngredients(10,3).forEach((s, integer) ->
                tableView.addToObservableArrayList( new RowRecipeIngredients(s, integer)));


        tableView.setItems();
        tableView.setTable();
        anchorPane.getChildren().add(tableView.getTable());
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

}
