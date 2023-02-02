package com.example.demo1.gui;
import com.example.demo1.Configuration;
import com.example.demo1.gui.row.RowAuthorScore;
import com.example.demo1.gui.tableview.TableViewAbstract;
import com.example.demo1.gui.tableview.TableViewAuthorScore;
import com.example.demo1.service.ReportedRecipeService;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
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
        tableViewAuthorScore.getTable().setLayoutX(20);
        tableViewAuthorScore.getTable().setLayoutY(200);
        anchorPane.getChildren().add(tableViewAuthorScore.getTable());
    }

    public void searchInDBAndLoadInTableView(Integer pageNumber){
        tableViewAuthorScore.resetObservableArrayList();
        ReportedRecipeService.onHighestRatioQueryClick().forEach(author ->
                tableViewAuthorScore.addToObservableArrayList(new RowAuthorScore(author.getName(),author.getScore(),
                        new ImageView(Configuration.AVATAR.get(author.getImage() - 1)))));
        tableViewAuthorScore.setItems();
    }

    public void onBackToHomeClick(ActionEvent actionEvent) {
        Utils.changeScene(actionEvent,"Moderator.fxml");
    }
}
