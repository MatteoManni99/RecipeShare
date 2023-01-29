package com.example.demo1.gui;

import com.example.demo1.model.Recipe;
import com.example.demo1.model.ReportedRecipe;
import com.example.demo1.service.RecipeService;
import com.example.demo1.service.ReportedRecipeService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RecipeController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    public Label name;
    public Label authorName;
    public Label calories;
    public Label servings;
    public Label time;
    public Label date;
    public ChoiceBox<Integer> ratingChoiceBox;
    public Button reportRecipeButton;
    public TextArea description;
    public TextArea reviewTextArea;
    public ImageView image;
    public ListView<String> ingredients;
    public ListView<String> keywords;
    public ListView<String> instructions;

    private final DataSingleton data = DataSingleton.getInstance();
    private Integer indexImages=0;
    private List<String> images_list;
    private String recipeName;
    private Recipe recipe;

    private TableViewReview tableViewReview = new TableViewReview();


    private ArrayList<String> reviewers = new ArrayList<>();

    private void printImages(){
        image.setImage(new Image(images_list.get(indexImages)));
    }
    @FXML
    public void onPreviousClick(ActionEvent actionEvent) throws IOException {
        indexImages -= indexImages>0 ? 1 : 0;
        printImages();
    }

    @FXML
    public void onNextClick(ActionEvent actionEvent) throws IOException {
        indexImages += indexImages < images_list.size()-1 ? 1 : 0;
        printImages();
    }

    @FXML
    public void onBackClick(ActionEvent actionEvent) throws IOException {
        Utils.changeScene(actionEvent,"loggato.fxml");
    }

    @FXML
    public void onReportRecipeClick(ActionEvent actionEvent){
        ReportedRecipeService.addReportedRecipe(new ReportedRecipe(recipe.getName(),recipe.getAuthorName(),
                data.getAuthorName(),LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),recipe.getImages().get(0)));
    }
    public void onLeaveAReviewClick(ActionEvent actionEvent){
        String reviewer = data.getAuthorName();
        if(reviewers.contains(reviewer)){
            reviewTextArea.setText("Avevi già recensito questa ricetta");
        } else if (reviewer.equals(authorName.getText())) {
            reviewTextArea.setText("Questa ricetta è tua non puoi recensirla");
        } else if (ratingChoiceBox.getValue() == null){
            reviewTextArea.setText("Select a rating");
        }else{
            RecipeService.addReview(recipeName, reviewer, ratingChoiceBox.getValue(), reviewTextArea.getText());
            Utils.changeScene(actionEvent, "Recipe.fxml");
        }
    }

    //this method is called when the recipe's author correspond to the recipe visualizer
    public void addDeleteButtonAndRemoveReportButton(){
        anchorPane.getChildren().remove(reportRecipeButton);
        Button deleteRecipe = new Button();
        deleteRecipe.setText("Delete this Recipe");
        deleteRecipe.setLayoutX(750);
        deleteRecipe.setLayoutY(34);
        EventHandler<ActionEvent> eventHandler = actionEvent -> {
            RecipeService.deleteRecipe(recipe);
            Utils.changeScene(actionEvent,"Loggato.fxml");
        };
        deleteRecipe.setOnAction(eventHandler);
        anchorPane.getChildren().add(deleteRecipe);
    }
    private ObservableList<String> safeCastToObservableList(List<String> list){
        try {return FXCollections.observableArrayList(list);}
        catch (NullPointerException e){return null;}
    }

    private void setLablesAndLists(Recipe recipe){
        name.setText(recipe.getName());
        authorName.setText(recipe.getAuthorName());
        description.setText(recipe.getDescription());
        calories.setText(String.valueOf(recipe.getCalories()));
        servings.setText(String.valueOf(recipe.getRecipeServings()));
        time.setText(String.valueOf(recipe.getTotalTime()));
        date.setText(recipe.getDatePublished());
        keywords.setItems(safeCastToObservableList(recipe.getKeywords()));
        ingredients.setItems(safeCastToObservableList(recipe.getRecipeIngredientParts()));
        instructions.setItems(safeCastToObservableList(recipe.getRecipeInstructions()));
        images_list = recipe.getImages();
        printImages();

        recipe.getReviews().forEach(review -> {
            String reviewer = review.getAuthorName();
            reviewers.add(reviewer);
            RowReview reviewT = new RowReview(reviewer, review.getRating(), review.getReview());
            tableViewReview.addToObservableArrayList(reviewT);
        });
        tableViewReview.setItems();
    }
    public void initializeTableViewReview(){
        tableViewReview.initializeTableView();
        tableViewReview.resetObservableArrayList();
        tableViewReview.setTableDB();
        anchorPane.getChildren().add(tableViewReview.getTableDB());
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ratingChoiceBox.setItems(FXCollections.observableArrayList(1,2,3,4,5));
        recipeName = data.getRecipeName();
        initializeTableViewReview();
        recipe = RecipeService.getRecipeByName(data.getRecipeName());
        setLablesAndLists(recipe);

        if(data.getAuthorName().equals(recipe.getAuthorName()))
            addDeleteButtonAndRemoveReportButton();
    }
}
