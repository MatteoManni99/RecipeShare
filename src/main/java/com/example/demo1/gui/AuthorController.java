package com.example.demo1.gui;

import com.example.demo1.Configuration;
import com.example.demo1.dao.mongo.AuthorMongoDAO;
import com.example.demo1.service.RecipeService;
import com.example.demo1.model.Author;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AuthorController implements Initializable {
    @FXML
    public ImageView image;

    @FXML
    public Label name;

    private Integer pageNumber = 0;

    private final DataSingleton data = DataSingleton.getInstance();
    private String authorName;

    private final TableViewAbstract TableViewObject = new TableViewRecipe();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    public void onBackClick(ActionEvent actionEvent) throws IOException {
        if(data.getTypeOfUser().equals("moderator")) Utils.changeScene(actionEvent,"Moderator.fxml");
        else Utils.changeScene(actionEvent,"Loggato.fxml");
    }

    @FXML
    public void onNextPageClick(){
        pageNumber = pageNumber + 1;
        searchInDBAndLoadInTableView(authorName,pageNumber);
    }
    @FXML
    public void onPreviousPageClick(){
        if(pageNumber>=1){
            pageNumber = pageNumber - 1;
            searchInDBAndLoadInTableView(authorName,pageNumber);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        authorName = data.getOtherAuthorName();
        System.out.println(authorName);
        Author author = AuthorMongoDAO.getAuthor(authorName);
        if (author == null) System.out.println("NON ESISTE NELLA COLLECTION AUTHOR"); //problema: authorName c'è in Recipe ma non in Author
        name.setText(Objects.requireNonNull(author).getName());
        image.setImage(Configuration.AVATAR.get(author.getImage() - 1));
        createTableView();
    }

    public void createTableView() {
        searchInDBAndLoadInTableView(authorName, pageNumber);
        TableViewObject.setEventForTableCells();
        TableViewObject.setTable();
        TableViewObject.getTable().setLayoutX(40);
        TableViewObject.getTable().setLayoutY(240);
        anchorPane.getChildren().add(TableViewObject.getTable());
    }

    public void searchInDBAndLoadInTableView(String nameToSearch, Integer pageNumber) {
        TableViewObject.resetObservableArrayList();
        RecipeService.getRecipeFromAuthor(nameToSearch, pageNumber*10, 10)
                .forEach(recipeReducted -> TableViewObject.addToObservableArrayList(
                        new RowRecipe(recipeReducted.getName(), recipeReducted.getAuthorName(), new ImageView(recipeReducted.getImage()))));
        TableViewObject.setItems();
    }
}
