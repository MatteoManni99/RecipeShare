package it.unipi.dii.aide.lsmsd.recipeshare.gui;

import it.unipi.dii.aide.lsmsd.recipeshare.Configuration;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowAuthor;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowImage;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowRecipe;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewAbstract;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewAuthor;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewRecipe;
import it.unipi.dii.aide.lsmsd.recipeshare.model.Author;
import it.unipi.dii.aide.lsmsd.recipeshare.service.AuthorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SuggestionsController implements Initializable {
    public AnchorPane anchorPane;
    private final TableViewAbstract tableViewAuthorSugg = new TableViewAuthor();
    private final TableViewAbstract tableViewRecipeSugg = new TableViewRecipe();
    private final DataSingleton data = DataSingleton.getInstance();

    private String pageBefore = null;

    @FXML
    public void onBackClick(ActionEvent actionEvent){
        DataSingleton.getInstance().setPageBefore("Suggestions.fxml");
        Utils.changeScene(actionEvent,pageBefore);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        createTableViewAuthorSugg();
        createTableViewRecipeSugg();
        pageBefore = DataSingleton.getInstance().getPageBefore();
    }

    public void createTableViewAuthorSugg(){
        tableViewAuthorSugg.getTable().setLayoutX(560);
        tableViewAuthorSugg.getTable().setLayoutY(50);
        tableViewAuthorSugg.getTable().setPrefSize(250,530);
        tableViewAuthorSugg.setEventForTableCells();
        searchInDBAndLoadInTableViewAuthorSugg(new Author(data.getAuthorName(),data.getAvatarIndex()));
        tableViewAuthorSugg.setTable();
        anchorPane.getChildren().add(tableViewAuthorSugg.getTable());
    }

    public void searchInDBAndLoadInTableViewAuthorSugg(Author currentAuthor){
        tableViewAuthorSugg.resetObservableArrayList();
        AuthorService.getAuthorSuggested(currentAuthor).forEach(author ->
                tableViewAuthorSugg.addToObservableArrayList(new RowAuthor(author.getName(), 0,
                        new RowImage(new ImageView(Configuration.AVATAR.get(author.getImage() - 1))).getImage())));
        tableViewAuthorSugg.setItems();
    }

    public void createTableViewRecipeSugg(){
        tableViewRecipeSugg.getTable().setLayoutX(20);
        tableViewRecipeSugg.getTable().setLayoutY(120);
        tableViewRecipeSugg.getTable().setPrefSize(465,460);
        tableViewRecipeSugg.setEventForTableCells();
        searchInDBAndLoadInTableRecipeSugg(data.getAuthorName());
        tableViewRecipeSugg.setTable();
        anchorPane.getChildren().add(tableViewRecipeSugg.getTable());
    }
    public void searchInDBAndLoadInTableRecipeSugg(String authorName){
        tableViewRecipeSugg.resetObservableArrayList();
        AuthorService.getRecipeSuggested(authorName).forEach( recipeReducted ->
                tableViewRecipeSugg.addToObservableArrayList(new RowRecipe(recipeReducted.getName(), recipeReducted.getAuthorName(),
                       new ImageView(recipeReducted.getImage()))));
        tableViewRecipeSugg.setItems();
    }
}
