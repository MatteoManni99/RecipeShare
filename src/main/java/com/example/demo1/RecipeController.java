package com.example.demo1;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static com.mongodb.client.model.Aggregates.*;

public class RecipeController implements Initializable {

    @FXML
    public Label name;
    public Label authorName;
    public Label calories;
    public Label servings;
    public Label time;
    public Label date;

    @FXML
    public TextArea description;
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
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
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
    private void addRecipeToReportedRecipe(Document recipeToAdd){
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        recipeName = data.getRecipeName(); //!!!!! questa funziona se prima del cambio di scena modifico DataSingleton !!!!!!
        String uri = Configuration.MONGODB_URL;
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collectionRecipe = database.getCollection(Configuration.MONGODB_RECIPE);
            Bson match = match(Filters.eq("Name", recipeName));

            tableViewReview.initializeTableView();

            for (Document doc : collectionRecipe.aggregate(Arrays.asList(match))) {
                name.setText(doc.getString("Name"));
                authorName.setText(doc.getString("AuthorName"));
                description.setText(doc.getString("Description"));
                calories.setText(String.valueOf(doc.get("Calories")));
                servings.setText(String.valueOf(doc.get("RecipeServings")));
                time.setText(String.valueOf(doc.get("TotalTime")));
                date.setText(doc.getString("DatePublished"));
                try {
                    ObservableList<String> ingredients_list = FXCollections.observableArrayList(doc.getList("RecipeIngredientParts", String.class));
                    ingredients.setItems(ingredients_list);
                }catch (NullPointerException e){
                    ingredients.setItems(null);
                }
                try {
                    ObservableList<String> keywords_list = FXCollections.observableArrayList(doc.getList("Keywords", String.class));
                    keywords.setItems(keywords_list);
                }catch (NullPointerException e){
                    keywords.setItems(null);
                }
                try {
                    ObservableList<String> instructions_list = FXCollections.observableArrayList(doc.getList("RecipeInstructions", String.class));
                    instructions.setItems(instructions_list);
                }catch (NullPointerException e){
                    keywords.setItems(null);
                }
                images_list = doc.getList("Images", String.class);
                printImages();

                tableViewReview.resetObservableArrayList();
                List<Document> reviews_list = doc.getList("Reviews", Document.class);
                for (Document reviewDoc : reviews_list) {
                    Review review = new Review(reviewDoc.getString("AuthorName"), reviewDoc.getInteger("Rating"), reviewDoc.getString("Review"));
                    tableViewReview.addToObservableArrayList(review);
                }
                tableViewReview.setItems();
            }
        }
        tableViewReview.setTableDB();
        anchorPane.getChildren().add(tableViewReview.getTableDB());
    }


}
