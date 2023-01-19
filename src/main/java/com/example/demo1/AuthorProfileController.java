package com.example.demo1;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.limit;

public class AuthorProfileController implements Initializable {

    @FXML
    public Label name;
    @FXML
    public Text description;
    @FXML
    public ImageView image;
    @FXML
    public ListView<String> ingredients;
    @FXML
    public ListView<String> keywords;
    @FXML
    public ListView<String> instructions;
    private DataSingleton data = DataSingleton.getInstance();
    private ClassForTableView TableViewObject = new ClassForTableView();
    private Integer indexImages=0;
    private List<String> images_list;
    private Stage stage;
    private Integer recipeId;
    private String authorName;
    private String password;
    @FXML
    private TextField authorNameField;
    @FXML
    private TextField passwordField;

    private String nameToSearch = null;
    private Integer pageNumber = 0;
    @FXML
    private AnchorPane anchorPane;


    private void printImages(){
        image.setImage(new Image(images_list.get(indexImages)));
    }

    @FXML
    public void onPreviousClick(ActionEvent actionEvent) throws IOException {
        indexImages -= indexImages>0 ? 1 : 0;
        printImages();
    }

    @FXML
    public void onNextClick(ActionEvent actionEvent) throws IOException {
        indexImages += indexImages<images_list.size()-1 ? 1 : 0;
        printImages();
    }

    @FXML
    public void onBackClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loggato.fxml"));
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onReportRecipeClick(ActionEvent actionEvent){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        authorName = data.getAuthorName();
        password = data.getPassword();
        authorNameField.setText(authorName);
        passwordField.setText(password);
        System.out.println(authorName);
        createTableView(TableViewObject);
    }

    public void searchInDBAndLoadInTableView(String nameToSearch,int pageNumber){
        Document recipeDoc;
        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_RECIPE);
            MongoCursor<Document> cursor;
            //Bson filter = Filters.regex("Name", "^(?)" + nameToSearch); //da togliere era il vecchio filtro
            Bson filter = new Document("AuthorName",new Document("$regex",/*nameToSearch*/"elly9812").append("$options","i"));
            Bson match = match(filter);
            Bson project = project(new Document("RecipeId",1).append("Name",1).append("AuthorId",1).append("AuthorName",1)
                    .append("Images", new Document("$first","$Images")));

            if(nameToSearch == null){
                cursor = collection.aggregate(Arrays.asList(skip(10*pageNumber),limit(10),project)).iterator();
                System.out.println("null");
            }else{
                cursor = collection.aggregate(Arrays.asList(match,skip(10*pageNumber),limit(10),project)).iterator();
                System.out.println(nameToSearch);
            }
            TableViewObject.resetObservableArrayList();
            while (cursor.hasNext()){
                recipeDoc = cursor.next();
                Recipe recipe = new Recipe(recipeDoc.getInteger(("RecipeId")),recipeDoc.getString("Name"),recipeDoc.getInteger(("AuthorId")),
                        recipeDoc.getString("AuthorName"),new ClassForTableView.CustomImage(new ImageView(recipeDoc.getString("Images"))).getImage());
                TableViewObject.addToObservableArrayList(recipe);
            }
            TableViewObject.setItems();
        }
    }

    public void createTableView (ClassForTableView TableViewObject) {
        TableViewObject.initializeTableView("Loggato");
        nameToSearch = authorName;
        searchInDBAndLoadInTableView(nameToSearch,pageNumber);
        TableViewObject.setEventForTableCells();
        TableViewObject.setTabellaDB();
        anchorPane.getChildren().add(TableViewObject.getTabellaDB());
    }
}
