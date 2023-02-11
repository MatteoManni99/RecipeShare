package it.unipi.dii.aide.lsmsd.recipeshare.gui;

import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowRecipeCategory;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowRecipeIngredients;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowRecipeRating;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowRecipeTime;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.*;
import it.unipi.dii.aide.lsmsd.recipeshare.model.Recipe;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.*;
import javafx.css.StyleableObjectProperty;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import it.unipi.dii.aide.lsmsd.recipeshare.service.RecipeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class AuthorAnalyticsController implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    private Button nextPage;
    private Button previousPage;
    private Label labelPageNumber;
    private TextField pageNumberField;
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
        tableView.getTable().setLayoutX(324);
        tableView.getTable().setLayoutY(95);
        tableView.getTable().setPrefHeight(450);
        removeOtherTableView();
        tableView.setEventForTableCells();
        tableView.resetObservableArrayList();
    }

    private void addTopRecipesForRangesOfPreparationTime(TableViewAbstract tableView, Integer min, Integer max){
        RecipeService.findTopRecipesForRangesOfPreparationTime(min,max,10,3)
                .forEach(recipe -> tableView.addToObservableArrayList(
                        new RowRecipeTime(recipe.getName(), recipe.getTotalTime(), recipe.getAggregatedRating(),
                                new ImageView(recipe.getImages().get(0)))));
    }

    public void onMostUsedIngredientsClick(ActionEvent actionEvent) {
        removeButton();
        TableViewAbstract tableView = new TableViewRecipeIngredients();
        initializeTableView(tableView);
        tableView.getTable().setPrefWidth(175);
        RecipeService.findMostUsedIngredients(20,3).forEach((s, integer) ->
                tableView.addToObservableArrayList( new RowRecipeIngredients(s, integer)));
        displayTableView(tableView);
    }
    public void onRecipesWithHighestratingClick(ActionEvent actionEvent) {
        removeButton();
        TableViewAbstract tableView = new TableViewRecipeRating();
        initializeTableView(tableView);
        tableView.getTable().setPrefWidth(450);
        RecipeService.findRecipesWithHighestRating(0,10,25)
                .forEach(recipe -> tableView.addToObservableArrayList(
                    new RowRecipeRating(recipe.getName(), recipe.getAggregatedRating(),
                        new ImageView(recipe.getImages().get(0)))));
        displayTableView(tableView);
    }
    private void removeButton(){
        anchorPane.getChildren().remove(nextPage);
        anchorPane.getChildren().remove(previousPage);
        anchorPane.getChildren().remove(labelPageNumber);
        anchorPane.getChildren().remove(pageNumberField);
    }

    public void onTopRecipesForEachCategory(ActionEvent actionEvent) {
        List<Recipe> listRecipe = RecipeService.findTopRecipesForEachCategory(15).stream()
                .filter(recipe -> recipe.getRecipeCategory() != null).sorted(Comparator.comparing(Recipe::getRecipeCategory)).toList();
        int[] pageNumber = {0};

        pageNumberField = new TextField("1");
        pageNumberField.setEditable(false);
        pageNumberField.setLayoutX(452);
        pageNumberField.setLayoutY(63);
        pageNumberField.setPrefSize(53,25);
        pageNumberField.setAlignment(Pos.CENTER);
        labelPageNumber = new Label("Page Number");
        labelPageNumber.setLayoutX(324);
        labelPageNumber.setLayoutY(67);
        labelPageNumber.setPrefSize(82,18);
        nextPage = new Button(">>>");
        previousPage = new Button("<<<");
        nextPage.setLayoutX(511);
        nextPage.setLayoutY(63);
        nextPage.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
            pageNumber[0] += 1;
            printCategoryTable(listRecipe, pageNumber[0]);
            pageNumberField.setText(String.valueOf(pageNumber[0]+1));
        });
        previousPage.setLayoutX(406);
        previousPage.setLayoutY(63);
        previousPage.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
            pageNumber[0] -= pageNumber[0] >0 ? 1 : 0;
            printCategoryTable(listRecipe, pageNumber[0]);
            pageNumberField.setText(String.valueOf(pageNumber[0]+1));
        });
        printCategoryTable(listRecipe, pageNumber[0]);
        anchorPane.getChildren().add(nextPage);
        anchorPane.getChildren().add(previousPage);
        anchorPane.getChildren().add(labelPageNumber);
        anchorPane.getChildren().add(pageNumberField);
    }

    private void printCategoryTable(List<Recipe> listRecipe, Integer page){
        TableViewAbstract tableView = new TableViewRecipeCategory();
        tableView.getTable().setPrefWidth(600);
        initializeTableView(tableView);
        listRecipe.stream().skip(page * 10L).limit(10).toList().forEach(recipe ->
                tableView.addToObservableArrayList(new RowRecipeCategory(recipe.getName(), recipe.getRecipeCategory(),
                        recipe.getAggregatedRating(), new ImageView(recipe.getImages().get(0)))));
        displayTableView(tableView);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DataSingleton.getInstance().setPageBefore("AuthorAnalytics.fxml");
    }
}
