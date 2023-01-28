package com.example.demo1;

import com.example.demo1.gui.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassForTableView {
    public TableView<RecipeTableView> tabellaDB;
    private ObservableList<RecipeTableView> ol;
    //private TableColumn recipeIdCol;
    private TableColumn nameCol;
    //private TableColumn authorIdCol;
    private TableColumn authorNameCol;
    private TableColumn imageCol;

    private Stage stage;

    private DataSingleton data = DataSingleton.getInstance();
    //private DataSingleton dataAuthor = DataSingleton.getInstance();

    public void initializeTableView(String classe) {
        tabellaDB = new TableView<>();

        //recipeIdCol = new TableColumn<Recipe, String>("RecipeID");
        nameCol = new TableColumn<RecipeTableView, String>("Name");
        //authorIdCol = new TableColumn<Recipe, String>("AuthorID");
        authorNameCol = new TableColumn<RecipeTableView, String>("AuthorName");
        //recipeIdCol = new TableColumn("RecipeID");
        //nameCol = new TableColumn("Name");
        //authorIdCol = new TableColumn("AuthorID");
        //authorNameCol = new TableColumn("AuthorName");
        imageCol = new TableColumn<CustomImage, ImageView>("Image");

        /////////////non cambiate i nomi o vi vengo a cercare a casa//////////////////
        //recipeIdCol.setCellValueFactory(new PropertyValueFactory<>("recipeId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        //authorIdCol.setCellValueFactory(new PropertyValueFactory<>("authorId"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        imageCol.setCellValueFactory(new PropertyValueFactory<CustomImage,ImageView>("image"));
        /////////////non cambiate i nomi o vi vengo a cercare a casa//////////////////

        tabellaDB.setPrefHeight(400);
        tabellaDB.setPrefWidth(600);
        tabellaDB.setLayoutX(150);
        tabellaDB.setLayoutY(150);
    }


    public void setTabellaDB() {
        //tabellaDB.setItems(ol);
        tabellaDB.getColumns().addAll(imageCol, nameCol, authorNameCol);
    }
    public void setItems(){
        tabellaDB.setItems(ol);
    }

    public TableView<RecipeTableView> getTabellaDB() {
        return tabellaDB;
    }

    /* vecchia versione di searchInDBAndLoadInTableView che è in LoggatoController
    public void uploadElementsTableViewDB(Integer pageNumber) {
        Document recipeDoc;
        ArrayList recipeImage;
        resetObservableArrayList();

        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_RECIPE);
            MongoCursor<Document> cursor = collection.aggregate(Arrays.asList(skip(10*pageNumber),limit(10))).iterator();

            while (cursor.hasNext()) {
                recipeDoc = cursor.next();
                Object prova = recipeDoc.get("Images", new Document("$first", "$Images"));
                //System.out.println(prova);
                ArrayList test = (ArrayList) prova;
                ol.add(new Recipe(recipeDoc.getInteger(("RecipeId")), recipeDoc.getString("Name"),recipeDoc.getInteger(("AuthorId")),
                        recipeDoc.getString("AuthorName"),new CustomImage(new ImageView(test.get(0).toString())).getImage()));

                //da rendere efficiente accorpando gli array o qualcosa del genere
                recipeNameArray.add(recipeDoc.getString("Name"));
                recipeIdArray.add(recipeDoc.getInteger(("RecipeId")));
            }
        }
    }*/
    public void resetObservableArrayList(){
        ol = FXCollections.observableArrayList();
    }

    public void setObservableArrayList(List<RecipeTableView> list){
        ol = FXCollections.observableList(list);
    }
    public void addToObservableArrayList(RecipeTableView recipe){
        ol.add(recipe);
    }

    public void setEventForTableCells() {
        tabellaDB.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { //evento per il mouse clickato
                TableCell cell = findCell(evt,tabellaDB);
                if (cell != null && !cell.isEmpty()) {
                    if(cell.getTableColumn().getText().equals("Name")){
                        //System.out.println(cell.getText()); // Andare alla pagina relativa alla ricetta
                        data.setRecipeName(cell.getText());
                        Utils.changeScene(evt,"Recipe.fxml");
                        /*try {
                            Integer cellPosition = recipeNameArray.indexOf(cell.getText());
                            data.setRecipeId(recipeIdArray.get(cellPosition));
                            changeScene(evt,"Recipe.fxml");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }*/
                    }
                    if(cell.getTableColumn().getText().equals("AuthorName")){
                        System.out.println(cell.getText()); // Andare alla pagina relativa all'autore
                        data.setOtherAuthorName(cell.getText());
                        Utils.changeScene(evt,"Author.fxml");
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
}
