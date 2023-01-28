package com.example.demo1;

import com.example.demo1.gui.Utils;
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
        Utils.changeScene(actionEvent,"Login.fxml");
    }

    public void onModeratorClick(ActionEvent actionEvent) throws IOException {
        Utils.changeScene(actionEvent,"Moderator.fxml");
    }
}
