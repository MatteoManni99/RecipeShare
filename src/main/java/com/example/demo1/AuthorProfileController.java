package com.example.demo1;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    public TextField parameterValueField;
    private DataSingleton data = DataSingleton.getInstance();
    private ClassForTableView TableViewObject = new ClassForTableView();
    private Integer indexImages = 0;
    private List<String> images_list;
    private Stage stage;
    private Integer recipeId;
    private String authorName;
    private String password;
    @FXML
    private TextField authorNameField;
    @FXML
    private TextField passwordField;
    private String parameterToChange = null;
    private String nameToSearch = null;
    private Integer pageNumber = 0;
    @FXML
    private AnchorPane anchorPane;


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
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Loggato.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void onReportRecipeClick(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        authorName = data.getAuthorName();
        password = data.getPassword();
        authorNameField.setText(authorName);
        authorNameField.setEditable(false);
        passwordField.setText(password);
        passwordField.setEditable(false);
        createTableView(TableViewObject);
    }

    public void searchInDBAndLoadInTableView(String nameToSearch, int pageNumber) {
        Document recipeDoc;
        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_RECIPE);
            MongoCursor<Document> cursor;
            //Bson filter = Filters.regex("Name", "^(?)" + nameToSearch); //da togliere era il vecchio filtro
            Bson filter = new Document("AuthorName", new Document("$regex",/*nameToSearch*/"elly9812").append("$options", "i")); //ho messo elly9812 per avere dei risultati nella tabella, sarebbe da mettere nameToSearch
            Bson match = match(filter);
            Bson project = project(new Document("RecipeId", 1).append("Name", 1).append("AuthorId", 1).append("AuthorName", 1)
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
                Recipe recipe = new Recipe(recipeDoc.getInteger(("RecipeId")), recipeDoc.getString("Name"), recipeDoc.getInteger(("AuthorId")),
                        recipeDoc.getString("AuthorName"), new ClassForTableView.CustomImage(new ImageView(recipeDoc.getString("Images"))).getImage());
                TableViewObject.addToObservableArrayList(recipe);
            }
            TableViewObject.setItems();
        }
    }

    public void createTableView(ClassForTableView TableViewObject) {
        TableViewObject.initializeTableView("Loggato");
        nameToSearch = authorName;
        searchInDBAndLoadInTableView(nameToSearch, pageNumber);
        TableViewObject.setEventForTableCells();
        TableViewObject.setTabellaDB();
        TableViewObject.getTabellaDB().setLayoutX(20);
        TableViewObject.getTabellaDB().setLayoutY(240);
        anchorPane.getChildren().add(TableViewObject.getTabellaDB());
    }

    public void changeProfileParameter(ActionEvent actionEvent) {

        if (parameterToChange == null) {
            System.out.println("Prima devi selezionare un' opzione dal menu a tendina sopra");
            return;
        }
        String parameterNewValue = parameterValueField.getText();

        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_AUTHOR);
            if (parameterToChange.equals("authorName")) {
                Bson filterAuthor = Filters.and(
                        Filters.eq("authorName", parameterNewValue));
                MongoCursor<Document> cursorAuthor = collection.find(filterAuthor).iterator();
                if (cursorAuthor.hasNext()) {
                    System.out.println("QUESTO NICKNAME ESISTE GIA, PROVANE UN ALTRO");
                    return;
                }
            }
            Document query = new Document().append("authorName", authorName);
            Bson updates = Updates.combine(
                    Updates.set(parameterToChange, parameterNewValue)
                    );
            UpdateOptions options = new UpdateOptions().upsert(true);

            try {
                UpdateResult result = collection.updateOne(query, updates, options);
                System.out.println("Modified document count: " + result.getModifiedCount());
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }

            if (parameterToChange.equals("authorName") == false) {
                System.out.println("PARAMETRO CAMBIATO");
                if (parameterToChange.equals("password")) {
                    passwordField.setText(parameterNewValue); //da fare anche per le immagini
                    DataSingleton.getInstance().setPassword(parameterNewValue);
                    authorName = data.getAuthorName();
                    password = data.getPassword();
                }
                parameterToChange = null;
                return;
            }

            MongoCollection<Document> collectionRecipe = database.getCollection(Configuration.MONGODB_RECIPE);
            Document queryRecipe = new Document().append("AuthorName", authorName);
            Bson updatesRecipe = Updates.combine(
                    Updates.set("AuthorName", parameterNewValue));
            UpdateOptions optionsRecipe = new UpdateOptions().upsert(true);

            try {
                UpdateResult result = collection.updateMany(queryRecipe, updatesRecipe, optionsRecipe);
                System.out.println("Modified document count: " + result.getModifiedCount());
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }

            System.out.println("PARAMETRO CAMBIATO");
            authorNameField.setText(parameterNewValue);
            DataSingleton.getInstance().setAuthorName(parameterNewValue);
            authorName = data.getAuthorName();
            password = data.getPassword();
            parameterToChange = null;
        }
    }

    public void setParameterToAuthorName(ActionEvent actionEvent) {parameterToChange = "authorName";}

    public void setParameterToPassword(ActionEvent actionEvent) {parameterToChange = "password";}

    public void setParameterToImage(ActionEvent actionEvent) {
        parameterToChange = "immagine"; //non è ancora nella collection
    }

    public void onGoBackClick(ActionEvent actionEvent) {
    }
}

