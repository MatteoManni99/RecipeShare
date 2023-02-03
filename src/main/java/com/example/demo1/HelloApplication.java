package com.example.demo1;

import com.example.demo1.dao.mongo.ReportedRecipeMongoDAO;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        MongoClientSettings settingAP = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb://10.1.1.8:27019"))
                .readPreference(ReadPreference.secondaryPreferred())
                .retryWrites(true)
                .writeConcern(WriteConcern.W1).build(); //W3 garantirebbe la massima consistenza perché appena cade un nodo non viene fatta la write
        MongoClient clientSettingsAP = MongoClients.create(settingAP);  //mettendo noi W1 ci garantiamo che basta un solo nodo UP per far passare la write, accettando
        System.out.println(clientSettingsAP.getDatabase("RecipeShare").getCollection("author") //l'inconsistenza momentanea dei dati per avere aggiornamenti successivi
                .find(new Document("authorName","Malarkey Test")).first().getString("authorName"));

        MongoClientSettings settingCP = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb://10.1.1.8:27019"))
                .readPreference(ReadPreference.primary())
                .retryWrites(true)
                .writeConcern(WriteConcern.MAJORITY).build(); //
        MongoClient clientSettingsCP = MongoClients.create(settingCP);


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}