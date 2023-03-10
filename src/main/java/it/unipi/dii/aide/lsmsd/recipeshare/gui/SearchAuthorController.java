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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchAuthorController implements Initializable {
    @FXML
    public Button cercaUtenteButton;
    public TextField authorToSearchTextField;
    public TextField pageAuthorField;
    private String authorNameClicked; //qui ci salvo l'utente della tabella che è stato clickato e che quindi si vuole promuovere
    public AnchorPane anchorPane;
    private Integer pageAuthor = 0;
    private String nameToSearch = null;
    private final TableViewAbstract tableViewAuthor = new TableViewAuthor();

    private String pageBefore = null;

    @FXML
    public void onNextAuthorPageClick(){
        pageAuthor = pageAuthor + 1;
        pageAuthorField.setText(String.valueOf(pageAuthor+1));
        searchInDBAndLoadInTableView(nameToSearch,pageAuthor);
    }
    @FXML
    public void onPreviousAuthorPageClick(){
        if(pageAuthor>=1){
            pageAuthor = pageAuthor - 1;
            pageAuthorField.setText(String.valueOf(pageAuthor+1));
            searchInDBAndLoadInTableView(nameToSearch,pageAuthor);
        }
    }

    @FXML
    public void onBackClick(ActionEvent actionEvent){
        Utils.changeScene(actionEvent,"HomeAuthor.fxml");
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        pageBefore = DataSingleton.getInstance().getPageBefore();
        pageAuthorField.setText(String.valueOf(pageAuthor+1));
        DataSingleton.getInstance().setPageBefore("SearchAuthor.fxml");
        if (DataSingleton.getInstance().getTypeOfUser().equals("moderator")) {
            Button promoteAuthorButton = new Button("PROMOTE AUTHOR");
            promoteAuthorButton.setLayoutX(164);
            promoteAuthorButton.setLayoutY(140);
            promoteAuthorButton.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
                AuthorService.updatePromotion(authorNameClicked,1);
                authorNameClicked = null;
            });
            anchorPane.getChildren().add(promoteAuthorButton);
        }
        createTableView();
    }

    public void createTableView () {
        tableViewAuthor.getTable().setLayoutX(220);
        tableViewAuthor.getTable().setLayoutY(150);
        tableViewAuthor.getTable().setPrefHeight(400);
        tableViewAuthor.getTable().setPrefWidth(250);
        tableViewAuthor.setEventForTableCells();
        searchInDBAndLoadInTableView(nameToSearch,pageAuthor);
        tableViewAuthor.setTable();
        anchorPane.getChildren().add(tableViewAuthor.getTable());
    }

    public void searchInDBAndLoadInTableView(String nameToSearch, Integer pageAuthor){
        tableViewAuthor.resetObservableArrayList();
        AuthorService.searchAuthors(nameToSearch,pageAuthor*10,10).forEach( author ->
            tableViewAuthor.addToObservableArrayList(new RowAuthor(author.getName(), author.getPromotion(),
                    new RowImage(new ImageView(Configuration.AVATAR.get(author.getImage() - 1))).getImage())));
        tableViewAuthor.setItems();
    }


    @FXML
    public void onSearchAuthorClick(ActionEvent actionEvent) {
        nameToSearch = authorToSearchTextField.getText();
        if(nameToSearch.isBlank()) nameToSearch = null;
        pageAuthor = 0;
        searchInDBAndLoadInTableView(nameToSearch,pageAuthor);
    }

}
