package com.example.demo1.gui;

import com.example.demo1.gui.row.RowRecipeCategory;
import com.example.demo1.gui.row.RowRecipeIngredients;
import com.example.demo1.gui.row.RowRecipeRating;
import com.example.demo1.gui.row.RowRecipeTime;
import com.example.demo1.gui.tableview.*;
import com.example.demo1.model.Recipe;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import com.example.demo1.service.RecipeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class AuthorAnalyticsController {
    @FXML
    private AnchorPane anchorPane;
    private Button nextPage;
    private Button previousPage;
    @FXML
    public void onBackClick(ActionEvent actionEvent) throws IOException {
        Utils.changeScene(actionEvent,"HomeAuthor.fxml");
    }

    private void removeOtherTableView(){anchorPane.getChildren().removeIf(node -> node instanceof TableView);}

    public void onTopRecipesForRangesOfPreparationTimeClick(ActionEvent actionEvent) {
        removeButton();
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
        removeButton();
        TableViewAbstract tableView = new TableViewRecipeIngredients();
        initializeTableView(tableView);
        RecipeService.findMostUsedIngredients(10,3).forEach((s, integer) ->
                tableView.addToObservableArrayList( new RowRecipeIngredients(s, integer)));
        displayTableView(tableView);
    }

    public void onRecipesWithHighestratingClick(ActionEvent actionEvent) {
        removeButton();
        TableViewAbstract tableView = new TableViewRecipeRating();
        initializeTableView(tableView);
        RecipeService.findRecipesWithHighestRating(10,3)
                .forEach(recipe -> tableView.addToObservableArrayList(
                    new RowRecipeRating(recipe.getName(), recipe.getAggregatedRating(),
                        new ImageView(recipe.getImages().get(0)))));
        displayTableView(tableView);
    }
    private void removeButton(){
        anchorPane.getChildren().remove(nextPage);
        anchorPane.getChildren().remove(previousPage);
    }

    public void onTopRecipesForEachCategory(ActionEvent actionEvent) {
        List<Recipe> listRecipe = RecipeService.findTopRecipesForEachCategory(3).stream()
                .filter(recipe -> recipe.getRecipeCategory() != null).sorted(Comparator.comparing(Recipe::getRecipeCategory)).toList();
        int[] pageNumber = {0};
        nextPage = new Button("Next Page");
        previousPage = new Button("Previous Page");
        nextPage.setLayoutX(420);
        nextPage.setLayoutY(200);
        nextPage.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
            pageNumber[0] += 1;
            printCategoryTable(listRecipe, pageNumber[0]);
        });
        previousPage.setLayoutX(280);
        previousPage.setLayoutY(200);
        previousPage.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
            pageNumber[0] -= pageNumber[0] >0 ? 1 : 0;
            printCategoryTable(listRecipe, pageNumber[0]);
        });
        printCategoryTable(listRecipe, pageNumber[0]);
        anchorPane.getChildren().add(nextPage);
        anchorPane.getChildren().add(previousPage);
    }

    private void printCategoryTable(List<Recipe> listRecipe, Integer page){
        TableViewAbstract tableView = new TableViewRecipeCategory();
        initializeTableView(tableView);
        listRecipe.stream().skip(page * 10L).limit(10).toList().forEach(recipe ->
                tableView.addToObservableArrayList(new RowRecipeCategory(recipe.getName(), recipe.getRecipeCategory(),
                        recipe.getAggregatedRating(), new ImageView(recipe.getImages().get(0)))));
        displayTableView(tableView);
    }

}
