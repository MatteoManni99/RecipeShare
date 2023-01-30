package com.example.demo1.gui;

import com.example.demo1.Configuration;
import com.example.demo1.model.Author;
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

    private String authorNameClicked;


    private TableViewAuthor tabella;
    @FXML
    private AnchorPane anchorPane;
    private TableViewAuthor tableAuthor = new TableViewAuthor();
    //private TableViewReportedRecipe tableReportedRecipe = new TableViewReportedRecipe();
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
    public void onViewAnalyticsClick() {
        ReportedRecipeService.onHighestRatioQueryClick().forEach((key, value) -> System.out.println(key + ":" + value));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startingX = recipeText.getLayoutX();
        //List<ReportedRecipe> listaReportedRecipes = ReportedRecipeMongoDAO.getListReportedRecipes();
        /*try (MongoClient mongoClient = MongoClients.create(uri)) { //fatto in DAO
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB); //da scegliere il nome uguale per tutti
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_REPORTED_RECIPE);

            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext())
                listaReportedRecipes.add(cursor.next());
        }
        for (int i = 0; i < listaReportedRecipes.size(); i++)
            setReportedLabels(listaReportedRecipes,i);
        */

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
        createTableView(tableAuthor/*,tableReportedRecipe*/);
    }

    public void createTableView (TableViewAuthor TableViewObject/*,TableViewReportedRecipe tableReportedRecipe*/) {
        TableViewObject.initializeTableView();
        searchInDBAndLoadInTableView(authorName,pageNumber);
        TableViewObject.setTableWithPromotion();
        setEventForTableCells();
        anchorPane.getChildren().add(TableViewObject.getTabellaDB());
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

    public void searchInDBAndLoadInTableView(String nameToSearch, Integer pageNumber){
        List<Author> listAuthorsSearched = AuthorService.searchAuthors(nameToSearch,10*pageNumber,10);
        tableAuthor.resetObservableArrayList();
        listAuthorsSearched.forEach(author ->
                tableAuthor.addToObservableArrayList(
                        new RowAuthor(author.getName(), author.getPromotion(),
                                new ImageView(Configuration.AVATAR.get(author.getImage()-1)))));
        tableAuthor.setItems();
        /*Document authorDoc;
        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_AUTHOR);
            MongoCursor<Document> cursor;
            //Bson filter = Filters.regex("Name", "^(?)" + nameToSearch); //da togliere era il vecchio filtro
            Bson filter = new Document("Name",new Document("$regex",nameToSearch).append("$options","i"));
            Bson match = match(filter);
            Bson project = project(new Document("authorName",1).append("promotion",1).append("image", 1));
            if(nameToSearch == null){
                cursor = collection.aggregate(Arrays.asList(skip(10*pageNumber),limit(10),project)).iterator();
            }else{
                cursor = collection.aggregate(Arrays.asList(match, skip(10*pageNumber),limit(10),project)).iterator();
                System.out.println(nameToSearch);
            }
        //tableAuthor.resetObservableArrayList();
        while (cursor.hasNext()){
                authorDoc = cursor.next();
                AuthorTableView author = new AuthorTableView(authorDoc.getString("authorName"),
                        authorDoc.getInteger("promotion"),
                        new ClassTableAuthor.CustomImageAuthor(new ImageView(Configuration.AVATAR.get(authorDoc.getInteger("image") - 1))).getImage());
                tableAuthor.addToObservableArrayList(author);
            }
            tableAuthor.setItems();
        }*/
    }


    /*public void setReportedLabels(List<ReportedRecipe> listaReportedRecipes, int i) {
        //listaReportedRecipes = ReportedRecipeMongoDAO.f
        //int documentSize = listaReportedRecipes.get(0).size() - 1;
        List<String> listaLabelNames = new ArrayList<>();
        listaLabelNames.add("RecipeName");
        listaLabelNames.add("Image");
        listaLabelNames.add("AuthorName");
        listaLabelNames.add("ReporterName");
        double copiaStartingX = startingX - 200;
        for (int j = 0;j < documentSize; j++) {
            Label currentLabel = new Label();
            Object valore = listaReportedRecipes.get(i).get(listaLabelNames.get(j));
            currentLabel.setText((String.valueOf(valore)));
            currentLabel.setLayoutX(copiaStartingX + j*10);
            currentLabel.setLayoutY(recipeText.getLayoutY() + 50 + i * 10);
            currentLabel.setMaxWidth(50);
            copiaStartingX += currentLabel.getMaxWidth();
            anchorPane.getChildren().add(currentLabel);
        }
    }*/
    public void onFindAuthorClick(ActionEvent actionEvent ) throws IOException {
        nameToSearch = authorToSearchTextField.getText( );
        if(nameToSearch.isBlank()) nameToSearch =  null;
        pageNumber =  0;
        searchInDBAndLoadInTableView(nameToSearch, pageNumber);
    }
}
