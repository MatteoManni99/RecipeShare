package it.unipi.dii.aide.lsmsd.recipeshare.gui;

import it.unipi.dii.aide.lsmsd.recipeshare.Configuration;
import it.unipi.dii.aide.lsmsd.recipeshare.service.AuthorService;
import it.unipi.dii.aide.lsmsd.recipeshare.service.ModeratorService;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterAsModeratorController implements Initializable {

    public AnchorPane anchorPane;

    public TextField password;

    public TextField name;
    public Label warningLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        warningLabel.setText("");
    }
    private boolean checkIfEmpty(String string){
        return (string.isEmpty() || string.isBlank());
    }
    public void onRegisterClick(ActionEvent actionEvent) {

        if (checkIfEmpty(name.getText()) || checkIfEmpty(password.getText())) {
            warningLabel.setText("Insert valid Username and Password...");
        } else {
            if (ModeratorService.checkRegistration(name.getText(), password.getText())){
                warningLabel.setText("Successfully Registered");
                name.setEditable(false);
                password.setEditable(false);
                AuthorService.updatePromotion(DataSingleton.getInstance().getAuthorName(), 2);
            }else
                warningLabel.setText("Username not Available");
        }
    }
    public void onBackToLoginClick(ActionEvent actionEvent) {
        Utils.changeScene(actionEvent,"Login.fxml");
    }

}
