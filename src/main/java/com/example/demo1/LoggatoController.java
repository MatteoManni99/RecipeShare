package com.example.demo1;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Projections.*;

public class LoggatoController implements Initializable{
    public AnchorPane anchorPane;
    @FXML
    private Label welcomeText;
    private Stage stage;
    private ClassFotTableView TableViewObject = new ClassFotTableView();
    @FXML
    private TextField nameToSearchTextField;

    private String nameToSearch = null;
    private Integer pageNumber = 0;
    @FXML
    public void onLogoutClick(ActionEvent actionEvent) throws IOException {
        String nomeSchermata = "hello-view.fxml";
        cambiaSchermata(actionEvent,nomeSchermata);
    }

    @FXML
    public void onCercaUtenteClick(ActionEvent actionEvent) throws IOException {
        String nomeSchermata = "Ricerca_Utente.fxml";
        cambiaSchermata(actionEvent,nomeSchermata);
    }

    @FXML
    public void onAnalyticsClick(ActionEvent actionEvent) throws IOException {
        String nomeSchermata = "LoggatoAnalytics.fxml";
        cambiaSchermata(actionEvent,nomeSchermata);
    }

    @FXML
    public void onProvaRecipe(ActionEvent actionEvent) throws IOException {
        String nomeSchermata = "Recipe.fxml";
        cambiaSchermata(actionEvent,nomeSchermata);
    }
    @FXML
    public void onNextPageClick(){
        pageNumber = pageNumber + 1;
        //updateTableView(TableViewObject,pageNumber);
        searchInDBAndLoadInTableView(nameToSearch,pageNumber);
    }
    @FXML
    public void onPreviousPageClick(){
        if(pageNumber>=1){
            pageNumber = pageNumber - 1;
            //updateTableView(TableViewObject,pageNumber);
            searchInDBAndLoadInTableView(nameToSearch,pageNumber);
        }
    }
    @FXML
    public void onFindRecipeByNameClick(){
        nameToSearch = nameToSearchTextField.getText();
        if(nameToSearch.isBlank()) nameToSearch = null;
        System.out.println(nameToSearch); //solo per debug sarà da togliere
        pageNumber = 0;
        searchInDBAndLoadInTableView(nameToSearch,pageNumber);
    }
    public void searchInDBAndLoadInTableView(String nameToSearch, Integer pageNumber){
        Document recipeDoc;
        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_RECIPE);
            MongoCursor<Document> cursor;
            //filters
            Bson filter = Filters.regex("Name", "^(?)" + nameToSearch);
            Bson match = match(filter);
            Bson project = project(new Document("RecipeId",1).append("Name",1).append("AuthorId",1).append("AuthorName",1)
                    .append("Images", new Document("$first","$Images")));

            if(nameToSearch == null){
                cursor = collection.aggregate(Arrays.asList(skip(10*pageNumber),limit(10),project)).iterator();
                System.out.println("null");
            }else{
                cursor = collection.aggregate(Arrays.asList(match,skip(10*pageNumber),limit(10),project)).iterator();
                System.out.println(nameToSearch);
            }
            TableViewObject.resetObservableArrayList();
            while (cursor.hasNext()){
                recipeDoc = cursor.next();
                Recipe recipe = new Recipe(recipeDoc.getInteger(("RecipeId")),recipeDoc.getString("Name"),recipeDoc.getInteger(("AuthorId")),
                        recipeDoc.getString("AuthorName"),new ClassFotTableView.CustomImage(new ImageView(recipeDoc.getString("Images"))).getImage());
                TableViewObject.addToObservableArrayList(recipe);
            }
            TableViewObject.setItems();
        }
    }

    //alla fine printDocuments sarà inutile, da togliere in ultimo
    private static Consumer<Document> printDocuments() {
        return doc -> System.out.println(doc.toJson());
    }

    public void cambiaSchermata(ActionEvent actionEvent,String nomeSchermata) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(nomeSchermata));
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createTableView(TableViewObject);
        //String uri = Configuration.MONGODB_URL;
        /*ObservableList<String> recipesIdList = FXCollections.observableArrayList();
        Integer count = 0; //per fare debug (da togliere)
        ImageView imageView = new ImageView();
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("RecipeShare");
            MongoCollection<Document> collectionRecipe = database.getCollection("recipe");
            //Bson project = project(include("RecipeId","Name","AggregatedRating","Images")); //da rivedere i campi in base a quello che vogliamo far visualizzare
            Bson project = project(new Document("Images",new Document("$first","$Images"))
                    .append("Name",1).append("RecipeId",1));
            Bson limit = limit(10);
            for (Document document : collectionRecipe.aggregate(Arrays.asList(limit, project))) {
                System.out.println(count); //per fare debug (da togliere)
                count += 1; //per fare debug (da togliere)
                recipesIdList.add(String.valueOf(document.getInteger("RecipeId")));
            }
            RecipesListView.setItems(recipesIdList);
        }*/
    }

    public void createTableView (ClassFotTableView TableViewObject) {
        TableViewObject.initializeTableView();
        searchInDBAndLoadInTableView(nameToSearch,pageNumber);
        TableViewObject.setEventForTableCells();
        TableViewObject.setTabellaDB();
        anchorPane.getChildren().add(TableViewObject.getTabellaDB());
    }

    //vecchia versione di searchInDBAndLoadInTableView
    /*
    public void updateTableView(ClassFotTableView TableViewObject,Integer pageNumber){
        TableViewObject.uploadElementsTableViewDB(pageNumber);
        TableViewObject.setItems();
    }*/
}