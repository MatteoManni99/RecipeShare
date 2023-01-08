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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onRegisterClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Register.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public void onLoginClick(ActionEvent actionEvent) {
        /*dati di un utente che esiste nella collection se volete usarli per prova:
            NAME = Carmen
            PASSWORD = 6czYhW4F
        */
        String name = insertedName.getText();
        String password = insertedPassword.getText();
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("RecipeShare"); //da scegliere il nome uguale per tutti
            MongoCollection<Document> collectionAuthor = database.getCollection("author");
            login(actionEvent, collectionAuthor, "authorName", name, password, "Loggato.fxml");
            MongoCollection<Document> collectionModerator = database.getCollection("moderator");
            login(actionEvent, collectionModerator, "moderatorName", name, password, "Moderator.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void login(ActionEvent actionEvent, MongoCollection<Document> collection, String username, String name, String password, String fxml) throws IOException {
        Bson filterModerator = Filters.and(
                Filters.eq(username, name),
                Filters.eq("password", password));
        MongoCursor<Document> cursor = collection.find(filterModerator).iterator();
        if (cursor.hasNext()) {
            System.out.println("OKAY");
            //cambio pagina
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml));
            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            stage.setTitle("Hello "+ name);
            stage.setScene(scene);
            stage.show();
        }
        else System.out.println("NO");
    }
}