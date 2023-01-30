package com.example.demo1.gui;
import com.example.demo1.Configuration;
import com.example.demo1.model.Author;
import com.example.demo1.service.RecipeService;
import com.example.demo1.service.ReportedRecipeService;
import com.mongodb.internal.connection.tlschannel.util.Util;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ModeratorAnalyticsController implements Initializable {

    public AnchorPane anchorPane;
    private final TableViewAuthorScore tableViewAuthorScore = new TableViewAuthorScore();
    private Integer pageNumber = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createTableView(tableViewAuthorScore);

    }

    public void createTableView (TableViewAuthorScore TableViewObject) {
        TableViewObject.initializeTableView();
        searchInDBAndLoadInTableView(pageNumber);
        TableViewObject.setTable();
        TableViewObject.setEventForTableCells();
        anchorPane.getChildren().add(TableViewObject.getTabellaDB());
    }

    public void searchInDBAndLoadInTableView(Integer pageNumber){
        tableViewAuthorScore.resetObservableArrayList();
        List<Author> listAuthors = ReportedRecipeService.onHighestRatioQueryClick();
        listAuthors.forEach(author ->
                tableViewAuthorScore.addToObservableArrayList(new RowAuthorScore(author.getName(),author.getScore())));
        tableViewAuthorScore.setItems();
    }

    public void onBackToHomeClick(ActionEvent actionEvent) {
        Utils.changeScene(actionEvent,"Moderator.fxml");
    }
}
