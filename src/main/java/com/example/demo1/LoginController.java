package com.example.demo1;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

public class LoginController {
    @FXML
    private TextField insertedName;
    @FXML
    private TextField insertedPassword;
    private Stage stage;

    @FXML
    public void onLogoutClick(ActionEvent actionEvent) throws IOException {
        String nomeSchermata = "hello-view.fxml";
        cambiaSchermata(actionEvent,nomeSchermata);
    }

    @FXML
    public void onRegisterClick(ActionEvent actionEvent) throws IOException {
        String nomeSchermata = "Register.fxml";
        cambiaSchermata(actionEvent,nomeSchermata);
    }

    public void onLoginClick(ActionEvent actionEvent) {
        /*dati di un utente che esiste nella collection se volete usarli per prova:
            NAME = Carmen
            PASSWORD = 6czYhW4F
        */
        String nomePagina = null;
        String name = insertedName.getText();
        String password = insertedPassword.getText();
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("test"); //da scegliere il nome uguale per tutti
            MongoCollection<Document> collectionAuthor = database.getCollection("author");
            MongoCollection<Document> collectionModerator = database.getCollection("moderator");
            Bson filterAuthor = Filters.and(
                    Filters.eq("authorName", name),
                    Filters.eq("password", password));
            MongoCursor<Document> cursorAuthor = collectionAuthor.find(filterAuthor).iterator();
            Bson filterModerator = Filters.and(
                    Filters.eq("moderatorName", name),
                    Filters.eq("password", password));
            MongoCursor<Document> cursorModerator = collectionModerator.find(filterModerator).iterator();
            if (cursorAuthor.hasNext()) {
                System.out.println("TROVATO AUTHOR");
                nomePagina = "Loggato.fxml";
            }
            else {
                System.out.println("NON TROVATO AUTHOR");
                if (cursorModerator.hasNext()) {
                    System.out.println("TROVATO MODERATOR");
                    nomePagina = "Moderator.fxml";
                }
                else System.out.println("NON TROVATO MODERATOR");
            }
            if (nomePagina != null) {
                cambiaSchermata(actionEvent,nomePagina);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void cambiaSchermata(ActionEvent actionEvent,String nomeSchermata) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(nomeSchermata));
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    public void onTextClick(MouseEvent mouseEvent) {
        System.out.println("TESTO CLICKATO");
    }
}