package it.unipi.dii.aide.lsmsd.recipeshare.gui;

import it.unipi.dii.aide.lsmsd.recipeshare.Configuration;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowAuthor;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowImage;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewAbstract;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewAuthor;
import it.unipi.dii.aide.lsmsd.recipeshare.service.AuthorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class FollowerController implements Initializable {
    public AnchorPane anchorPane;
    private final TableViewAbstract tableViewAuthorFollowers = new TableViewAuthor();
    private final TableViewAbstract tableViewAuthorFollowing = new TableViewAuthor();
    private final DataSingleton data = DataSingleton.getInstance();

    @FXML
    public void onBackClick(ActionEvent actionEvent){
        Utils.changeScene(actionEvent,"HomeAuthor.fxml");
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        createTableViewFollowers();
        createTableViewFollowing();
    }

    public void createTableViewFollowers(){
        tableViewAuthorFollowers.getTable().setLayoutX(20);
        tableViewAuthorFollowers.getTable().setLayoutY(120);
        tableViewAuthorFollowers.getTable().setPrefSize(250,400);
        tableViewAuthorFollowers.setEventForTableCells();
        searchInDBAndLoadInTableViewFollowers(data.getAuthorName());
        tableViewAuthorFollowers.setTable();
        anchorPane.getChildren().add(tableViewAuthorFollowers.getTable());
    }

    public void searchInDBAndLoadInTableViewFollowers(String authorName){
        tableViewAuthorFollowers.resetObservableArrayList();
        AuthorService.getFollowers(authorName).forEach( author ->
                tableViewAuthorFollowers.addToObservableArrayList(new RowAuthor(author.getName(), 0,
                        new RowImage(new ImageView(Configuration.AVATAR.get(author.getImage() - 1))).getImage())));
        tableViewAuthorFollowers.setItems();
    }
    public void createTableViewFollowing(){
        tableViewAuthorFollowing.getTable().setLayoutX(420);
        tableViewAuthorFollowing.getTable().setLayoutY(120);
        tableViewAuthorFollowing.getTable().setPrefSize(250,400);
        tableViewAuthorFollowing.setEventForTableCells();
        searchInDBAndLoadInTableViewFollowing(data.getAuthorName());
        tableViewAuthorFollowing.setTable();
        anchorPane.getChildren().add(tableViewAuthorFollowing.getTable());
    }
    public void searchInDBAndLoadInTableViewFollowing(String authorName){
        tableViewAuthorFollowing.resetObservableArrayList();
        AuthorService.getFollowing(authorName).forEach( author ->
                tableViewAuthorFollowing.addToObservableArrayList(new RowAuthor(author.getName(), 0,
                        new RowImage(new ImageView(Configuration.AVATAR.get(author.getImage() - 1))).getImage())));
        tableViewAuthorFollowing.setItems();
    }
}
