package it.unipi.dii.aide.lsmsd.recipeshare.gui;

import it.unipi.dii.aide.lsmsd.recipeshare.Configuration;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowAuthor;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowReportedRecipe;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowTableView;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewAbstract;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewAuthor;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewReportedRecipe;
import it.unipi.dii.aide.lsmsd.recipeshare.service.AuthorService;
import it.unipi.dii.aide.lsmsd.recipeshare.service.ReportedRecipeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.*;

public class HomeModeratorController implements Initializable {
    public TextField authorToSearchTextField;
    public TextField pageAuthorField;
    public TextField pageReportedRecipeField;

    private Integer pageAuthor = 0;

    private Integer pageReportedRecipe = 0;
    private String authorName;
    private String authorNameClicked;
    public TextField reportedRecipeToSearchTextField;
    private String reportedRecipeName;
    @FXML
    private AnchorPane anchorPane;

    private TableCell cellAuthorPromotion;
    private final TableViewAbstract tableAuthor = new TableViewAuthor();
    private final TableViewAbstract tableReportedRecipe = new TableViewReportedRecipe();
    @FXML
    public void onLogoutClick(ActionEvent actionEvent){
        Utils.changeScene(actionEvent,"Login.fxml");
    }

    public void onViewAnalyticsClick(ActionEvent actionEvent) {
        Utils.changeScene(actionEvent,"ModeratorAnalytics.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DataSingleton.getInstance().setPageBefore("HomeModerator.fxml");
        pageAuthorField.setText(String.valueOf(pageAuthor+1));
        pageReportedRecipeField.setText(String.valueOf(pageReportedRecipe+1));
        createTableViews();
    }

    public void createTableViews() {
        searchInDBAndLoadInTableViewAuthor(authorName,pageAuthor);
        searchInDBAndLoadInTableViewReportedRecipe(null,pageReportedRecipe);
        tableAuthor.setTable();
        tableAuthor.getTable().setPrefSize(300,470);
        tableAuthor.getTable().setLayoutX(670);
        tableAuthor.getTable().setLayoutY(100);
        tableAuthor.setEventForTableCells();

        tableReportedRecipe.setTable();
        tableReportedRecipe.setEventForTableCells();
        tableReportedRecipe.getTable().setPrefSize(600,420);
        tableReportedRecipe.getTable().setLayoutX(20);
        tableReportedRecipe.getTable().setLayoutY(150);
        setEventPromotion();
        anchorPane.getChildren().addAll(tableAuthor.getTable(),tableReportedRecipe.getTable());
    }

    public void setEventPromotion() {
        tableAuthor.getTable().addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { //evento per il mouse clickato
                    TableCell<RowTableView, ?> cell = Utils.findCell(evt,tableAuthor.getTable());
                    if (cell != null && !cell.isEmpty()) {
                        if(cell.getTableColumn().getText().equals("Promotion")){
                            if (!cell.getText().equals("0")) return;
                            authorNameClicked = (String) tableAuthor.getTable().getColumns().get(1).getCellData(cell.getIndex());
                            cellAuthorPromotion = cell;
                        }
                        evt.consume();
                    }
                }
        );
    }
    public void onPromoteAuthorClick(ActionEvent actionEvent) {
        AuthorService.updatePromotion(authorNameClicked,1);
        authorNameClicked = null;
        searchInDBAndLoadInTableViewAuthor(authorName, pageAuthor);
    }

    public void searchInDBAndLoadInTableViewAuthor(String nameToSearch, Integer pageNumber) {
        tableAuthor.resetObservableArrayList();
        AuthorService.searchAuthors(nameToSearch, 10 * pageNumber, 10).forEach(author ->
                tableAuthor.addToObservableArrayList(
                        new RowAuthor(author.getName(), author.getPromotion(),
                                new ImageView(Configuration.AVATAR.get(author.getImage() - 1)))));
        tableAuthor.setItems();
    }
    public void onFindAuthorClick(ActionEvent actionEvent ) {
        authorName = authorToSearchTextField.getText( );
        if(authorName.isBlank()) authorName = null;
        pageAuthor =  0;
        searchInDBAndLoadInTableViewAuthor(authorName, pageAuthor);
    }
    public void onNextPageAuthorClick(){
        pageAuthor = pageAuthor + 1;
        pageAuthorField.setText(String.valueOf(pageAuthor+1));
        searchInDBAndLoadInTableViewAuthor(authorName,pageAuthor);
    }
    @FXML
    public void onPreviousPageAuthorClick(){
        if(pageAuthor>=1){
            pageAuthor = pageAuthor - 1;
            pageAuthorField.setText(String.valueOf(pageAuthor+1));
            searchInDBAndLoadInTableViewAuthor(authorName,pageAuthor);
        }
    }

    public void searchInDBAndLoadInTableViewReportedRecipe(String nameToSearch, Integer pageNumberReportedRecipe){
        tableReportedRecipe.resetObservableArrayList();
        ReportedRecipeService.getListReportedRecipes(nameToSearch,pageNumberReportedRecipe*10,10).forEach(reportedRecipe ->
                tableReportedRecipe.addToObservableArrayList(
                        new RowReportedRecipe(reportedRecipe.getName(), reportedRecipe.getAuthorName(),reportedRecipe.getReporterName(),
                                reportedRecipe.getDateReporting(),new ImageView(reportedRecipe.getImage()))));
        tableReportedRecipe.setItems();
    }
    public void onFindReportedRecipeClick(ActionEvent actionEvent ) {
        reportedRecipeName = reportedRecipeToSearchTextField.getText();
        if(reportedRecipeName.isBlank()) reportedRecipeName =  null;
        pageReportedRecipe =  0;
        searchInDBAndLoadInTableViewReportedRecipe(reportedRecipeName, pageReportedRecipe);
    }
    public void onNextPageReportedRecipeClick(ActionEvent actionEvent) {
        pageReportedRecipe = pageReportedRecipe + 1;
        pageReportedRecipeField.setText(String.valueOf(pageReportedRecipe+1));
        searchInDBAndLoadInTableViewReportedRecipe(reportedRecipeName,pageReportedRecipe);
    }
    public void onPreviousReportedRecipePageClick(ActionEvent actionEvent) {
        if(pageReportedRecipe>=1){
            pageReportedRecipe = pageReportedRecipe - 1;
            pageReportedRecipeField.setText(String.valueOf(pageReportedRecipe+1));
            searchInDBAndLoadInTableViewReportedRecipe(reportedRecipeName,pageReportedRecipe);
        }
    }
}
