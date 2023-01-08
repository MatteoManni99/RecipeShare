package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LoggatoController {
    @FXML
    private Label welcomeText;
    private Stage stage;
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

    public void cambiaSchermata(ActionEvent actionEvent,String nomeSchermata) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(nomeSchermata));
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setScene(scene);
        stage.show();
    }
}