package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {
    @FXML
    private Label trovaRecipeText;
    private Stage stage;
    @FXML
    protected void onTrovaRecipeClick() {
        trovaRecipeText.setText("************");
    }
    @FXML
    public void onLoginClick(ActionEvent actionEvent) throws IOException {
        String nomeSchermata = "Login.fxml";
        cambiaSchermata(actionEvent,nomeSchermata);
    }

    public void onModeratorClick(ActionEvent actionEvent) throws IOException {
        String nomeSchermata = "Moderator.fxml";
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
