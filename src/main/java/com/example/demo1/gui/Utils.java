package com.example.demo1.gui;

import com.example.demo1.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Utils {

    public static void changeScene(ActionEvent actionEvent, String nameScene){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(nameScene));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    public static void changeScene(MouseEvent mouseEvent, String nameScene) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(nameScene));
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {throw new RuntimeException(e);}
    }
}
