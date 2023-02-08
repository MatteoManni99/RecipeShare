package it.unipi.dii.aide.lsmsd.recipeshare.gui;

import it.unipi.dii.aide.lsmsd.recipeshare.Configuration;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowAuthor;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowImage;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowRecipe;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewAbstract;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewAuthor;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewRecipe;
import it.unipi.dii.aide.lsmsd.recipeshare.model.Author;
import it.unipi.dii.aide.lsmsd.recipeshare.model.RecipeReducted;
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

    private List<RecipeReducted> suggestedRecipe;
    private Integer pageRecipe = 0;
    private String pageBefore = null;
    @FXML
    private Button nextButton;
    @FXML
    private TextField pageRecipeField;

    @FXML
    private void onBackClick(ActionEvent actionEvent){
        DataSingleton.getInstance().setPageBefore("Suggestions.fxml");
        Utils.changeScene(actionEvent,pageBefore);
    }
    @FXML
    private void onNextClick() {
        pageRecipe += 1;
        loadInTableView(pageRecipe);
        pageRecipeField.setText(String.valueOf(pageRecipe+1));
    }
    @FXML
    private void onPreviousClick() {
        if(pageRecipe>=1){
            pageRecipe -= 1;
            loadInTableView(pageRecipe);
            pageRecipeField.setText(String.valueOf(pageRecipe+1));
            nextButton.setDisable(false);
        }
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageRecipeField.setText(String.valueOf(pageRecipe+1));
        createTableViewAuthorSugg();
        createTableViewRecipeSugg();
        pageBefore = DataSingleton.getInstance().getPageBefore();
    }

    private void createTableViewAuthorSugg(){
        tableViewAuthorSugg.getTable().setLayoutX(560);
        tableViewAuthorSugg.getTable().setLayoutY(50);
        tableViewAuthorSugg.getTable().setPrefSize(250,530);
        tableViewAuthorSugg.setEventForTableCells();
        searchInDBAndLoadInTableViewAuthorSugg(new Author(data.getAuthorName(),data.getAvatarIndex()));
        tableViewAuthorSugg.setTable();
        anchorPane.getChildren().add(tableViewAuthorSugg.getTable());
    }

    private void searchInDBAndLoadInTableViewAuthorSugg(Author currentAuthor){
        tableViewAuthorSugg.resetObservableArrayList();
        AuthorService.getAuthorSuggested(currentAuthor).forEach(author ->
                tableViewAuthorSugg.addToObservableArrayList(new RowAuthor(author.getName(), 0,
                        new RowImage(new ImageView(Configuration.AVATAR.get(author.getImage() - 1))).getImage())));
        tableViewAuthorSugg.setItems();
    }

    private void createTableViewRecipeSugg(){
        tableViewRecipeSugg.getTable().setLayoutX(20);
        tableViewRecipeSugg.getTable().setLayoutY(120);
        tableViewRecipeSugg.getTable().setPrefSize(465,460);
        suggestedRecipe = AuthorService.getRecipeSuggested(data.getAuthorName());
        if(!suggestedRecipe.isEmpty()) {
            loadInTableView(pageRecipe);
            tableViewRecipeSugg.setEventForTableCells();
        }
        tableViewRecipeSugg.setTable();
        anchorPane.getChildren().add(tableViewRecipeSugg.getTable());
    }
    private void loadInTableView(Integer pageRecipe){
        tableViewRecipeSugg.resetObservableArrayList();
        if((suggestedRecipe.size()-pageRecipe*10)>=10) {
            suggestedRecipe.subList(pageRecipe*10, pageRecipe*10 + 10).forEach(recipeReducted ->
                    tableViewRecipeSugg.addToObservableArrayList(new RowRecipe(recipeReducted.getName(), recipeReducted.getAuthorName(),
                            new ImageView(recipeReducted.getImage()))));
        }else {
            suggestedRecipe.subList(pageRecipe*10, suggestedRecipe.size()).forEach(recipeReducted ->
                tableViewRecipeSugg.addToObservableArrayList(new RowRecipe(recipeReducted.getName(), recipeReducted.getAuthorName(),
                        new ImageView(recipeReducted.getImage()))));
            nextButton.setDisable(true);
        }
        tableViewRecipeSugg.setItems();
    }
}
