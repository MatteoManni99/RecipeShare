package com.example.demo1.gui;

import javafx.scene.control.TableView;
import com.example.demo1.service.RecipeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class LoggatoAnalyticsController {
    @FXML
    private AnchorPane anchorPane;

    private void removeOtherTableView(){anchorPane.getChildren().removeIf(node -> node instanceof TableView);}

    public void onTopRecipesForRangesOfPreparationTimeClick(ActionEvent actionEvent) {
        TableViewAbstract tableView = new TableViewRecipeTime();
        initializeTableView(tableView);
        addTopRecipesForRangesOfPreparationTime(tableView,0, 30);
        addTopRecipesForRangesOfPreparationTime(tableView,31, 90);
        addTopRecipesForRangesOfPreparationTime(tableView,90, -1);
        displayTableView(tableView);
    }

    private void displayTableView(TableViewAbstract tableView) {
        tableView.setItems();
        tableView.setTable();
        anchorPane.getChildren().add(tableView.getTable());
    }

    private void initializeTableView(TableViewAbstract tableView) {
        removeOtherTableView();
        tableView.setEventForTableCells();
        tableView.resetObservableArrayList();
    }

    private void addTopRecipesForRangesOfPreparationTime(TableViewAbstract tableView, Integer min, Integer max){
        RecipeService.findTopRecipesForRangesOfPreparationTime(min,max,1,3)
                .forEach(recipe -> tableView.addToObservableArrayList(
                        new RowRecipeTime(recipe.getName(), recipe.getTotalTime(), recipe.getAggregatedRating(),
                                new ImageView(recipe.getImages().get(0)))));
    }

    public void onMostUsedIngredientsClick(ActionEvent actionEvent) {
        TableViewAbstract tableView = new TableViewRecipeIngredients();
        initializeTableView(tableView);
        RecipeService.findMostUsedIngredients(10,3).forEach((s, integer) ->
                tableView.addToObservableArrayList( new RowRecipeIngredients(s, integer)));
        displayTableView(tableView);
    }

    public void onRecipesWithHighestratingClick(ActionEvent actionEvent) {
        TableViewRecipeRating tableView = new TableViewRecipeRating();
        initializeTableView(tableView);
        RecipeService.findRecipesWithHighestRating(10,3)
                .forEach(recipe -> tableView.addToObservableArrayList(
                new RowRecipeRating(recipe.getName(), recipe.getAggregatedRating(), new ImageView(recipe.getImages().get(0)))));
        displayTableView(tableView);
    }

    //{$sort: {'color': 1, value: -1}},
    //{$group: {_id: '$color', value: {$first: '$value'}}}
    public void onTopRecipesForEachCategory(ActionEvent actionEvent) {
        TableViewRecipeCategory tableView = new TableViewRecipeCategory();
        initializeTableView(tableView);
        RecipeService.findTopRecipesForEachCategory(3).forEach(recipe ->
                tableView.addToObservableArrayList(new RowRecipeCategory(recipe.getName(), recipe.getRecipeCategory(),
                        recipe.getAggregatedRating(), new ImageView(recipe.getImages().get(0)))));
        displayTableView(tableView);
    }

}
