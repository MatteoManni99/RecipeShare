package com.example.demo1.gui;

import com.example.demo1.Configuration;
import com.example.demo1.dao.mongo.AuthorMongoDAO;
import com.example.demo1.dao.neo4j.AuthorNeoDAO;
import com.example.demo1.gui.row.RowRecipe;
import com.example.demo1.gui.tableview.TableViewAbstract;
import com.example.demo1.gui.tableview.TableViewRecipe;
import com.example.demo1.gui.tableview.TableViewRecipeWithoutAuthor;
import com.example.demo1.service.AuthorService;
import com.example.demo1.service.RecipeService;
import com.example.demo1.model.Author;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
        if(data.getTypeOfUser().equals("moderator")) Utils.changeScene(actionEvent,"Moderator.fxml");
        else Utils.changeScene(actionEvent,"Loggato.fxml");
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
        if (author == null) System.out.println("NON ESISTE NELLA COLLECTION AUTHOR"); //problema: authorName c'è in Recipe ma non in Author

        //delete follow or unfollow button //TODO disabilitare anzichè togliere
        if(AuthorService.checkIfFollowIsAvailable(data.getAuthorName(),author.getName()))
            unfollowButton.setDisable(true);
            //anchorPane.getChildren().remove(unfollowButton);
        else followButton.setDisable(true);
            //anchorPane.getChildren().remove(followButton);

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
