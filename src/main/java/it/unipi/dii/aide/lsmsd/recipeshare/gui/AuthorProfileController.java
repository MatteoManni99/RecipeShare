package it.unipi.dii.aide.lsmsd.recipeshare.gui;

import it.unipi.dii.aide.lsmsd.recipeshare.Configuration;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowImage;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowRecipe;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewAbstract;
import it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview.TableViewRecipeWithoutAuthor;
import it.unipi.dii.aide.lsmsd.recipeshare.model.Author;
import it.unipi.dii.aide.lsmsd.recipeshare.service.AuthorService;
import it.unipi.dii.aide.lsmsd.recipeshare.service.RecipeService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class AuthorProfileController implements Initializable {

    @FXML
    public Label name;
    @FXML
    public ImageView image;
    @FXML
    public Label avatarLabel = new Label();

    private final DataSingleton data = DataSingleton.getInstance();
    private final TableViewAbstract tableRecipe = new TableViewRecipeWithoutAuthor();
    @FXML
    private TextField authorNameField;
    @FXML
    private TextField passwordField;
    @FXML
    private AnchorPane anchorPane;
    public ImageView avatarImage;

    @FXML
    public void onBackClick(ActionEvent actionEvent) throws IOException {
        Utils.changeScene(actionEvent,"HomeAuthor.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        avatarImage.setImage(data.getAvatar().getImage());
        authorNameField.setText(data.getAuthorName());
        passwordField.setText(data.getPassword());
        createTableView();
    }

    public void searchInDBAndLoadInTableView(Integer pageNumber) { //chiamata a DAO
        tableRecipe.resetObservableArrayList();
        RecipeService.getRecipeFromAuthor(data.getAuthorName(),
                10 * pageNumber,10).forEach(recipeReducted ->
                tableRecipe.addToObservableArrayList(new RowRecipe( recipeReducted.getName(),
                        recipeReducted.getAuthorName(), new RowImage(new ImageView(recipeReducted.getImage())).getImage())));
        tableRecipe.setItems();
    }

    public void createTableView() {
        searchInDBAndLoadInTableView(0);
        tableRecipe.setEventForTableCells();
        tableRecipe.setTable();
        tableRecipe.getTable().setLayoutX(20);
        tableRecipe.getTable().setLayoutY(240);
        tableRecipe.getTable().setPrefWidth(450);
        tableRecipe.getTable().setPrefHeight(340);
        anchorPane.getChildren().addAll(tableRecipe.getTable());
    }

    public void changePassword(ActionEvent actionEvent) {
        String newPassword = passwordField.getText();
        System.out.println(newPassword);
        Author currentAuthor = new Author(data.getAuthorName(),data.getPassword(),
                data.getAvatarIndex(), data.getAuthorPromotion());
        if(AuthorService.changePassword(newPassword,currentAuthor)){
            data.setPassword(newPassword);
            passwordField.setText(newPassword);
            Utils.changeScene(actionEvent,"AuthorProfile.fxml");
        }else {System.out.println("Error: Password wasn't changed");}
    }

    @Deprecated
    public void changeAuthorName(ActionEvent actionEvent) {
        String newAuthorName = authorNameField.getText();
        System.out.println(newAuthorName);
        Author currentAuthor = new Author(data.getAuthorName(), data.getPassword(),
                data.getAvatarIndex(), data.getAuthorPromotion());
        if (AuthorService.changeAuthorName(newAuthorName, currentAuthor)) {
            data.setAuthorName(newAuthorName);
            authorNameField.setText(newAuthorName);
            Utils.changeScene(actionEvent, "AuthorProfile.fxml");
        }
    }

    private void setSelectedImage(Integer imageNumber){
        avatarImage.setImage(Configuration.AVATAR.get(imageNumber-1));
        if(!AuthorService.changeAvatar(data.getAuthorName(),imageNumber, data.getAvatarIndex()))
            System.out.println("Avatar non cambiato per un errore");
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

