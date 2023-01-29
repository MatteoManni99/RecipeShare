package com.example.demo1.gui;

import com.example.demo1.Configuration;
import com.example.demo1.service.AuthorService;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    public AnchorPane anchorPane;
    public ImageView image1;
    public ImageView image2;
    public ImageView image3;
    public ImageView image4;
    public ImageView image5;
    public ImageView image6;
    public ImageView image7;
    public ImageView image8;
    public ImageView selectedImage;
    public TextField password;
    public TextField name;
    public Label warningLabel;

    private Integer selectedImageNumber;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        warningLabel.setText("");
        selectedImageNumber = null;
    }
    private boolean checkIfEmpty(String string){
        return (string.isEmpty() || string.isBlank());
    }
    public void onRegisterClick(ActionEvent actionEvent) {
        if(selectedImageNumber == null){
            warningLabel.setText("Select an Avatar to register...");
        } else if (checkIfEmpty(name.getText()) || checkIfEmpty(password.getText())) {
            warningLabel.setText("Insert valid Username and Password...");
        } else {
            if (AuthorService.register(name.getText(), password.getText(), selectedImageNumber-1, 0)){
                warningLabel.setText("Successfully Registered");
            }else
                warningLabel.setText("Username not Available");
        }
    }
    public void onBackToLoginClick(ActionEvent actionEvent) {
        Utils.changeScene(actionEvent,"Login.fxml");
    }

    private void setSelectedImage(Integer imageNumber){
        selectedImageNumber = imageNumber;
        selectedImage.setImage(Configuration.AVATAR.get(imageNumber-1));
    }
    public void onMouseClickImage1(MouseEvent mouseEvent) {
        setSelectedImage(1);
    }
    public void onMouseClickImage2(MouseEvent mouseEvent) {
        setSelectedImage(2);
    }
    public void onMouseClickImage3(MouseEvent mouseEvent) {
        setSelectedImage(3);
    }
    public void onMouseClickImage4(MouseEvent mouseEvent) {
        setSelectedImage(4);
    }
    public void onMouseClickImage5(MouseEvent mouseEvent) {
        setSelectedImage(5);
    }
    public void onMouseClickImage6(MouseEvent mouseEvent) {
        setSelectedImage(6);
    }
    public void onMouseClickImage7(MouseEvent mouseEvent) {
        setSelectedImage(7);
    }
    public void onMouseClickImage8(MouseEvent mouseEvent) {
        setSelectedImage(8);
    }
}
