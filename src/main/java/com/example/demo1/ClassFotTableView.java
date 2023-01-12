package com.example.demo1;

import com.mongodb.client.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ClassFotTableView {
    private TableView<Recipe> tabellaDB;
    private ObservableList<Recipe> ol;
    private TableColumn recipeIdCol;
    private TableColumn recipeNameCol;
    private TableColumn authorIdCol;
    private TableColumn authorNameCol;
    private TableColumn ImageCol;


    public void initializeTableView() {
        tabellaDB = new TableView<>();

        recipeIdCol = new TableColumn("RecipeID");
        recipeNameCol = new TableColumn("RecipeName");
        authorIdCol = new TableColumn("AuthorID");
        authorNameCol = new TableColumn("AuthorName");
        ImageCol = new TableColumn("Image");

        recipeIdCol.setCellValueFactory(new PropertyValueFactory<>("recipeId"));
        recipeNameCol.setCellValueFactory(new PropertyValueFactory<>("recipeName"));
        authorIdCol.setCellValueFactory(new PropertyValueFactory<>("authorId"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        ImageCol.setCellValueFactory(new PropertyValueFactory<>("image"));

    }

    public void setTabellaDB() {
        tabellaDB.setItems(ol);
        tabellaDB.getColumns().addAll(recipeIdCol, recipeNameCol, authorIdCol, authorNameCol, ImageCol);
    }

    public TableView<Recipe> getTabellaDB() {
        return tabellaDB;
    }

    public void caricaElementiTableViewDB() { //1
        ol = FXCollections.observableArrayList();
        String uri = "mongodb://localhost:27017";
        String nomeDatabase = "test";
        String nomeCollection = "recipes";
        List<Document> listaRecipes = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(nomeDatabase); //da scegliere il nome uguale per tutti
            MongoCollection<Document> collection = database.getCollection(nomeCollection);

            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext())
                listaRecipes.add(cursor.next());

            Object prova;
            for (int i = 0; i < 10/*listaRecipes.size()*/; i++) {
                prova = listaRecipes.get(i).get("Images",new Document("$first","$Images"));
                //System.out.println(prova);
                ArrayList test = (ArrayList) prova;
                ol.add(new Recipe(listaRecipes.get(i).get(("RecipeId")), listaRecipes.get(i).get("Name"),
                        listaRecipes.get(i).get(("AuthorId")), listaRecipes.get(i).get("AuthorName"), test.get(0)));
            }
        }
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
