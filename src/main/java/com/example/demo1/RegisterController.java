package com.example.demo1;

import com.mongodb.MongoException;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    public AnchorPane anchorPane;
    @FXML
    private TextField insertedName;
    @FXML
    private TextField insertedPassword;
    @FXML
    private Label registerText;
    @FXML
    private Label avatarLabel;
    private ImageView avatar;
    private ArrayList avatarsAvailable;
    private int avatarIndexForDatabase = 1;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        avatarsAvailable = new ArrayList<>();
        avatar = new ImageView();
        avatar.setImage(Configuration.AVATAR.get(0));
        avatar.setX(avatarLabel.getLayoutX());
        avatar.setY(avatarLabel.getLayoutY() + 20);
        avatar.setFitHeight(100);
        avatar.setFitWidth(100);
        anchorPane.getChildren().add(avatar);
        for (int i = 0; i < 8; i++) {
            ImageView temp = new ImageView();
            temp.setImage(Configuration.AVATAR.get(i));
            temp.setId(String.valueOf(i+1));
            temp.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { //evento per il click sull'immagine
                System.out.println(temp.getId());
                DataSingleton.getInstance().setAvatar(Integer.parseInt(temp.getId()) - 1);
                avatarIndexForDatabase = Integer.parseInt(temp.getId()) - 1;
                int avatarIndex = anchorPane.getChildren().indexOf(avatar);
                avatar.setImage(Configuration.AVATAR.get(Integer.parseInt(temp.getId()) - 1));
                avatar.setX(avatarLabel.getLayoutX());
                avatar.setY(avatarLabel.getLayoutY() + 20);
                avatar.setFitHeight(100);
                avatar.setFitWidth(100);
                anchorPane.getChildren().set(avatarIndex,avatar);
            });
            temp.setFitWidth(70);
            temp.setFitHeight(70);
            if (i >= 4) {
                temp.setY(avatarLabel.getLayoutY() + 170);
                temp.setX(avatarLabel.getLayoutX() - 50 + 55*(i-4));
            }
            else {
                temp.setY(avatarLabel.getLayoutY() + 100);
                temp.setX(avatarLabel.getLayoutX() - 50 + 55 * i);
            }
            avatarsAvailable.add(i,temp);
        }
        anchorPane.getChildren().addAll(avatarsAvailable);
    }
    public void onRegisterClick(ActionEvent actionEvent) {
        String name = insertedName.getText();
        String password = insertedPassword.getText();
        String uri = Configuration.MONGODB_URL;
        Label warningText = new Label();
        warningText.setLayoutX(insertedPassword.getLayoutX() - 100);
        warningText.setLayoutY(insertedPassword.getLayoutY() + 100);
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB); //da scegliere il nome uguale per tutti
            MongoCollection<Document> collection;
            if (DataSingleton.getInstance().getTypeOfUser().equals("author")) collection = database.getCollection(Configuration.MONGODB_AUTHOR);
            else {
                collection = database.getCollection(Configuration.MONGODB_MODERATOR);
            }
            Bson filter = Filters.eq(DataSingleton.getInstance().getTypeOfUser() + "Name", name);
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
                if (DataSingleton.getInstance().getTypeOfUser().equals("author")) {
                    try {
                        collection.insertOne(new Document()
                                .append("_id", new ObjectId())
                                .append("authorName", name)
                                .append("password", password)
                                .append("promotion", 0)
                                .append("image", avatarIndexForDatabase));
                    }
                    catch (MongoException me) {
                        System.err.println("Unable to insert due to an error: " + me);
                    }
                }
                else {
                    try {
                        collection.insertOne(new Document()
                                .append("_id", new ObjectId())
                                .append("moderatorName", name)
                                .append("password", password));
                    }
                    catch (MongoException me) {
                        System.err.println("Unable to insert due to an error: " + me);
                    }
                }
                DataSingleton.getInstance().setAuthorName(name);
                DataSingleton.getInstance().setPassword(password);
                //visto che la registrazione è andata bene vado subito alla schermata di loggato
                if (DataSingleton.getInstance().getTypeOfUser().equals("author")) cambiaSchermata(actionEvent,"Loggato.fxml");
                else cambiaSchermata(actionEvent,"Moderator.fxml");
            }
        }
        catch (IOException e) {
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
