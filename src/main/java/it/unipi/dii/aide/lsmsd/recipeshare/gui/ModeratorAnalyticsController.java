package it.unipi.dii.aide.lsmsd.recipeshare.gui;
import it.unipi.dii.aide.lsmsd.recipeshare.Configuration;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowAuthorScore;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewAbstract;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewAuthorScore;
import it.unipi.dii.aide.lsmsd.recipeshare.service.ReportedRecipeService;
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
        DataSingleton.getInstance().setPageBefore("ModeratorAnalytics.fxml");
        createTableView();
    }

    public void createTableView () {
        searchInDBAndLoadInTableView(pageNumber);
        tableViewAuthorScore.setTable();
        tableViewAuthorScore.setEventForTableCells();
        tableViewAuthorScore.getTable().setLayoutX(30);
        tableViewAuthorScore.getTable().setLayoutY(100);
        tableViewAuthorScore.getTable().setPrefSize(300,450);
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
        Utils.changeScene(actionEvent,"HomeModerator.fxml");
    }
}
