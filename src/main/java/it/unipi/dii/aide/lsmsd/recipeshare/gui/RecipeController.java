package it.unipi.dii.aide.lsmsd.recipeshare.gui;

import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowReview;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewAbstract;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewReview;
import it.unipi.dii.aide.lsmsd.recipeshare.model.Recipe;
import it.unipi.dii.aide.lsmsd.recipeshare.model.ReportedRecipe;
import it.unipi.dii.aide.lsmsd.recipeshare.service.RecipeService;
import it.unipi.dii.aide.lsmsd.recipeshare.service.ReportedRecipeService;
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
    @FXML
    private TextField name;
    @FXML
    private TextField authorName;
    @FXML
    private TextField calories;
    @FXML
    private TextField servings;
    @FXML
    private TextField time;
    @FXML
    private TextField date;
    @FXML
    private TextField aggregatedRating;
    @FXML
    private ChoiceBox<Integer> ratingChoiceBox;
    @FXML
    private Button reportRecipeButton;
    @FXML
    private TextArea description;
    @FXML
    private TextArea reviewTextArea;
    @FXML
    private Button leaveReviewButton;
    @FXML
    private ImageView image;
    @FXML
    private Label ratingLabel;
    @FXML
    private ListView<String> ingredients;
    @FXML
    private ListView<String> keywords;
    @FXML
    private ListView<String> instructions;

    private final DataSingleton data = DataSingleton.getInstance();
    private Integer indexImages=0;
    private List<String> images_list;
    private String recipeName;
    private Recipe recipe;
    private final TableViewAbstract tableViewReview = new TableViewReview();

    private final ArrayList<String> reviewers = new ArrayList<>();

    private void printImages(){
        image.setImage(new Image(images_list.get(indexImages)));
    }
    @FXML
    private void onPreviousClick(ActionEvent actionEvent){
        indexImages -= indexImages>0 ? 1 : 0;
        printImages();
    }

    @FXML
    private void onNextClick(ActionEvent actionEvent){
        indexImages += indexImages < images_list.size()-1 ? 1 : 0;
        printImages();
    }

    @FXML
    private void onBackClick(ActionEvent actionEvent) throws IOException {
        if(data.getTypeOfUser().equals("moderator")){
            Utils.changeScene(actionEvent,"HomeModerator.fxml");
        }else Utils.changeScene(actionEvent,"HomeAuthor.fxml");
    }

    @FXML
    private void onReportRecipeClick(ActionEvent actionEvent){
        ReportedRecipe reportedRecipe = new ReportedRecipe(recipe.getName(),recipe.getAuthorName(),
                data.getAuthorName(),LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),recipe.getImages().get(0));

        if(!ReportedRecipeService.checkIfRecipeAlreadyReported(reportedRecipe)){
            if(ReportedRecipeService.addReportedRecipe(reportedRecipe)){
                System.out.println("Recipe correctly reported");
            }else{
                System.out.println("Error: Recipe not reported");
            }
        }else System.out.println("You have already reported this recipe");
    }
    private void onLeaveAReviewClick(ActionEvent actionEvent){
        String reviewer = data.getAuthorName();
        if(reviewers.contains(reviewer)){
            reviewTextArea.setText("Avevi già recensito questa ricetta");
        } else if (reviewer.equals(authorName.getText())) {
            reviewTextArea.setText("Questa ricetta è tua non puoi recensirla");
        } else if (ratingChoiceBox.getValue() == null){
            reviewTextArea.setText("Select a rating");
        }else{
            if(RecipeService.addReview(recipeName, reviewer, ratingChoiceBox.getValue(), reviewTextArea.getText())){
                Utils.changeScene(actionEvent, "Recipe.fxml");
            }else System.out.println("Error: Review not added");
        }
    }

    //this method is called when the recipe's author correspond to the recipe visualizer
    private void addDeleteButton(){
        Button deleteRecipe = new Button();
        deleteRecipe.setText("Delete this Recipe");
        deleteRecipe.setLayoutX(750);
        deleteRecipe.setLayoutY(26);
        EventHandler<ActionEvent> eventHandler = actionEvent -> {
            if(RecipeService.deleteRecipe(recipe)){
                Utils.changeScene(actionEvent,"HomeAuthor.fxml");
            }else System.out.println("Errore nella cancellazione della recipe");
        };
        deleteRecipe.setOnAction(eventHandler);
        anchorPane.getChildren().add(deleteRecipe);
    }
    private void addApprovalButtons(){
        //aggiunta tasto per approvare
        Button approveRecipe = new Button();
        approveRecipe.setText("Approve Recipe");
        approveRecipe.setLayoutX(750);
        approveRecipe.setLayoutY(26);
        EventHandler<ActionEvent> eventHandlerApprove = actionEvent -> {
            if(ReportedRecipeService.approveReportedRecipe(recipe))
                Utils.changeScene(actionEvent,"HomeModerator.fxml");;
        };
        approveRecipe.setOnAction(eventHandlerApprove);
        anchorPane.getChildren().add(approveRecipe);

        //aggiunta tasto per NON approvare
        Button notApproveRecipe = new Button();
        notApproveRecipe.setText("Not Approve and Delete Recipe");
        notApproveRecipe.setLayoutX(750);
        notApproveRecipe.setLayoutY(60);
        EventHandler<ActionEvent> eventHandlerNotApprove = actionEvent -> {
            if(ReportedRecipeService.notApproveReportedRecipe(recipe)){
                Utils.changeScene(actionEvent,"HomeModerator.fxml");
            }else System.out.println("Error: Not Approve Recipe didn't work");
        };
        notApproveRecipe.setOnAction(eventHandlerNotApprove);
        anchorPane.getChildren().add(notApproveRecipe);
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
        aggregatedRating.setText(String.valueOf(recipe.getAggregatedRating()));
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
    private void initializeTableViewReview(){
        tableViewReview.resetObservableArrayList();
        tableViewReview.setEventForTableCells();
        tableViewReview.setTable();
        tableViewReview.getTable().setLayoutY(480);
        tableViewReview.getTable().setLayoutX(295);
        tableViewReview.getTable().setPrefWidth(679);
        anchorPane.getChildren().add(tableViewReview.getTable());
    }
    private void removeReviewInterface(){
        anchorPane.getChildren().remove(ratingChoiceBox);
        anchorPane.getChildren().remove(ratingLabel);
        anchorPane.getChildren().remove(reviewTextArea);
        anchorPane.getChildren().remove(leaveReviewButton);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ratingChoiceBox.setItems(FXCollections.observableArrayList(1,2,3,4,5));
        recipeName = data.getRecipeName();
        initializeTableViewReview();
        recipe = RecipeService.getRecipeByName(data.getRecipeName());
        setLablesAndLists(recipe);

        if(data.getAuthorName().equals(recipe.getAuthorName())){
            anchorPane.getChildren().remove(reportRecipeButton);
            addDeleteButton();
        }
        if(data.getTypeOfUser().equals("moderator")){
            anchorPane.getChildren().remove(reportRecipeButton);
            removeReviewInterface();
            addApprovalButtons();
        }
    }
}
