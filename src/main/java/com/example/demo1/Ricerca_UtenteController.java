package com.example.demo1;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Ricerca_UtenteController implements Initializable {
    @FXML
    private Button cercaUtenteButton;
    @FXML
    private TextField utenteCercato;
    private Stage stage;
    public AnchorPane anchorPane;

    @FXML
    public void onLogoutClick(ActionEvent actionEvent) throws IOException {
        String nomeSchermata = "hello-view.fxml";
        cambiaSchermata(actionEvent,nomeSchermata);
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String uri = "mongodb://localhost:27017";
        List<Document> listaAuthors = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("test"); //da scegliere il nome uguale per tutti
            MongoCollection<Document> collection = database.getCollection("author");

            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext())
                listaAuthors.add(cursor.next());
        }

        for (int i = 0; i < listaAuthors.size() - 220000; i++)
        {
            setAuthorLabels(listaAuthors,i);
        }
    }

    public void setAuthorLabels(List<Document> listaAuthors,int i) {
        int documentSize = listaAuthors.get(0).size() - 2;
        List<String> listaLabelNames = new ArrayList<>();
        listaLabelNames.add("authorId");
        listaLabelNames.add("authorName");

        double copiaStartingX = cercaUtenteButton.getLayoutX() - 200;
        for (int j = 0;j < documentSize; j++) {
            Label currentLabel = new Label();
            Object valore = listaAuthors.get(i).get(listaLabelNames.get(j));
            currentLabel.setText((String.valueOf(valore)));
            currentLabel.setLayoutX(copiaStartingX + j*10);
            currentLabel.setLayoutY(cercaUtenteButton.getLayoutY() + 50 + i * 10);
            currentLabel.setMaxWidth(50);
            copiaStartingX += currentLabel.getMaxWidth();
            anchorPane.getChildren().add(currentLabel);
        }
    }

    public void onCercaUtenteClick(ActionEvent actionEvent) {
        String uri = "mongodb://localhost:27017";
        List<Document> listaAuthors = new ArrayList<>();
        System.out.println(999);
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("test"); //da scegliere il nome uguale per tutti
            MongoCollection<Document> collectionAuthor = database.getCollection("author");

            Bson filterAuthor = Filters.regex("authorName", "^" + utenteCercato.getText());
            MongoCursor<Document> cursorAuthor = collectionAuthor.find(filterAuthor).iterator();

            while (cursorAuthor.hasNext())
                listaAuthors.add(cursorAuthor.next());

            for (int i = 0; i < listaAuthors.size(); i++) {
                setReportedLabels(listaAuthors, i);
            }
        }
    }

    public void setReportedLabels(List<Document> listaAuthors,int i) {
        int documentSize = listaAuthors.get(0).size() - 2;
        List<String> listaLabelNames = new ArrayList<>();
        listaLabelNames.add("authorId");
        listaLabelNames.add("authorName");

        double copiaStartingX = cercaUtenteButton.getLayoutX();
        for (int j = 0;j < documentSize; j++) {
            Label currentLabel = new Label();
            Object valore = listaAuthors.get(i).get(listaLabelNames.get(j));
            currentLabel.setText((String.valueOf(valore)));
            currentLabel.setLayoutX(copiaStartingX + j*10);
            currentLabel.setLayoutY(cercaUtenteButton.getLayoutY() + 50 + i * 10);
            currentLabel.setMaxWidth(50);
            copiaStartingX += currentLabel.getMaxWidth();
            anchorPane.getChildren().add(currentLabel);
        }
    }

    public void cambiaSchermata(ActionEvent actionEvent,String nomeSchermata) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(nomeSchermata));
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setScene(scene);
        stage.show();
    }
}
