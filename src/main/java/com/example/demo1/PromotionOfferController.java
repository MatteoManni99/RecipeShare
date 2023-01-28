package com.example.demo1;

import com.example.demo1.service.AuthorService;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static com.mongodb.client.model.Aggregates.*;

public class PromotionOfferController implements Initializable {

    public AnchorPane anchorPane;

    public void onLogoutClick(ActionEvent actionEvent) throws IOException {
        String nomeSchermata = "hello-view.fxml";
        cambiaSchermata(actionEvent, nomeSchermata);
    }

    public void cambiaSchermata(ActionEvent actionEvent, String nomeSchermata) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(nomeSchermata));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void onRejectPromotionClick(ActionEvent actionEvent) {
        AuthorService.updatePromotion(DataSingleton.getInstance().getAuthorName(),0);
        /*try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collectionAuthor = database.getCollection(Configuration.MONGODB_AUTHOR);
            Document query = new Document().append("authorName", DataSingleton.getInstance().getAuthorName());
            Bson updates = Updates.combine(
                    Updates.set("promotion", 0)
            );
            UpdateOptions options = new UpdateOptions().upsert(true);
            try {
                UpdateResult result = collectionAuthor.updateOne(query, updates, options);
                System.out.println("Modified document count: " + result.getModifiedCount());
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }*/
        try {
            DataSingleton.getInstance().setTypeOfUser("author");
            cambiaSchermata(actionEvent, "Loggato.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onAcceptPromotionClick(ActionEvent actionEvent) {
        AuthorService.updatePromotion(DataSingleton.getInstance().getAuthorName(), 2);
        /*try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collectionAuthor = database.getCollection(Configuration.MONGODB_AUTHOR);
            Document query = new Document().append("authorName", DataSingleton.getInstance().getAuthorName());
            Bson updates = Updates.combine(
                    Updates.set("promotion", 2)
            );
            UpdateOptions options = new UpdateOptions().upsert(true);
            try {
                UpdateResult result = collectionAuthor.updateOne(query, updates, options);
                System.out.println("Modified document count: " + result.getModifiedCount());
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        }*/
        try {
            DataSingleton.getInstance().setTypeOfUser("moderator");
            cambiaSchermata(actionEvent, "Register.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

