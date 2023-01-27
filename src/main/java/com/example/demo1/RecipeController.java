package com.example.demo1;

import com.example.demo1.dao.mongo.RecipeMongoDAO;
import com.example.demo1.model.Recipe;
import com.example.demo1.model.ReportedRecipe;
import com.example.demo1.model.Review;
import com.example.demo1.service.RecipeService;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.mongodb.client.model.Aggregates.*;

public class RecipeController implements Initializable {

    @FXML
    public Label name;
    public Label authorName;
    public Label calories;
    public Label servings;
    public Label time;
    public Label date;
    public ChoiceBox<Integer> ratingChoiceBox;
    public Button reportRecipeButton;

    @FXML
    public TextArea description;
    public TextArea reviewTextArea;
    @FXML
    public ImageView image;
    @FXML
    public ListView<String> ingredients;
    @FXML
    public ListView<String> keywords;
    @FXML
    public ListView<String> instructions;
    private DataSingleton data = DataSingleton.getInstance();
    private Integer indexImages=0;
    private List<String> images_list;
    private Stage stage;
    private String recipeName;

    private TableViewReview tableViewReview = new TableViewReview();
    @FXML
    private AnchorPane anchorPane;

    private ArrayList<String> reviewers = new ArrayList<String>();


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
        indexImages += indexImages<images_list.size()-1 ? 1 : 0;
        printImages();
    }

    @FXML
    public void onBackClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loggato.fxml"));
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onReportRecipeClick(ActionEvent actionEvent){
        LocalDate currentDate = LocalDate.now();
        String isoDate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        Document recipeToReport = new Document();
        recipeToReport.append("name",data.getRecipeName());
        recipeToReport.append("authorName",authorName.getText());
        recipeToReport.append("reporterName",data.getAuthorName());
        recipeToReport.append("dateReporting", isoDate);
        recipeToReport.append("Image",images_list.get(0));
        addRecipeToReportedRecipe(recipeToReport);
    }
    private void addRecipeToReportedRecipe(Document recipeToAdd){ //fatto in DAO
        ReportedRecipe reportedRecipe = new ReportedRecipe(recipeToAdd.getString("name"),recipeToAdd.getString("authorName"),
                recipeToAdd.getString("reporterName"),recipeToAdd.getString("dateReporting"),recipeToAdd.getString("image"));
        String uri = Configuration.MONGODB_URL;
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_REPORTED_RECIPE);
            Bson filter = Filters.and(
                    Filters.eq("name", recipeToAdd.getString("name")),
                    Filters.eq("reporterName", recipeToAdd.getString("reporterName")));
            if (collection.find(filter).cursor().hasNext()) {
                System.out.println("Avevi già Reportato questa Recipe");
            }else{
                if(collection.insertOne(recipeToAdd).wasAcknowledged()){
                    System.out.println("Recipe Reportata");
                }
            }
        }
    }
    public void onLeaveAReviewClick(ActionEvent actionEvent){
        String reviewer = data.getAuthorName();
        if(reviewers.contains(reviewer)){
            reviewTextArea.setText("Avevi già recensito questa ricetta");
            reviewTextArea.setStyle("-fx-text-fill: #dc143c");
        } else if (reviewer.equals(authorName.getText())) {
            reviewTextArea.setText("Questa ricetta è tua non puoi recensirla");
            reviewTextArea.setStyle("-fx-text-fill: #dc143c");
        } else {
            Integer rating = ratingChoiceBox.getValue();
            String review = reviewTextArea.getText();
            String uri = Configuration.MONGODB_URL;
            try (MongoClient mongoClient = MongoClients.create(uri)) { //fatto in RECIPEDAO
                MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
                MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_RECIPE);
                Bson match = new Document("Name",recipeName);
                Bson updates = new Document("$push",new Document("Reviews",new Document("AuthorName",reviewer)
                        .append("Rating",rating).append("Review",review)));
                collection.updateOne(match,updates);
                //cambio pagina con la stessa pagina per fare il refresh delle review
                changeScene(actionEvent,"Recipe.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private void changeScene(ActionEvent actionEvent, String sceneFXML) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(sceneFXML));
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setScene(scene);
        stage.show();
    }

    public void addDeleteButtonAndRemoveReportButton(){
        anchorPane.getChildren().remove(reportRecipeButton);

        Button deleteRecipe = new Button();
        deleteRecipe.setText("Delete this Recipe");
        deleteRecipe.setLayoutX(750);
        deleteRecipe.setLayoutY(34);
        EventHandler<ActionEvent> eventHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) { //fatto in RECIPEDAO
                try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
                    MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
                    MongoCollection<Document> collectionRecipe = database.getCollection(Configuration.MONGODB_RECIPE);
                    Bson match = new Document("Name", recipeName);
                    collectionRecipe.deleteOne(match);
                    changeScene(actionEvent,"Loggato.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        deleteRecipe.setOnAction(eventHandler);
        anchorPane.getChildren().add(deleteRecipe);
    }

    private void setLablesAndLists(Recipe recipe){
        name.setText(recipe.getName());
        authorName.setText(recipe.getAuthorName());
        description.setText(recipe.getDescription());
        calories.setText(String.valueOf(recipe.getCalories()));
        servings.setText(String.valueOf(recipe.getRecipeServings()));
        time.setText(String.valueOf(recipe.getTotalTime()));
        date.setText(recipe.getDatePublished());
        try {
            ObservableList<String> ingredients_list = FXCollections.observableArrayList(recipe.getRecipeIngredientParts());
            ingredients.setItems(ingredients_list);
        }catch (NullPointerException e){
            ingredients.setItems(null);
        }
        try {
            ObservableList<String> keywords_list = FXCollections.observableArrayList(recipe.getKeywords());
            keywords.setItems(keywords_list);
        }catch (NullPointerException e){
            keywords.setItems(null);
        }
        try {
            ObservableList<String> instructions_list = FXCollections.observableArrayList(recipe.getRecipeInstructions());
            instructions.setItems(instructions_list);
        }catch (NullPointerException e){
            keywords.setItems(null);
        }
        images_list = recipe.getImages();
        printImages();

        tableViewReview.initializeTableView();
        tableViewReview.resetObservableArrayList();
        List<Review> reviews_list = recipe.getReviews();
        for (Review review : reviews_list) {
            String reviewer = review.getAuthorName();
            reviewers.add(reviewer);
            ReviewTableView reviewT = new ReviewTableView(reviewer, review.getRating(), review.getReview());
            tableViewReview.addToObservableArrayList(reviewT);
        }
        tableViewReview.setItems();
        tableViewReview.setTableDB();
        anchorPane.getChildren().add(tableViewReview.getTableDB());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ratingChoiceBox.setItems(FXCollections.observableArrayList(1,2,3,4,5));
        recipeName = data.getRecipeName(); //!!!!! questa funziona se prima del cambio di scena modifico DataSingleton !!!!!!

        Recipe recipe = RecipeService.getRecipeByName(data.getRecipeName());
        setLablesAndLists(recipe);

        if(data.getAuthorName().equals(authorName.getText())){
            addDeleteButtonAndRemoveReportButton();
        }
    }
}
