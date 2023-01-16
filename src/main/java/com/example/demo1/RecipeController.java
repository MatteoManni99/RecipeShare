package com.example.demo1;

import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static com.mongodb.client.model.Aggregates.*;

public class RecipeController implements Initializable {

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
    private Integer indexImages=0;
    private List<String> images_list;
    private Stage stage;


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
        Scene scene = new Scene(fxmlLoader.load(), 1000, 1000);
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Integer recipeId = data.getRecipeId(); // !!!!! questa funziona se prima del cambio di scena modifico DataSingleton !!!!!!
        String uri = Configuration.MONGODB_URL;
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collectionRecipe = database.getCollection(Configuration.MONGODB_RECIPE);
            Bson match = match(Filters.eq("RecipeId", recipeId));
            Bson project = project(new Document("Images",new Document("$first","$Images"))
                    .append("Name",1).append("RecipeId",1));
            //MongoCursor<Document> cursor = collectionRecipe.find(match).iterator();
            for (Document doc : collectionRecipe.aggregate(Arrays.asList(match))) {
                name.setText(doc.getString("Name"));
                description.setText(doc.getString("Description"));
                ObservableList<String> ingredients_list = FXCollections.observableArrayList(doc.getList("RecipeIngredientParts", String.class));
                ingredients.setItems(ingredients_list);
                ObservableList<String> keywords_list = FXCollections.observableArrayList(doc.getList("Keywords", String.class));
                keywords.setItems(keywords_list);
                ObservableList<String> instructions_list = FXCollections.observableArrayList(doc.getList("RecipeInstructions", String.class));
                instructions.setItems(instructions_list);
                images_list = doc.getList("Images", String.class);
                printImages();
            }
        }
    }
}
