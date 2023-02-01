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
    private final TableViewAbstract tableViewAuthorScore = new TableViewAuthorScore();
    private Integer pageNumber = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createTableView();

    }

    public void createTableView () {
        searchInDBAndLoadInTableView(pageNumber);
        tableViewAuthorScore.setTable();
        tableViewAuthorScore.setEventForTableCells();
        anchorPane.getChildren().add(tableViewAuthorScore.getTable());
    }

    public void searchInDBAndLoadInTableView(Integer pageNumber){
        tableViewAuthorScore.resetObservableArrayList();
        ReportedRecipeService.onHighestRatioQueryClick().forEach(author ->
                tableViewAuthorScore.addToObservableArrayList(new RowAuthorScore(author.getName(),author.getScore())));
        tableViewAuthorScore.setItems();
    }

    public void onBackToHomeClick(ActionEvent actionEvent) {
        Utils.changeScene(actionEvent,"Moderator.fxml");
    }
}
