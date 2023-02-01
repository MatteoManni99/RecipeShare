package com.example.demo1.gui;

import com.example.demo1.Configuration;
import com.example.demo1.service.AuthorService;
import com.example.demo1.service.ReportedRecipeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ModeratorController implements Initializable {
    public String nameToSearch;
    public TextField authorToSearchTextField;
    @FXML
    private Label recipeText;
    private double startingX;
    @FXML
    private Label reviewText;
    private Stage stage;
    @FXML
    private VBox vbox;
    private Integer pageNumber = 0;
    private String authorName;
    private Integer pageNumberReportedRecipe = 0;
    private String recipeName;
    private String authorNameClicked;


    private TableViewAuthor tabella;
    @FXML
    private AnchorPane anchorPane;
    private TableViewAbstract tableAuthor = new TableViewAuthor();
    private TableViewAbstract tableReportedRecipe = new TableViewReportedRecipe();
    @FXML
    public void onLogoutClick(ActionEvent actionEvent) throws IOException {
        Utils.changeScene(actionEvent,"Login.fxml");
    }

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

    // IMPLEMENTATO IN REPORTED RECIPE DAO
    public void onViewAnalyticsClick(ActionEvent actionEvent) {
        Utils.changeScene(actionEvent,"ModeratorAnalytics.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startingX = recipeText.getLayoutX();
        if (DataSingleton.getInstance().getTypeOfUser().equals("moderator")) {
            Button promoteAuthorButton = new Button("PROMOTE AUTHOR");
            promoteAuthorButton.setLayoutX(64);
            promoteAuthorButton.setLayoutY(120);
            promoteAuthorButton.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
                AuthorService.updatePromotion(authorNameClicked,1);
                authorNameClicked = null;
            });
            anchorPane.getChildren().add(promoteAuthorButton);
        }
        createTableView();
    }

    public void createTableView () {
        searchInDBAndLoadInTableView(authorName,pageNumber);
        searchInDBAndLoadInTableViewReportedRecipe(recipeName,pageNumberReportedRecipe);
        tableAuthor.setTable();
        tableReportedRecipe.setTable();
        tableReportedRecipe.setEventForTableCells();
        setEventForTableCells();
        anchorPane.getChildren().addAll(tableAuthor.getTable(),tableReportedRecipe.getTable());
    }

    public void setEventForTableCells() {
        tableAuthor.getTable().addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { //evento per il mouse clickato
                    TableCell cell = Utils.findCell(evt,tableAuthor.getTable());
                    if (cell != null && !cell.isEmpty()) {
                        if(cell.getTableColumn().getText().equals("Promotion")){
                            int rowIndex = cell.getIndex();
                            authorNameClicked = (String) tableAuthor.getTable().getColumns().get(1).getCellData(rowIndex);
                            cell.setText("1");
                        }
                        if(cell.getTableColumn().getText().equals("Author")) {
                            authorNameClicked = cell.getText();
                            DataSingleton.getInstance().setOtherAuthorName(authorNameClicked);
                            Utils.changeScene(evt,"Author.fxml");
                        }
                        evt.consume();
                    }
                }
        );
    }

    public void searchInDBAndLoadInTableView(String nameToSearch, Integer pageNumber) {
        tableAuthor.resetObservableArrayList();
        AuthorService.searchAuthors(nameToSearch, 10 * pageNumber, 10).forEach(author ->
                tableAuthor.addToObservableArrayList(
                        new RowAuthor(author.getName(), author.getPromotion(),
                                new ImageView(Configuration.AVATAR.get(author.getImage() - 1)))));
        tableAuthor.setItems();
    }

    public void searchInDBAndLoadInTableViewReportedRecipe(String nameToSearch, Integer pageNumber){
        tableReportedRecipe.resetObservableArrayList();
        ReportedRecipeService.getListReportedRecipes().forEach(reportedRecipe ->
                tableReportedRecipe.addToObservableArrayList(
                        new RowReportedRecipe(reportedRecipe.getName(), reportedRecipe.getAuthorName(),reportedRecipe.getReporterName(),
                                reportedRecipe.getDateReporting(),new ImageView(reportedRecipe.getImage()))));
        tableReportedRecipe.setItems();
    }

    public void onFindAuthorClick(ActionEvent actionEvent ) throws IOException {
        nameToSearch = authorToSearchTextField.getText( );
        if(nameToSearch.isBlank()) nameToSearch =  null;
        pageNumber =  0;
        searchInDBAndLoadInTableView(nameToSearch, pageNumber);
    }
}
