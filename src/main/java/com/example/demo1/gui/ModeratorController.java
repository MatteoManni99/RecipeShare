package com.example.demo1.gui;

import com.example.demo1.Configuration;
import com.example.demo1.model.Author;
import com.example.demo1.model.ReportedRecipe;
import com.example.demo1.service.AuthorService;
import com.example.demo1.service.ReportedRecipeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
    private TableViewAuthor tableAuthor = new TableViewAuthor();
    private TableViewReportedRecipe tableReportedRecipe = new TableViewReportedRecipe();
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

        //questa parte sotto è quella che setta il Button per la promozione
        if (DataSingleton.getInstance().getTypeOfUser().equals("moderator")) {
            Button promoteAuthorButton = new Button("PROMOTE AUTHOR");
            promoteAuthorButton.setLayoutX(64);
            promoteAuthorButton.setLayoutY(120);
            promoteAuthorButton.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
                AuthorService.updatePromotion(authorNameClicked,1);
                authorNameClicked = null;
                /*try (MongoClient mongoClient = MongoClients.create(uri)) {
                    MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB); //da scegliere il nome uguale per tutti
                    MongoCollection<Document> collectionAuthor = database.getCollection(Configuration.MONGODB_AUTHOR);
                    Document query = new Document().append("authorName", authorNameClicked);
                    Bson updates = Updates.combine(
                            Updates.set("promotion",1)
                    );
                    UpdateOptions options = new UpdateOptions().upsert(true);
                    try {
                        UpdateResult result = collectionAuthor.updateOne(query, updates, options);
                        System.out.println("Modified document count: " + result.getModifiedCount());
                    } catch (MongoException me) {
                        System.err.println("Unable to update due to an error: " + me);
                    }
                }*/
            });
            anchorPane.getChildren().add(promoteAuthorButton);
        }
        createTableView(tableAuthor,tableReportedRecipe);
    }

    public void createTableView (TableViewAuthor TableViewObject,TableViewReportedRecipe tableReportedRecipe) {
        TableViewObject.initializeTableView();
        tableReportedRecipe.initializeTableView();
        searchInDBAndLoadInTableView(authorName,pageNumber);
        searchInDBAndLoadInTableViewReportedRecipe(recipeName,pageNumberReportedRecipe);
        TableViewObject.setTableWithPromotion();
        tableReportedRecipe.setTabellaDB();
        tableReportedRecipe.setEventForTableCells();
        setEventForTableCells();
        anchorPane.getChildren().addAll(TableViewObject.getTabellaDB(),tableReportedRecipe.getTable());
    }

    public void setEventForTableCells() {
        tableAuthor.getTabellaDB().addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { //evento per il mouse clickato
                    TableCell cell = findCell(evt,tableAuthor.getTabellaDB());
                    if (cell != null && !cell.isEmpty()) {
                        if(cell.getTableColumn().getText().equals("Promotion")){
                            int rowIndex = cell.getIndex();
                            authorNameClicked = (String) tableAuthor.getTabellaDB().getColumns().get(1).getCellData(rowIndex);
                            cell.setText("1");
                        }
                        if(cell.getTableColumn().getText().equals("Name")) {
                            authorNameClicked = cell.getText();
                            DataSingleton.getInstance().setOtherAuthorName(authorNameClicked);
                            Utils.changeScene(evt,"Author.fxml");
                        }
                        evt.consume();
                    }
                }
        );
    }
    private static TableCell findCell(MouseEvent event, TableView table) { //metodo chiamato dall'evento
        Node node = event.getPickResult().getIntersectedNode();
        // go up in node hierarchy until a cell is found or we can be sure no cell was clicked
        while (node != table && !(node instanceof TableCell)) {
            node = node.getParent();
        }
        return node instanceof TableCell ? (TableCell) node : null;
    }

    public void searchInDBAndLoadInTableView(String nameToSearch, Integer pageNumber) {
        List<Author> listAuthorsSearched = AuthorService.searchAuthors(nameToSearch, 10 * pageNumber, 10);
        tableAuthor.resetObservableArrayList();
        listAuthorsSearched.forEach(author ->
                tableAuthor.addToObservableArrayList(
                        new RowAuthor(author.getName(), author.getPromotion(),
                                new ImageView(Configuration.AVATAR.get(author.getImage() - 1)))));
        tableAuthor.setItems();
    }

    public void searchInDBAndLoadInTableViewReportedRecipe(String nameToSearch, Integer pageNumber){
        List<ReportedRecipe> listReportedRecipesSearched = ReportedRecipeService.getListReportedRecipes();
        tableReportedRecipe.resetObservableArrayList();
        listReportedRecipesSearched.forEach(reportedRecipe ->
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
