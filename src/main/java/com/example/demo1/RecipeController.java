package com.example.demo1;

import com.mongodb.client.*;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

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
    public Text ingredients;
    @FXML
    public ImageView image;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Integer id_recipe = 38;
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("RecipeShare");
            MongoCollection<Document> collectionRecipe = database.getCollection("recipe");
            Bson match = match(Filters.eq("RecipeId", id_recipe));
            Bson project = project(new Document("Images",new Document("$first","$Images"))
                    .append("Name",1).append("RecipeId",1));
            //MongoCursor<Document> cursor = collectionRecipe.find(match).iterator();
            for (Document doc : collectionRecipe.aggregate(Arrays.asList(match))) {
                name.setText(doc.getString("Name"));
                description.setText(doc.getString("Description"));
                List<String> ingredients_array = doc.getList("RecipeIngredientParts", String.class);
                System.out.println(ingredients_array);
                //ingredients.setText(doc.getString("RecipeIngredientParts"));
                image.setImage(new Image("https://www.focus.it/images/2021/02/22/gatto_1020x680.jpg"));
            }
        }
    }
}
