package com.example.demo1;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModeratorController implements Initializable {
    @FXML
    private Label recipeText;

    @FXML
    private Label reviewText;
    private Stage stage;
    private Scene scene;
    @FXML
    private VBox vbox;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    public void onLogoutClick(ActionEvent actionEvent) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(fxmlLoader.load(), 320, 240);
            recipeText = (Label) scene.lookup("recipeText");
            reviewText = (Label) scene.lookup("reviewText");
            vbox = (VBox) scene.lookup("vbox");
            anchorPane = (AnchorPane) scene.lookup("anchorPane");
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Label reportedRecipeLabel;
        Label reportedReviewLabel;
        for (int i = 0; i < 4; i++)
        {
            reportedRecipeLabel = new Label("*****");
            reportedRecipeLabel.setLayoutX(recipeText.getLayoutX() + 50);
            reportedRecipeLabel.setLayoutY(recipeText.getLayoutY() + 50 + i*10);
            anchorPane.getChildren().add(reportedRecipeLabel);

            reportedReviewLabel = new Label("*****");
            reportedReviewLabel.setLayoutX(reviewText.getLayoutX() + 50);
            reportedReviewLabel.setLayoutY(reviewText.getLayoutY() + 50 + i*10);
            anchorPane.getChildren().add(reportedReviewLabel);
        }
    }
}
