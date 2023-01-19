package com.example.demo1;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class AddRecipeController implements Initializable {
    DataSingleton data = DataSingleton.getInstance();

    private Stage stage;

    public TextField nameField;
    public TextField categoryField;
    public TextField servingsField;
    public TextField timeField;
    public TextField caloriesField;
    public TextArea descriptionField;
    public TextField keywordField;
    public ListView keywordListView;
    private ArrayList<String> keywordArrayList = new ArrayList<String>();
    public TextField ingredientField;
    public ListView ingredientListView;
    private ArrayList<String> ingredientArrayList = new ArrayList<String>();
    public TextField imageField;
    public ListView imageListView;
    private ArrayList<String> imageArrayList = new ArrayList<String>();
    public TextField instructionsField;
    public ListView instructionsListView;
    private ArrayList<String> instructionsArrayList = new ArrayList<String>();

    ObservableList<String> observableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void onAddRecipeClick(ActionEvent actionEvent) {
        String name = nameField.getText();
        Integer time;
        Double calories;
        Integer servings;
        try {time = Integer.valueOf(timeField.getText());} catch (NumberFormatException e) {time = null;}
        try {calories = Double.valueOf(caloriesField.getText());} catch (NumberFormatException e) {calories = null;}
        try {servings = Integer.valueOf(servingsField.getText());} catch (NumberFormatException e) {servings = null;}
        LocalDate currentDate = LocalDate.now();
        String isoDate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

        if(name.isBlank() || name.isEmpty()){
            System.out.println("Insert a Name in the corresponding TextField");
        }else if(checkIfNameIsAvailable(name)) {
            System.out.println("Name is available");
            data = DataSingleton.getInstance();

            Document recipeToAdd = new Document();
            recipeToAdd.append("RecipeId",null); ///FORSE SAREBBE DA TOGLIERE///
            recipeToAdd.append("Name",name);
            recipeToAdd.append("AuthorId",null); ///FORSE SAREBBE DA TOGLIERE///
            recipeToAdd.append("AuthorName",data.getAuthorName()); //da cambiare
            recipeToAdd.append("TotalTime",time);
            recipeToAdd.append("DatePublished", isoDate);
            recipeToAdd.append("Description",setNullIfEmpty(descriptionField.getText()));
            recipeToAdd.append("Images",imageArrayList);
            recipeToAdd.append("RecipeCategory",setNullIfEmpty(categoryField.getText()));
            recipeToAdd.append("Keywords",keywordArrayList);
            recipeToAdd.append("RecipeIngredientParts",ingredientArrayList);
            recipeToAdd.append("AggregatedRating",null); ///FORSE SAREBBE DA TOGLIERE///
            recipeToAdd.append("Calories",calories);
            recipeToAdd.append("RecipeServings",servings);
            recipeToAdd.append("RecipeInstructions",instructionsArrayList);
            recipeToAdd.append("Reviews",new ArrayList<String>());
            addToDB(recipeToAdd);
        }else{
            System.out.println("Name isn't available");
        }
    }
    private boolean checkIfNameIsAvailable(String name){
        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_RECIPE);
            Bson filter = new Document("Name",name);
            if(collection.find(filter).cursor().hasNext()){
                return false;
            }else{
                return true;
            }
        }
    }
    private void addToDB(Document recipeToAdd){
        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_RECIPE);
            if(collection.insertOne(recipeToAdd).wasAcknowledged()){
                System.out.println("Recipe successfully added");
                clearFields();
            }else System.out.println("Recipe was not added for some reason...");
        }
    }
    private String setNullIfEmpty(String string){
        if(string.isEmpty() || string.isBlank()) return null;
        else return string;
    }

    public void onBackClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Loggato.fxml"));
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    private void clearFields() {
        nameField.clear();
        categoryField.clear();
        servingsField.clear();
        timeField.clear();
        caloriesField.clear();
        descriptionField.clear();

        keywordField.clear();
        keywordArrayList.clear();
        keywordListView.setItems(observableList = FXCollections.observableList(keywordArrayList));

        ingredientField.clear();
        ingredientArrayList.clear();
        ingredientListView.setItems(observableList = FXCollections.observableList(ingredientArrayList));

        imageField.clear();
        imageArrayList.clear();
        imageListView.setItems(observableList = FXCollections.observableList(imageArrayList));

        instructionsField.clear();
        instructionsArrayList.clear();
        instructionsListView.setItems(observableList = FXCollections.observableList(instructionsArrayList));

    }

    public void onAddKeywordClick(ActionEvent actionEvent) {
        onAdd(keywordField,keywordArrayList,keywordListView);
    }
    public void onRemoveKeywordClick(ActionEvent actionEvent) {
        onRemove(keywordField,keywordArrayList,keywordListView);
    }
    public void onAddIngredientClick(ActionEvent actionEvent) {
        onAdd(ingredientField,ingredientArrayList,ingredientListView);
    }
    public void onRemoveIngredientClick(ActionEvent actionEvent) {
        onRemove(ingredientField,ingredientArrayList,ingredientListView);
    }
    public void onAddImageClick(ActionEvent actionEvent) {
        onAdd(imageField,imageArrayList,imageListView);
    }
    public void onRemoveImageClick(ActionEvent actionEvent) {
       onRemove(imageField,imageArrayList,imageListView);
    }
    public void onAddInstructionClick(ActionEvent actionEvent) {
        onAdd(instructionsField,instructionsArrayList,instructionsListView);
    }
    public void onRemoveInstructionClick(ActionEvent actionEvent) {
        onRemove(instructionsField,instructionsArrayList,ingredientListView);
    }
    private void onRemove(TextField textField, ArrayList<String> arrayList, ListView<String> listView){
        String field = textField.getText();
        if(!field.isBlank() && !field.isEmpty()) {
            arrayList.remove(field);
            observableList = FXCollections.observableList(arrayList);
            listView.setItems(observableList);
        }
    }
    private void onAdd(TextField textField, ArrayList<String> arrayList, ListView<String> listView){
        String field = textField.getText();
        if(!field.isBlank() && !field.isEmpty()) {
            arrayList.add(field);
            observableList = FXCollections.observableList(arrayList);
            listView.setItems(observableList);
        }
    }
}
