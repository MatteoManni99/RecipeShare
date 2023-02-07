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
    private Integer pageNumber = 0;
    private final DataSingleton data = DataSingleton.getInstance();
    private String authorName;
    private Author author;
    private final TableViewAbstract tableViewAuthor = new TableViewRecipeWithoutAuthor();
    @FXML
    private AnchorPane anchorPane;

    @FXML
    public void onBackClick(ActionEvent actionEvent) throws IOException {
        if(data.getTypeOfUser().equals("moderator")) Utils.changeScene(actionEvent,"HomeModerator.fxml");
        else Utils.changeScene(actionEvent,"HomeAuthor.fxml");
    }

    public void onFollowButtonClick(ActionEvent actionEvent) {
        if(AuthorService.followAnOtherAuthor(data.getAuthorName(), author.getName())){
            Utils.changeScene(actionEvent,"Author.fxml");
        }else System.out.println("Follow non riuscito");
    }
    public void onUnfollowButtonClick(ActionEvent actionEvent) {
        if(AuthorService.unfollowAuthor(data.getAuthorName(), author.getName())){
            Utils.changeScene(actionEvent,"Author.fxml");
        }else System.out.println("Unfollow non riuscito");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(data.getTypeOfUser().equals("moderator")){
            anchorPane.getChildren().remove(followButton);
            anchorPane.getChildren().remove(unfollowButton);
        }

        authorName = data.getOtherAuthorName();
        author = AuthorService.getAuthor(authorName);
        //TODO forse da togliere
        if (author == null) System.out.println("NON ESISTE NELLA COLLECTION AUTHOR"); //problema: authorName c'è in Recipe ma non in Author

        if(AuthorService.checkIfFollowIsAvailable(data.getAuthorName(),author.getName()))
            unfollowButton.setDisable(true);
        else followButton.setDisable(true);

        name.setText(Objects.requireNonNull(author).getName());
        image.setImage(Configuration.AVATAR.get(author.getImage() - 1));
        createTableView();
    }

    public void createTableView() {
        searchInDBAndLoadInTableView(authorName, pageNumber);
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
                .forEach(recipeReducted -> tableViewAuthor.addToObservableArrayList(
                        new RowRecipe(recipeReducted.getName(), recipeReducted.getAuthorName(), new ImageView(recipeReducted.getImage()))));
        tableViewAuthor.setItems();
    }
    @FXML
    public void onNextPageClick(){
        pageNumber = pageNumber + 1;
        searchInDBAndLoadInTableView(authorName,pageNumber);
    }
    @FXML
    public void onPreviousPageClick(){
        if(pageNumber>=1){
            pageNumber = pageNumber - 1;
            searchInDBAndLoadInTableView(authorName,pageNumber);
        }
    }
}
