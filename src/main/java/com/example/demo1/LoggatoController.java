package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.mongodb.client.model.Projections.include;

public class LoggatoController implements Initializable{
    public AnchorPane anchorPane;
    @FXML
    private Label welcomeText;
    private Stage stage;
    private ClassFotTableView TableViewObject = new ClassFotTableView();
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
        updateTableView(TableViewObject,pageNumber);
    }
    @FXML
    public void onPreviousPageClick(){
        if(pageNumber>=1){
            pageNumber = pageNumber - 1;
            updateTableView(TableViewObject,pageNumber);
        }
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
        String uri = Configuration.MONGODB_URL;
        createTableView(TableViewObject);
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
        TableViewObject.caricaElementiTableViewDB();
        TableViewObject.setTabellaDB();
        anchorPane.getChildren().add(TableViewObject.getTabellaDB());
    }
    public void updateTableView(ClassFotTableView TableViewObject,Integer pageNumber){
        TableViewObject.uploadNewElementsTableViewDB(pageNumber);
    }
}