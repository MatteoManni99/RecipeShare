package it.unipi.dii.aide.lsmsd.recipeshare.gui;

import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowRecipe;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewAbstract;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewRecipe;
import it.unipi.dii.aide.lsmsd.recipeshare.service.RecipeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.*;

public class HomeAuthorController implements Initializable{
    public AnchorPane anchorPane;
    private final TableViewAbstract TableViewObject = new TableViewRecipe();
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
        Utils.changeScene(actionEvent,"SearchAuthor.fxml");
    }

    @FXML
    public void onAnalyticsClick(ActionEvent actionEvent){
        Utils.changeScene(actionEvent,"AuthorAnalytics.fxml");
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
        RecipeService.getRecipeFromName(nameToSearch, pageNumber*10, 10)
                .forEach(recipeReducted -> TableViewObject.addToObservableArrayList(
                        new RowRecipe(recipeReducted.getName(), recipeReducted.getAuthorName(), new ImageView(recipeReducted.getImage()))));
        TableViewObject.setItems();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchInDBAndLoadInTableView(nameToSearch,pageNumber);
        TableViewObject.getTable().setLayoutY(110);
        TableViewObject.getTable().setLayoutX(183);
        TableViewObject.getTable().setPrefSize(465,480);
        TableViewObject.setEventForTableCells();
        TableViewObject.setTable();
        anchorPane.getChildren().add(TableViewObject.getTable());
    }

    @FXML
    public void onAddRecipeClick(ActionEvent actionEvent) {
        Utils.changeScene(actionEvent,"AddRecipe.fxml");
    }
    public void onPersonalProfileClick(ActionEvent actionEvent) {
        Utils.changeScene(actionEvent,"AuthorProfile.fxml");
    }
    public void onFollowersClick(ActionEvent actionEvent) {
        Utils.changeScene(actionEvent,"FollowerAndFollowing.fxml");
    }
}