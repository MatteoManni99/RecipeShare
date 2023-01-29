package com.example.demo1.gui;

import com.example.demo1.service.RecipeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class LoggatoController implements Initializable{
    public AnchorPane anchorPane;
    @FXML
    private Label welcomeText;
    private final ClassForTableView TableViewObject = new ClassForTableView();
    @FXML
    private TextField nameToSearchTextField;
    private String nameToSearch = null;
    private Integer pageNumber = 0;
    @FXML
    public void onLogoutClick(ActionEvent actionEvent){
        Utils.changeScene(actionEvent,"Login.fxml");
    }

    @FXML
    public void onCercaUtenteClick(ActionEvent actionEvent){
        Utils.changeScene(actionEvent,"Ricerca_Utente.fxml");
    }

    @FXML
    public void onAnalyticsClick(ActionEvent actionEvent){
        Utils.changeScene(actionEvent,"LoggatoAnalytics.fxml");
    }

    @FXML
    public void onProvaRecipe(ActionEvent actionEvent){
        Utils.changeScene(actionEvent,"Recipe.fxml");
    }
    @FXML
    public void onNextPageClick(){
        pageNumber += 1;
        searchInDBAndLoadInTableView(nameToSearch,pageNumber);
    }
    @FXML
    public void onPreviousPageClick(){
        if(pageNumber>=1){
            pageNumber -= 1;
            searchInDBAndLoadInTableView(nameToSearch,pageNumber);
        }
    }
    @FXML
    public void onFindRecipeByNameClick(){
        nameToSearch = nameToSearchTextField.getText();
        if(nameToSearch.isBlank()) nameToSearch = null;
        System.out.println(nameToSearch); //solo per debug sarà da togliere
        pageNumber = 0;
        searchInDBAndLoadInTableView(nameToSearch,pageNumber);
    }
    public void searchInDBAndLoadInTableView(String nameToSearch, Integer pageNumber){
        TableViewObject.resetObservableArrayList();
        List<RecipeTableView> listRecipeTable = new ArrayList<>();
        RecipeService.getRecipeFromName(nameToSearch, pageNumber*10, 10)
                .forEach(recipeReducted -> listRecipeTable.add(
                        new RecipeTableView(recipeReducted.getName(), recipeReducted.getAuthorName(), new ImageView(recipeReducted.getImage()))));
        TableViewObject.setObservableArrayList(listRecipeTable);
        TableViewObject.setItems();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableViewObject.initializeTableView("Loggato");
        searchInDBAndLoadInTableView(nameToSearch,pageNumber);
        TableViewObject.setEventForTableCells();
        TableViewObject.setTabellaDB();
        anchorPane.getChildren().add(TableViewObject.getTabellaDB());
    }

    @FXML
    public void onAddRecipeClick(ActionEvent actionEvent) throws IOException {
        Utils.changeScene(actionEvent,"AddRecipe.fxml");
    }
    public void onPersonalProfileClick(ActionEvent actionEvent) throws IOException {
        Utils.changeScene(actionEvent,"AuthorProfile.fxml");
    }
}