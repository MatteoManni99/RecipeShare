package com.example.demo1.gui;

import com.example.demo1.HelloApplication;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Utils {

    public static void changeScene(Event actionEvent, String nameScene){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(nameScene));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    public static TableCell<RowTableView, ?>  findCell(MouseEvent event, TableView<RowTableView>  table) { //metodo chiamato dall'evento
        Node node = event.getPickResult().getIntersectedNode();
        // go up in node hierarchy until a cell is found or we can be sure no cell was clicked
        while (node != table && !(node instanceof TableCell)) {
            node = node.getParent();
        }
        return node instanceof TableCell ? (TableCell<RowTableView, ?>) node : null;
    }
}
