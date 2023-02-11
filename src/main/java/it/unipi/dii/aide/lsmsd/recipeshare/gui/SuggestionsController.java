package it.unipi.dii.aide.lsmsd.recipeshare.gui;

import it.unipi.dii.aide.lsmsd.recipeshare.Configuration;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowAuthor;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowImage;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowRecipe;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewAbstract;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewAuthor;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewRecipe;
import it.unipi.dii.aide.lsmsd.recipeshare.model.Author;
import it.unipi.dii.aide.lsmsd.recipeshare.model.RecipeReduced;
import it.unipi.dii.aide.lsmsd.recipeshare.service.AuthorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SuggestionsController implements Initializable {
    public AnchorPane anchorPane;
    private final TableViewAbstract tableViewAuthorSugg = new TableViewAuthor();
    private final TableViewAbstract tableViewRecipeSugg = new TableViewRecipe();
    private final DataSingleton data = DataSingleton.getInstance();

    private List<RecipeReduced> suggestedRecipe;
    private Integer pageRecipe = 0;

    private Integer pageAuthor = 0;
    private String pageBefore = null;
    @FXML
    private Button nextRecipePageButton;

    @FXML
    private Button nextAuthorPageButton;
    @FXML
    private TextField pageRecipeField;
    @FXML
    private TextField pageAuthorField;
    @FXML
    private void onBackClick(ActionEvent actionEvent){
        Utils.changeScene(actionEvent,"HomeAuthor.fxml");
    }
    @FXML
    private void onNextRecipePageClick() {
        pageRecipe += 1;
        searchInDBAndLoadInTableViewRecipeSugg();
        pageRecipeField.setText(String.valueOf(pageRecipe+1));
    }
    @FXML
    private void onPreviousRecipePageClick() {
        if(pageRecipe>=1){
            pageRecipe -= 1;
            searchInDBAndLoadInTableViewRecipeSugg();
            pageRecipeField.setText(String.valueOf(pageRecipe+1));
            nextRecipePageButton.setDisable(false);
        }
    }

    @FXML
    private void onNextAuthorPageClick() {
        pageAuthor += 1;
        searchInDBAndLoadInTableViewAuthorSugg(new Author(data.getAuthorName(),data.getAvatarIndex()));
        pageAuthorField.setText(String.valueOf(pageAuthor+1));
    }
    @FXML
    private void onPreviousAuthorPageClick() {
        if(pageAuthor>=1){
            pageAuthor -= 1;
            searchInDBAndLoadInTableViewAuthorSugg(new Author(data.getAuthorName(),data.getAvatarIndex()));
            pageAuthorField.setText(String.valueOf(pageAuthor+1));
            nextAuthorPageButton.setDisable(false);
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageRecipeField.setText(String.valueOf(pageRecipe+1));
        pageAuthorField.setText(String.valueOf(pageAuthor+1));
        createTableViewAuthorSugg();
        createTableViewRecipeSugg();
        DataSingleton.getInstance().setPageBefore("Suggestions.fxml");
    }

    private void createTableViewAuthorSugg(){
        tableViewAuthorSugg.getTable().setLayoutX(560);
        tableViewAuthorSugg.getTable().setLayoutY(100);
        tableViewAuthorSugg.getTable().setPrefSize(250,480);
        tableViewAuthorSugg.setEventForTableCells();
        searchInDBAndLoadInTableViewAuthorSugg(new Author(data.getAuthorName(),data.getAvatarIndex()));
        tableViewAuthorSugg.setTable();
        anchorPane.getChildren().add(tableViewAuthorSugg.getTable());
    }

    private void searchInDBAndLoadInTableViewAuthorSugg(Author currentAuthor){
        tableViewAuthorSugg.resetObservableArrayList();
        AuthorService.getAuthorSuggested(currentAuthor,pageAuthor*10,10).forEach(author ->
                tableViewAuthorSugg.addToObservableArrayList(new RowAuthor(author.getName(), 0,
                        new RowImage(new ImageView(Configuration.AVATAR.get(author.getImage() - 1))).getImage())));
        tableViewAuthorSugg.setItems();
    }

    private void createTableViewRecipeSugg(){
        tableViewRecipeSugg.getTable().setLayoutX(20);
        tableViewRecipeSugg.getTable().setLayoutY(120);
        tableViewRecipeSugg.getTable().setPrefSize(465,460);
        tableViewRecipeSugg.setEventForTableCells();
        searchInDBAndLoadInTableViewRecipeSugg();
        tableViewRecipeSugg.setTable();
        anchorPane.getChildren().add(tableViewRecipeSugg.getTable());
    }
    private void searchInDBAndLoadInTableViewRecipeSugg(){
        tableViewRecipeSugg.resetObservableArrayList();
        AuthorService.getRecipeSuggested(data.getAuthorName(),pageRecipe*10,10).forEach(recipe ->
                tableViewRecipeSugg.addToObservableArrayList(new RowRecipe(recipe.getName(), recipe.getAuthorName(),
                        new ImageView(recipe.getImage()))));
        tableViewRecipeSugg.setItems();
    }
}
