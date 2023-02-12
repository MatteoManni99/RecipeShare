package it.unipi.dii.aide.lsmsd.recipeshare.gui;

import it.unipi.dii.aide.lsmsd.recipeshare.model.Recipe;
import it.unipi.dii.aide.lsmsd.recipeshare.service.RecipeService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddRecipeController implements Initializable {
    public Label warningLabel;
    DataSingleton data = DataSingleton.getInstance();
    public TextField nameField;
    public TextField categoryField;
    public TextField servingsField;
    public TextField timeField;
    public TextField caloriesField;
    public TextArea descriptionField;
    public TextField keywordField;
    public ListView<String> keywordListView;
    private final List<String> keywordArrayList = new ArrayList<>();
    public TextField ingredientField;
    public ListView<String> ingredientListView;
    private final List<String> ingredientArrayList = new ArrayList<>();
    public TextField imageField;
    public ListView<String> imageListView;
    private final List<String> imageArrayList = new ArrayList<>();
    public TextField instructionsField;
    public ListView<String> instructionsListView;
    private final List<String> instructionsArrayList = new ArrayList<>();
    ObservableList<String> observableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void onAddRecipeClick(ActionEvent actionEvent) {
        String name = nameField.getText();
        Integer time;
        Double calories;
        Double servings;
        try {time = Integer.valueOf(timeField.getText());} catch (NumberFormatException e) {time = null;}
        try {calories = Double.valueOf(caloriesField.getText());} catch (NumberFormatException e) {calories = null;}
        try {servings = Double.valueOf(servingsField.getText());} catch (NumberFormatException e) {servings = null;}

        if(name.isBlank() || name.isEmpty()){
            warningLabel.setText("Insert a Name in the corresponding TextField");
        }else if(imageArrayList.isEmpty()){
            warningLabel.setText("Insert at least one Image");
        }else if(RecipeService.checkIfNameIsAvailable(name)) {
            warningLabel.setText("Name is available");
            data = DataSingleton.getInstance();
            if(RecipeService.addRecipe( new Recipe(name, data.getAuthorName(), time, LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    setNullIfEmpty(descriptionField.getText()), imageArrayList, setNullIfEmpty(categoryField.getText()),
                    keywordArrayList, ingredientArrayList, null, calories, servings, instructionsArrayList,
                    new ArrayList<>()))) {
                clearFields();
                warningLabel.setText("Recipe was added");
            }else warningLabel.setText("Error in adding recipe");
        }else{
            warningLabel.setText("Name isn't available");
        }
    }

    private String setNullIfEmpty(String string){
        if(string.isEmpty() || string.isBlank()) return null;
        else return string;
    }

    public void onBackClick(ActionEvent actionEvent) throws IOException {
        Utils.changeScene(actionEvent,"HomeAuthor.fxml");
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
        onAdd(instructionsField,instructionsArrayList,instructionsListView);}
    public void onRemoveInstructionClick(ActionEvent actionEvent) {
        onRemove(instructionsField,instructionsArrayList,ingredientListView);
    }
    private void onRemove(TextField textField, List<String> arrayList, ListView<String> listView){
        String field = textField.getText();
        if(!field.isBlank() && !field.isEmpty()) {
            arrayList.remove(field);
            observableList = FXCollections.observableList(arrayList);
            listView.setItems(observableList);
        }
    }
    private void onAdd(TextField textField, List<String> arrayList, ListView<String> listView){
        String field = textField.getText();
        if(!field.isBlank() && !field.isEmpty()) {
            arrayList.add(field);
            observableList = FXCollections.observableList(arrayList);
            listView.setItems(observableList);
        }
    }
}
