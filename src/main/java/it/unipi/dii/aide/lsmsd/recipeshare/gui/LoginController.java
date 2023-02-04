package it.unipi.dii.aide.lsmsd.recipeshare.gui;

import it.unipi.dii.aide.lsmsd.recipeshare.model.Author;
import it.unipi.dii.aide.lsmsd.recipeshare.service.AuthorService;
import it.unipi.dii.aide.lsmsd.recipeshare.service.ModeratorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField insertedName;
    @FXML
    private PasswordField insertedPassword;

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
            nomePagina = "HomeAuthor.fxml";
        }
        else if (existModerator){
            DataSingleton.getInstance().setTypeOfUser("moderator");
            nomePagina = "HomeModerator.fxml";
        }
        if (nomePagina != null) {
            DataSingleton data = DataSingleton.getInstance();
            data.setAuthorName(name);
            data.setPassword(password);
            if (nomePagina.equals("HomeAuthor.fxml") && DataSingleton.getInstance().getAuthorPromotion() == 1)
                Utils.changeScene(actionEvent,"PromotionOffer.fxml");
            else Utils.changeScene(actionEvent,nomePagina);
        }
    }
}