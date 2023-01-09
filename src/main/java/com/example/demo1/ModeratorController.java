package com.example.demo1;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.internal.operation.MapReduceWithInlineResultsOperation;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ModeratorController implements Initializable {
    @FXML
    private Label recipeText;

    private double startingX;
    @FXML
    private Label reviewText;
    private Stage stage;
    private Scene scene;
    @FXML
    private VBox vbox;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    public void onLogoutClick(ActionEvent actionEvent) throws IOException {
        String nomeSchermata = "hello-view.fxml";
        cambiaSchermata(actionEvent,nomeSchermata);
    }

    public void highestRatioQuery() {
        /*PSEUDO CODICE
        String uri = "mongodb://localhost:27017";
        int numeroDiTopAutori = 5;
        List<int> listaQuantitaRecensioniAggiuntePerAutore = new ArrayList<>();
        List<int> listaQuantitaRecensioniReportatePerAutore = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("test"); //da scegliere il nome uguale per tutti
            MongoCollection<Document> collection = database.getCollection("author");

            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext())
            {
                int numeroDiRecipe = recipesAdded.size();
                listaQuantitaRecensioniAggiuntePerAutore.append(numeroDiRecipe); //sfrutto la ridondanza
            }

            //ora ho il numero di recensione fatte da ogni autore nell'ordine con cui compaiono nel database

            //ora calcolo con un' altra lista il numero di recensioni reportate per ogni autore, nello specifico
              il numero reportato + 1 per impedire errori nelle divisioni con autori che avrebbero 0 recensioni reportate

            //a questo punto creo una nuova lista di double in cui metto i risultati delle divisioni tra l'elemento 0 della prima
              lista e l'elemento 0 della seconda lista, elemento 1 della prima e della seconda e così via

            //creo un'ulteriore lista che è la copia della lista ottenuta prima, poi faccio il sort dei valori della lista così
              da avere in testa i primi "numeroDiTopAutori" elementi, infine prendo i valori di questi primi elementi
              e guardo a che indice si trovano nella lista copia e così so il loro Id e posso prendere i documenti interi
              dal database
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String uri = "mongodb://localhost:27017";
        List<Document> listaReportedRecipes = new ArrayList<>();
        startingX = recipeText.getLayoutX();

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("RecipeShare"); //da scegliere il nome uguale per tutti
            MongoCollection<Document> collection = database.getCollection("reportedRecipes");

            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext())
                listaReportedRecipes.add(cursor.next());
        }

        for (int i = 0; i < listaReportedRecipes.size(); i++)
        {
            setReportedLabels(listaReportedRecipes,i);
        }
    }

    public void setReportedLabels(List<Document> listaReportedRecipes,int i) {
        int documentSize = listaReportedRecipes.get(0).size() - 1;
        List<String> listaLabelNames = new ArrayList<>();
        listaLabelNames.add("RecipeId");
        listaLabelNames.add("RecipeName");
        listaLabelNames.add("Images");
        listaLabelNames.add("AuthorId");
        listaLabelNames.add("AuthorName");
        listaLabelNames.add("ReporterId");
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
    }
    public void onBrowseAuthorsClick(ActionEvent actionEvent) throws IOException {
        String nomeSchermata = "Ricerca_Utente.fxml";
        cambiaSchermata(actionEvent,nomeSchermata);
    }

    public void cambiaSchermata(ActionEvent actionEvent,String nomeSchermata) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(nomeSchermata));
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setScene(scene);
        stage.show();
    }
}
