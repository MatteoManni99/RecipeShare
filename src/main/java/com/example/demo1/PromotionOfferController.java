package com.example.demo1;

import com.example.demo1.gui.Utils;
import com.example.demo1.service.AuthorService;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static com.mongodb.client.model.Aggregates.*;

public class PromotionOfferController implements Initializable {

    public AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void onRejectPromotionClick(ActionEvent actionEvent) {
        AuthorService.updatePromotion(DataSingleton.getInstance().getAuthorName(),0);
        DataSingleton.getInstance().setTypeOfUser("author");
        Utils.changeScene(actionEvent,"Loggato.fxml");
    }

    public void onAcceptPromotionClick(ActionEvent actionEvent) {
        AuthorService.updatePromotion(DataSingleton.getInstance().getAuthorName(), 2);
        DataSingleton.getInstance().setTypeOfUser("moderator");
        Utils.changeScene(actionEvent,"Register.fxml");
    }
}

