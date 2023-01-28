package com.example.demo1;

import com.example.demo1.gui.Utils;
import com.example.demo1.model.Author;
import com.example.demo1.service.AuthorService;
import com.example.demo1.service.ModeratorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField insertedName;
    @FXML
    private TextField insertedPassword;

    @FXML
    public void onRegisterClick(ActionEvent actionEvent) throws IOException {
        DataSingleton.getInstance().setTypeOfUser("author");
        Utils.changeScene(actionEvent,"Register.fxml");
    }

    public void onLoginClick(ActionEvent actionEvent){
        String nomePagina = null;
        String name = insertedName.getText();
        String password = insertedPassword.getText();
        boolean existAuthor = AuthorService.login(name,password);
        boolean existModerator = ModeratorService.tryLogin(name, password);
        if (existAuthor) {
            Author currentAuthor = AuthorService.getAuthor(name);
            int avatarIndex = currentAuthor.getImage();
            DataSingleton.getInstance().setAvatar(avatarIndex);
            DataSingleton.getInstance().setAvatarIndex(avatarIndex);
            DataSingleton.getInstance().setAuthorPromotion(currentAuthor.getPromotion());
            DataSingleton.getInstance().setTypeOfUser("author");
            nomePagina = "Loggato.fxml";
        }
        else if (existModerator){
            DataSingleton.getInstance().setTypeOfUser("moderator");
            nomePagina = "Moderator.fxml";
        }
        if (nomePagina != null) {
            DataSingleton data = DataSingleton.getInstance();
            data.setAuthorName(name);
            data.setPassword(password);
            if (nomePagina.equals("Loggato.fxml") && DataSingleton.getInstance().getAuthorPromotion() == 1)
                Utils.changeScene(actionEvent,"PromotionOffer.fxml");
            else Utils.changeScene(actionEvent,nomePagina);
        }
    }
}