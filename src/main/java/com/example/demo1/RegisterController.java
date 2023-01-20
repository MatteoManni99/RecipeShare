package com.example.demo1;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.IOException;

public class RegisterController {

    public AnchorPane anchorPane;
    @FXML
    private TextField insertedName;
    @FXML
    private TextField insertedPassword;
    private Stage stage;

    public void onRegisterClick(ActionEvent actionEvent) {
        /*HO REGISTRATO 2 nuovi utenti con successo:
            primo utente:
                NAME = prova
                PASSWORD = prova
            secondo utente:
                NAME = prova1
                PASSWORD = prova1
        */
        String name = insertedName.getText();
        String password = insertedPassword.getText();
        String uri = Configuration.MONGODB_URL;
        Label warningText = new Label();
        warningText.setLayoutX(insertedPassword.getLayoutX() - 100);
        warningText.setLayoutY(insertedPassword.getLayoutY() + 100);
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB); //da scegliere il nome uguale per tutti
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_AUTHOR);
            Bson filter = Filters.eq("authorName", name);
            MongoCursor<Document> cursor = collection.find(filter).iterator();
            //finché non si trova un nickname valido si deve ritentare
            if (cursor.hasNext()) {
                System.out.println("NICKNAME GIA USATO");
                warningText.setText("ERRORE: Username già in uso, cambia Username per registrarti");
                anchorPane.getChildren().add(warningText);
            }
            else {
                System.out.println("NICKNAME VALIDO");
                warningText.setText("SUCCESSO: ti sei registrato correttamente");
                anchorPane.getChildren().add(warningText);
                try {
                    collection.insertOne(new Document()
                            .append("_id", new ObjectId())
                            .append("authorName", name)
                            .append("password", password)
                            .append("promotion", 0) // DA CAMBIARE
                            .append("image", 1)); // DA CAMBIARE
                }
                catch (MongoException me) {
                    System.err.println("Unable to insert due to an error: " + me);
                }
                //visto che la registrazione è andata bene vado subito alla schermata di loggato
                String nomeSchermata = "Loggato.fxml";
                cambiaSchermata(actionEvent,nomeSchermata);
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
}
