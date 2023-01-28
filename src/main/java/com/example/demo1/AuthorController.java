package com.example.demo1;

import com.example.demo1.dao.mongo.AuthorMongoDAO;
import com.example.demo1.service.RecipeService;
import com.example.demo1.model.Author;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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

    private final ClassForTableView TableViewObject = new ClassForTableView();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    public void onBackClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loggato.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setScene(scene);
        stage.show();
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
        name.setText(Objects.requireNonNull(author).getName());
        image.setImage(Configuration.AVATAR.get(author.getImage() - 1));
        createTableView(TableViewObject);
    }

    public void createTableView(ClassForTableView TableViewObject) {
        TableViewObject.initializeTableView("Loggato");
        searchInDBAndLoadInTableView(authorName, pageNumber);
        TableViewObject.setEventForTableCells();
        TableViewObject.setTabellaDB();
        TableViewObject.getTabellaDB().setLayoutX(40);
        TableViewObject.getTabellaDB().setLayoutY(240);
        anchorPane.getChildren().add(TableViewObject.getTabellaDB());
    }

    public void searchInDBAndLoadInTableView(String nameToSearch, Integer pageNumber) {
        TableViewObject.resetObservableArrayList();
        List<RecipeTableView> listRecipeTable = new ArrayList<>();
        RecipeService.getRecipeFromAuthor(nameToSearch, pageNumber*10, 10)
                .forEach(recipeReducted -> listRecipeTable.add(
                        new RecipeTableView(recipeReducted.getName(), recipeReducted.getAuthorName(), new ImageView(recipeReducted.getImage()))));
        TableViewObject.setObservableArrayList(listRecipeTable);
        TableViewObject.setItems();
    }
}
