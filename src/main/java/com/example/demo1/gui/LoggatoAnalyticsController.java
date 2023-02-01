package com.example.demo1.gui;

import com.example.demo1.model.Recipe;
import com.example.demo1.service.AuthorService;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import com.example.demo1.service.RecipeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class LoggatoAnalyticsController {
    @FXML
    private AnchorPane anchorPane;

    @FXML
    public void onBackClick(ActionEvent actionEvent) throws IOException {
        Utils.changeScene(actionEvent,"Loggato.fxml");
    }

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
        TableViewAbstract tableView = new TableViewRecipeRating();
        initializeTableView(tableView);
        RecipeService.findRecipesWithHighestRating(10,3)
                .forEach(recipe -> tableView.addToObservableArrayList(
                    new RowRecipeRating(recipe.getName(), recipe.getAggregatedRating(),
                        new ImageView(recipe.getImages().get(0)))));
        displayTableView(tableView);
    }

    public void onTopRecipesForEachCategory(ActionEvent actionEvent) {
        List<Recipe> listRecipe = RecipeService.findTopRecipesForEachCategory(3);
        int[] page = {10};


        listRecipe.forEach(recipe -> System.out.println(recipe.getRecipeCategory()));

        /*RecipeService.findTopRecipesForEachCategory(3).forEach(recipe ->
                tableView.addToObservableArrayList(new RowRecipeCategory(recipe.getName(), recipe.getRecipeCategory(),
                        recipe.getAggregatedRating(), new ImageView(recipe.getImages().get(0)))));*/


        Button nextPage = new Button("Next Page");
        Button previousPage = new Button("Previous Page");

        nextPage.setLayoutX(420);
        nextPage.setLayoutY(200);
        nextPage.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
            TableViewAbstract tableViewN = new TableViewRecipeCategory();
            initializeTableView(tableViewN);
            listRecipe.subList(page[0], page[0] +10).forEach(recipe -> tableViewN.addToObservableArrayList(new RowRecipeCategory(recipe.getName(), recipe.getRecipeCategory(),
                    recipe.getAggregatedRating(), new ImageView(recipe.getImages().get(0)))));
            displayTableView(tableViewN);
            page[0] +=10;
        });

        previousPage.setLayoutX(280);
        previousPage.setLayoutY(200);
        previousPage.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
            if(page[0]>=10){
                TableViewAbstract tableViewP = new TableViewRecipeCategory();
                initializeTableView(tableViewP);
                listRecipe.subList(page[0]-10, page[0]).forEach(recipe -> tableViewP.addToObservableArrayList(new RowRecipeCategory(recipe.getName(), recipe.getRecipeCategory(),
                        recipe.getAggregatedRating(), new ImageView(recipe.getImages().get(0)))));
                displayTableView(tableViewP);
                page[0] -=10;
            }
        });

        TableViewAbstract tableView = new TableViewRecipeCategory();
        initializeTableView(tableView);
        listRecipe.subList(0, 10).forEach(recipe -> tableView.addToObservableArrayList(new RowRecipeCategory(recipe.getName(), recipe.getRecipeCategory(),
                recipe.getAggregatedRating(), new ImageView(recipe.getImages().get(0)))));
        displayTableView(tableView);

        anchorPane.getChildren().add(nextPage);
        anchorPane.getChildren().add(previousPage);

    }

}
