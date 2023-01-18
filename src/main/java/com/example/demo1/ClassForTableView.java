package com.example.demo1;

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

public class ClassForTableView {
    public TableView<Recipe> tabellaDB;
    private ObservableList<Recipe> ol;
    private TableColumn recipeIdCol;
    private TableColumn nameCol;
    private TableColumn authorIdCol;
    private TableColumn authorNameCol;
    private TableColumn imageCol;

    private Stage stage;

    private DataSingleton data = DataSingleton.getInstance();

    //da rendere efficiente accorpando gli array o qualcosa del genere
    private ArrayList<String> recipeNameArray = new ArrayList<String>();
    private ArrayList<Integer> recipeIdArray = new ArrayList<Integer>();

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
        //tabellaDB.setItems(ol);
        tabellaDB.getColumns().addAll(recipeIdCol,nameCol, authorIdCol,authorNameCol, imageCol);
    }
    public void setItems(){
        tabellaDB.setItems(ol);
    }

    public TableView<Recipe> getTabellaDB() {
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
        recipeNameArray.clear();
        recipeIdArray.clear();
    }
    public void addToObservableArrayList(Recipe recipe){
        ol.add(recipe);
        recipeNameArray.add(recipe.getName());
        recipeIdArray.add(recipe.getRecipeId());
    }

    public void setEventForTableCells() {
        tabellaDB.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { //evento per il mouse clickato
                TableCell cell = findCell(evt,tabellaDB);
                if (cell != null && !cell.isEmpty()) {
                    if(cell.getTableColumn().getText().equals("Name")){
                        System.out.println(cell.getText()); // Andare alla pagina relativa alla ricetta
                        try {
                            Integer cellPosition = recipeNameArray.indexOf(cell.getText());
                            data.setRecipeId(recipeIdArray.get(cellPosition));
                            changeScene(evt,"Recipe.fxml");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if(cell.getTableColumn().getText().equals("AuthorName")){
                        System.out.println(cell.getText()); // Andare alla pagina relativa all'autore
                    }
                    evt.consume();
                }
            }
        );
    }
    private void changeScene(MouseEvent evt, String fxmlFileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(fxmlFileName));
        Parent tableViewParent = fxmlLoader.load();
        stage = (Stage) ((Node)evt.getSource()).getScene().getWindow();
        Scene tableViewScene = new Scene(tableViewParent, 1000, 700);
        stage.setScene(tableViewScene);
        stage.show();
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
