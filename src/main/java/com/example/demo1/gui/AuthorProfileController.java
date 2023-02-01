package com.example.demo1.gui;

import com.example.demo1.Configuration;
import com.example.demo1.model.Author;
import com.example.demo1.model.RecipeReducted;
import com.example.demo1.service.AuthorService;
import com.example.demo1.service.RecipeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class AuthorProfileController implements Initializable {

    @FXML
    public Label name;
    @FXML
    public ImageView image;
    @FXML
    //public TextField parameterValueField;
    public Label avatarLabel = new Label();

    private DataSingleton data = DataSingleton.getInstance();
    private TableViewRecipe TableViewObject = new TableViewRecipe();
    private Integer indexImages = 0;
    private List<String> images_list;
    @FXML
    private TextField authorNameField;
    @FXML
    private TextField passwordField;
    private Integer pageNumber = 0;
    @FXML
    private AnchorPane anchorPane;
    public ImageView avatarImage;


    private void printImages() {
        image.setImage(new Image(images_list.get(indexImages)));
    }

    @FXML
    public void onPreviousClick(ActionEvent actionEvent) throws IOException {
        indexImages -= indexImages > 0 ? 1 : 0;
        printImages();
    }

    @FXML
    public void onNextClick(ActionEvent actionEvent) throws IOException {
        indexImages += indexImages < images_list.size() - 1 ? 1 : 0;
        printImages();
    }

    @FXML
    public void onBackClick(ActionEvent actionEvent) throws IOException {
        Utils.changeScene(actionEvent,"Loggato.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        avatarImage.setImage(data.getAvatar().getImage());

        authorNameField.setText(data.getAuthorName());
        //authorNameField.setEditable(false);
        passwordField.setText(data.getPassword());
        //passwordField.setEditable(false);
        createTableView();
    }

    public void searchInDBAndLoadInTableView(Integer pageNumber) { //chiamata a DAO
        TableViewObject.resetObservableArrayList();
        RecipeService.getRecipeFromAuthor(data.getAuthorName(),
                10 * pageNumber,10).forEach(recipeReducted ->
                TableViewObject.addToObservableArrayList(new RowRecipe( recipeReducted.getName(),
                        recipeReducted.getAuthorName(),
                        new ImageTableView(new ImageView(recipeReducted.getImage())).getImage())));
        TableViewObject.setItems();
    }

    public void createTableView() {
        searchInDBAndLoadInTableView(pageNumber);
        TableViewObject.setEventForTableCells();
        TableViewObject.setTable();
        TableViewObject.getTable().setLayoutX(20);
        TableViewObject.getTable().setLayoutY(240);
        anchorPane.getChildren().addAll(TableViewObject.getTable());
    }

    public void changePassword(ActionEvent actionEvent) {
        String newPassword = passwordField.getText();
        System.out.println(newPassword);
        Author currentAuthor = new Author(data.getAuthorName(),data.getPassword(),
                data.getAvatarIndex(), data.getAuthorPromotion());

        AuthorService.changePassword(newPassword,currentAuthor);
        data.setPassword(newPassword);
        passwordField.setText(newPassword);
        Utils.changeScene(actionEvent,"AuthorProfile.fxml");
    }
    public void changeAuthorName(ActionEvent actionEvent) {
        String newAuthorName = authorNameField.getText();
        System.out.println(newAuthorName);
        Author currentAuthor = new Author(data.getAuthorName(),data.getPassword(),
                data.getAvatarIndex(), data.getAuthorPromotion());

        if(AuthorService.changeAuthorName(newAuthorName,currentAuthor)){
            data.setAuthorName(newAuthorName);
            authorNameField.setText(newAuthorName);
            Utils.changeScene(actionEvent,"AuthorProfile.fxml");
        }
    }

    private void setSelectedImage(Integer imageNumber){
        avatarImage.setImage(Configuration.AVATAR.get(imageNumber-1));
        AuthorService.updateImage(data.getAuthorName(),imageNumber);
        data.setAvatar(imageNumber);
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

