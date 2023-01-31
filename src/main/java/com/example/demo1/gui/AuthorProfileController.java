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
    public Label avatarLabel = new Label();
    private DataSingleton data = DataSingleton.getInstance();
    private TableViewRecipe TableViewObject = new TableViewRecipe();
    private Integer indexImages = 0;
    private List<String> images_list;
    private Stage stage;
    private Integer recipeId;
    private String authorName;
    private String password;
    @FXML
    private ImageView avatar;
    private ArrayList avatarsAvailable;
    @FXML
    private TextField authorNameField;
    @FXML
    private TextField passwordField;
    private String parameterToChange = null;
    private String nameToSearch = null;
    private Integer pageNumber = 0;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    public ImageView imageNew;


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
        authorName = data.getAuthorName();
        password = data.getPassword();
        avatar = DataSingleton.getInstance().getAvatar();
        authorNameField.setText(authorName);
        authorNameField.setEditable(false);
        passwordField.setText(password);
        passwordField.setEditable(false);
        avatar.setX(avatarLabel.getLayoutX());
        avatar.setY(avatarLabel.getLayoutY() + 20);
        avatar.setFitHeight(100);
        avatar.setFitWidth(100);
        anchorPane.getChildren().add(avatar);
        System.out.println(anchorPane.getChildren().indexOf(avatar));
        avatarsAvailable = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            ImageView temp = new ImageView();
            temp.setImage(Configuration.AVATAR.get(i));
            temp.setId(String.valueOf(i+1));
            temp.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { //evento per il click sull'immagine
                System.out.println(temp.getId());
                DataSingleton.getInstance().setAvatar(Integer.parseInt(temp.getId()));
                int avatarIndex = anchorPane.getChildren().indexOf(avatar);
                avatar.setImage(Configuration.AVATAR.get(Integer.parseInt(temp.getId()) - 1));
                avatar.setX(avatarLabel.getLayoutX());
                avatar.setY(avatarLabel.getLayoutY() + 20);
                avatar.setFitHeight(100);
                avatar.setFitWidth(100);
                anchorPane.getChildren().set(avatarIndex,avatar);
                AuthorService.updateImage(DataSingleton.getInstance().getAuthorName(),Integer.parseInt(temp.getId())); //chiamata a DAO
                /*try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
                    MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
                    MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_AUTHOR);
                    Document query = new Document().append("authorName", authorName);
                    Bson updates = Updates.combine(
                            Updates.set("image", Integer.parseInt(temp.getId()))
                    );
                    UpdateOptions options = new UpdateOptions().upsert(true);
                    try {
                        UpdateResult result = collection.updateOne(query, updates, options);
                        System.out.println("Modified document count: " + result.getModifiedCount());
                    } catch (MongoException me) {
                        System.err.println("Unable to update due to an error: " + me);
                    }
                }*/
            });
            temp.setFitWidth(70);
            temp.setFitHeight(70);
            if (i >= 4) {
                temp.setY(avatarLabel.getLayoutY() + 170);
                temp.setX(avatarLabel.getLayoutX() - 50 + 55*(i-4));
            }
            else {
                temp.setY(avatarLabel.getLayoutY() + 100);
                temp.setX(avatarLabel.getLayoutX() - 50 + 55 * i);
            }
            avatarsAvailable.add(i,temp);
        }
        createTableView();
    }

    public void searchInDBAndLoadInTableView(String nameToSearch, int pageNumber) { //chiamata a DAO
        TableViewObject.resetObservableArrayList();
        RecipeService.getRecipeFromAuthor(DataSingleton.getInstance().getAuthorName(),
                10 * pageNumber,10).forEach(recipeReducted ->
                TableViewObject.addToObservableArrayList(new RowRecipe( recipeReducted.getName(),
                        recipeReducted.getAuthorName(),
                        new ImageTableView(new ImageView(recipeReducted.getImage())).getImage())));
        TableViewObject.setItems();
    }

    public void createTableView() {
        nameToSearch = authorName;
        searchInDBAndLoadInTableView(nameToSearch, pageNumber);
        TableViewObject.setEventForTableCells();
        TableViewObject.setTable();
        TableViewObject.getTable().setLayoutX(20);
        TableViewObject.getTable().setLayoutY(240);
        anchorPane.getChildren().addAll(TableViewObject.getTable());
        avatarsAvailable.forEach(image -> anchorPane.getChildren().add((Node) image));
    }

    public void changeProfileParameter(ActionEvent actionEvent) {

        if (parameterToChange == null) {
            System.out.println("Prima devi selezionare un' opzione dal menu a tendina sopra");
            return;
        }
        String parameterNewValue = parameterValueField.getText();

        /*try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
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
        */
        Author currentAuthor = new Author(DataSingleton.getInstance().getAuthorName(),DataSingleton.getInstance().getPassword(),
                DataSingleton.getInstance().getAvatarIndex(),DataSingleton.getInstance().getAuthorPromotion());
        if (!parameterToChange.equals("authorName")) {
            //System.out.println("PARAMETRO CAMBIATO");
            AuthorService.changePassword(parameterNewValue,currentAuthor); //chiamata a DAO
            if (parameterToChange.equals("password")) {
                passwordField.setText(parameterNewValue);
                DataSingleton.getInstance().setPassword(parameterNewValue);
                authorName = data.getAuthorName();
                password = data.getPassword();
            }
            parameterToChange = null;
            return;
        }

        /*MongoCollection<Document> collectionRecipe = database.getCollection(Configuration.MONGODB_RECIPE);
        Document queryRecipe = new Document().append("AuthorName", authorName);
        Bson updatesRecipe = Updates.combine(
                Updates.set("AuthorName", parameterNewValue));
        UpdateOptions optionsRecipe = new UpdateOptions().upsert(true);

            try {
                UpdateResult result = collectionRecipe.updateMany(queryRecipe, updatesRecipe, optionsRecipe);
                System.out.println("Modified document count: " + result.getModifiedCount());
            } catch (MongoException me) {
                System.err.println("Unable to update due to an error: " + me);
            }
        */
        //System.out.println(parameterNewValue);
        if (!AuthorService.changeAuthorName(parameterNewValue, currentAuthor)) { //chiamata a DAO
            parameterToChange = null;
            return;
        }
        //System.out.println("PARAMETRO CAMBIATO");
        authorNameField.setText(parameterNewValue);
        DataSingleton.getInstance().setAuthorName(parameterNewValue);
        authorName = data.getAuthorName();
        password = data.getPassword();
        parameterToChange = null;
        //}
    }

    public void setParameterToAuthorName(ActionEvent actionEvent) {parameterToChange = "authorName";}
    public void setParameterToPassword(ActionEvent actionEvent) {parameterToChange = "password";}
}

