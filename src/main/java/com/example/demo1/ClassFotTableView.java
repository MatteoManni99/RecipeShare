package com.example.demo1;

import com.mongodb.client.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;

import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Aggregates.skip;

public class ClassFotTableView {
    public TableView<Recipe> tabellaDB;
    private ObservableList<Recipe> ol;
    private TableColumn recipeIdCol;
    private TableColumn nameCol;
    private TableColumn authorIdCol;
    private TableColumn authorNameCol;
    private TableColumn imageCol;

    private Stage stage;

    public void initializeTableView() {
        tabellaDB = new TableView<>();

        recipeIdCol = new TableColumn("RecipeID");
        nameCol = new TableColumn("Name");
        authorIdCol = new TableColumn("AuthorID");
        authorNameCol = new TableColumn("AuthorName");
        imageCol = new TableColumn<CustomImage, ImageView>("Image");
        /////////////non cambiate i nomi o vi vengo a cercare a casa//////////////////
        recipeIdCol.setCellValueFactory(new PropertyValueFactory<>("recipeId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorIdCol.setCellValueFactory(new PropertyValueFactory<>("authorId"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        imageCol.setCellValueFactory(new PropertyValueFactory<CustomImage,ImageView>("image")); ////non cambiate i nomi o vi vengo a cercare a casa//////////////////
        /////////
        tabellaDB.setPrefHeight(400);
        tabellaDB.setPrefWidth(600);
        tabellaDB.setLayoutX(150);
        tabellaDB.setLayoutY(150);
    }


    public void setTabellaDB() {
        tabellaDB.setItems(ol);
        tabellaDB.getColumns().addAll(recipeIdCol,nameCol, authorIdCol,authorNameCol, imageCol);
    }

    public TableView<Recipe> getTabellaDB() {
        return tabellaDB;
    }

    public void caricaElementiTableViewDB() {
        ol = FXCollections.observableArrayList();
        String uri = Configuration.MONGODB_URL;
        String nomeDatabase = Configuration.MONGODB_DB;
        String nomeCollection = Configuration.MONGODB_RECIPE;
        List<Document> listaRecipes = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(nomeDatabase); //da scegliere il nome uguale per tutti
            MongoCollection<Document> collection = database.getCollection(nomeCollection);

            MongoCursor<Document> cursor = collection.find().iterator();

            while (cursor.hasNext())
                listaRecipes.add(cursor.next());

            for (int i = 0; i < 10/*listaRecipes.size()*/; i++) {
                Object prova = listaRecipes.get(i).get("Images", new Document("$first", "$Images"));
                //System.out.println(prova);
                ArrayList test = (ArrayList) prova;
                ol.add(new Recipe(listaRecipes.get(i).getInteger(("RecipeId")), listaRecipes.get(i).getString("Name"),
                        listaRecipes.get(i).getInteger(("AuthorId")), listaRecipes.get(i).getString("AuthorName"), new CustomImage(new ImageView(test.get(0).toString())).getImage()));
            }
        }
        setEventForTableCells();
    }
    public void uploadNewElementsTableViewDB(Integer pageNumber) {
        ol = FXCollections.observableArrayList();
        Document recipe;
        ArrayList recipeImage;

        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_RECIPE);
            MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(skip(10*pageNumber),limit(10))).iterator();

            while (cursor.hasNext()) {
                recipe = cursor.next();
                System.out.println(recipe.getInteger(("RecipeId")));
                Object prova = recipe.get("Images", new Document("$first", "$Images"));
                //System.out.println(prova);
                ArrayList test = (ArrayList) prova;
                ol.add(new Recipe(recipe.getInteger(("RecipeId")), recipe.getString("Name"),recipe.getInteger(("AuthorId")),
                        recipe.getString("AuthorName"),new CustomImage(new ImageView(test.get(0).toString())).getImage()));
            }
        }
        tabellaDB.setItems(ol);
    }

    public void setEventForTableCells() {
        tabellaDB.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { //evento per il mouse clickato
                TableCell cell = findCell(evt,tabellaDB);
                if (cell != null && !cell.isEmpty()) {
                    if(cell.getTableColumn().getText().equals("Name")){
                        System.out.println(cell.getText()); // Andare alla pagina relativa alla ricetta

                        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Recipe.fxml"));
                        stage = (Stage) ((Node)evt.getSource()).getScene().getWindow();
                        Scene scene = null;
                        try {
                            scene = new Scene(fxmlLoader.load(), 1000, 600);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        stage.setScene(scene);
                        stage.show();
                    }
                    if(cell.getTableColumn().getText().equals("AuthorName")){
                        System.out.println(cell.getText()); // Andare alla pagina relativa all'autore
                    }
                    evt.consume();
                }
            }
        );
    }

    private static TableCell findCell(MouseEvent event, TableView table) { //metodo chiamato dall'evento
        Node node = event.getPickResult().getIntersectedNode();
        // go up in node hierarchy until a cell is found or we can be sure no cell was clicked
        while (node != table && !(node instanceof TableCell)) {
            node = node.getParent();
        }
        return node instanceof TableCell ? (TableCell) node : null;
    }

    static class CustomImage {
        private ImageView image;
        CustomImage(ImageView img) {
            this.image = img;
            this.image.setFitHeight(50);
            this.image.setFitWidth(50);
        }
        public void setImage(ImageView value) {image = value;}
        public ImageView getImage() {return image;}

    }
    /*public void registraGiocatoreDB() { //2
        ol = FXCollections.observableArrayList();
        try (
                Connection co = DriverManager.getConnection("jdbc:mysql://"+Configuration.databaseIp+":"+Configuration.databasePort+"/spaceinvaders",
                        Configuration.databaseUser,Configuration.databasePsw);
                PreparedStatement ps = co.prepareStatement("INSERT INTO classifica VALUES (?, ?)");
        ) {
            ps.setString(1,PlayerStatusLayout.playerName.getText()); ps.setInt(2,SpaceInvaders.score);
            System.out.println("rows affected: " + ps.executeUpdate());
        } catch (SQLException e) {System.err.println(e.getMessage());}
    }*/

}
