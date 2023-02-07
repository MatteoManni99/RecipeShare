package it.unipi.dii.aide.lsmsd.recipeshare.gui;

import it.unipi.dii.aide.lsmsd.recipeshare.service.AuthorService;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PromotionOfferController implements Initializable {

    public AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void onRejectPromotionClick(ActionEvent actionEvent) {
        AuthorService.updatePromotion(DataSingleton.getInstance().getAuthorName(),0);
        DataSingleton.getInstance().setTypeOfUser("author");
        Utils.changeScene(actionEvent,"HomeAuthor.fxml");
    }

    public void onAcceptPromotionClick(ActionEvent actionEvent) {

        DataSingleton.getInstance().setTypeOfUser("moderator");
        Utils.changeScene(actionEvent,"RegisterAsModerator.fxml");
    }
}

