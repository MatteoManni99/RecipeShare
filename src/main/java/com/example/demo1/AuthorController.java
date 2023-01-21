package com.example.demo1;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
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
import org.bson.Document;
import org.bson.conversions.Bson;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.limit;

public class AuthorController implements Initializable {
    @FXML
    public ImageView image;

    @FXML
    public Label name;

    private Stage stage;

    private Integer pageNumber = 0;

    private DataSingleton data = DataSingleton.getInstance();
    private String authorName;

    private ClassForTableView TableViewObject = new ClassForTableView();

    @FXML
    private AnchorPane anchorPane;

    @FXML
    public void onBackClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loggato.fxml"));
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
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
        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collectionAuthor = database.getCollection(Configuration.MONGODB_AUTHOR);
            Bson match = match(Filters.eq("authorName", authorName));
            for (Document doc : collectionAuthor.aggregate(List.of(match))) {
                System.out.println("QUI");
                name.setText(doc.getString("authorName"));
                System.out.println(doc.getInteger("image"));
                System.out.println(Configuration.AVATAR.get(doc.getInteger("image") - 1));
                image.setImage(Configuration.AVATAR.get(doc.getInteger("image") - 1));
            }
        }
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

    public void searchInDBAndLoadInTableView(String nameToSearch, int pageNumber) {
        Document recipeDoc;
        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_RECIPE);
            MongoCursor<Document> cursor;
            Bson filter = new Document("AuthorName", new Document("$regex",nameToSearch).append("$options", "i"));
            Bson match = match(filter);
            Bson project = project(new Document("Name", 1).append("AuthorName", 1)
                    .append("Images", new Document("$first", "$Images")));

            if (nameToSearch == null) {
                cursor = collection.aggregate(Arrays.asList(skip(10 * pageNumber), limit(10), project)).iterator();
                System.out.println("null");
            } else {
                cursor = collection.aggregate(Arrays.asList(match, skip(10 * pageNumber), limit(10), project)).iterator();
                System.out.println(nameToSearch);
            }
            TableViewObject.resetObservableArrayList();
            while (cursor.hasNext()) {
                recipeDoc = cursor.next();
                Recipe recipe = new Recipe( recipeDoc.getString("Name"), recipeDoc.getString("AuthorName"),
                        new ClassForTableView.CustomImage(new ImageView(recipeDoc.getString("Images"))).getImage());
                TableViewObject.addToObservableArrayList(recipe);
            }
            TableViewObject.setItems();
        }
    }
}
