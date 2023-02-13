package it.unipi.dii.aide.lsmsd.recipeshare.gui;

import it.unipi.dii.aide.lsmsd.recipeshare.Configuration;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowRecipe;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewAbstract;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewRecipeWithoutAuthor;
import it.unipi.dii.aide.lsmsd.recipeshare.service.AuthorService;
import it.unipi.dii.aide.lsmsd.recipeshare.service.RecipeService;
import it.unipi.dii.aide.lsmsd.recipeshare.model.Author;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AuthorController implements Initializable {
    @FXML
    public ImageView image;
    @FXML
    public TextField name;
    public Button followButton;
    public Button unfollowButton;
    public TextField pageAuthorField;
    private Integer pageAuthor = 0;
    private final DataSingleton data = DataSingleton.getInstance();
    private String authorName;
    private Author author;
    private final TableViewAbstract tableViewAuthor = new TableViewRecipeWithoutAuthor();
    private static String pageBefore = null;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label label;

    @FXML
    public void onBackClick(ActionEvent actionEvent) throws IOException {
        Utils.changeScene(actionEvent,pageBefore);
    }

    public void onFollowButtonClick(ActionEvent actionEvent) {
        if(AuthorService.followAnOtherAuthor(data.getAuthorName(), author.getName())){
            Utils.changeScene(actionEvent,"Author.fxml");
        }else label.setText("Follow failed");
    }
    public void onUnfollowButtonClick(ActionEvent actionEvent) {
        if(AuthorService.unfollowAuthor(data.getAuthorName(), author.getName())){
            Utils.changeScene(actionEvent,"Author.fxml");
        }else label.setText("Unfollow failed");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(data.getTypeOfUser().equals("moderator")){
            anchorPane.getChildren().remove(followButton);
            anchorPane.getChildren().remove(unfollowButton);
        }

        pageAuthorField.setText(String.valueOf(pageAuthor+1));
        if (!DataSingleton.getInstance().getPageBefore().equals("Author.fxml")) pageBefore = DataSingleton.getInstance().getPageBefore();
        DataSingleton.getInstance().setPageBefore("Author.fxml");
        authorName = data.getOtherAuthorName();
        author = AuthorService.getAuthor(authorName);

        if(AuthorService.checkIfFollowIsAvailable(data.getAuthorName(),author.getName()))
            unfollowButton.setDisable(true);
        else followButton.setDisable(true);

        name.setText(Objects.requireNonNull(author).getName());
        image.setImage(Configuration.AVATAR.get(author.getImage() - 1));
        createTableView();
    }

    public void createTableView() {
        searchInDBAndLoadInTableView(authorName, pageAuthor);
        tableViewAuthor.setEventForTableCells();
        tableViewAuthor.setTable();
        tableViewAuthor.getTable().setLayoutX(40);
        tableViewAuthor.getTable().setLayoutY(220);
        tableViewAuthor.getTable().setPrefHeight(350);
        tableViewAuthor.getTable().setPrefWidth(450);
        anchorPane.getChildren().add(tableViewAuthor.getTable());
    }

    public void searchInDBAndLoadInTableView(String nameToSearch, Integer pageNumber) {
        tableViewAuthor.resetObservableArrayList();
        RecipeService.getRecipeFromAuthor(nameToSearch, pageNumber*10, 10)
                .forEach(recipeReduced -> tableViewAuthor.addToObservableArrayList(
                        new RowRecipe(recipeReduced.getName(), recipeReduced.getAuthorName(), new ImageView(recipeReduced.getImage()))));
        tableViewAuthor.setItems();
    }
    @FXML
    public void onNextAuthorPageClick(){
        pageAuthor = pageAuthor + 1;
        searchInDBAndLoadInTableView(authorName,pageAuthor);
        pageAuthorField.setText(String.valueOf(pageAuthor+1));
    }
    @FXML
    public void onPreviousAuthorPageClick(){
        if(pageAuthor>=1){
            pageAuthor = pageAuthor - 1;
            pageAuthorField.setText(String.valueOf(pageAuthor+1));
            searchInDBAndLoadInTableView(authorName,pageAuthor);
        }
    }
}
