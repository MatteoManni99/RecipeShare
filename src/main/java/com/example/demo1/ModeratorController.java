package com.example.demo1;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Indexes.descending;
import static com.mongodb.client.model.Projections.include;

public class ModeratorController implements Initializable {
    @FXML
    private Label recipeText;
    private double startingX;
    @FXML
    private Label reviewText;
    private Stage stage;
    @FXML
    private VBox vbox;
    private Integer pageNumber = 0;
    private String authorName;

    private String authorNameClicked;

    private ClassTableAuthor tabella;
    @FXML
    private AnchorPane anchorPane;
    private ClassTableAuthor tableAuthor = new ClassTableAuthor();
    @FXML
    public void onLogoutClick(ActionEvent actionEvent) throws IOException {
        String nomeSchermata = "hello-view.fxml";
        cambiaSchermata(actionEvent,nomeSchermata);
    }

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

    public void onHighestRatioQueryClick() {
        String uri = "mongodb://localhost:27017";
        Map<String, Integer> map = new TreeMap<String, Integer>();
        Map<String, Double> mapRatio = new TreeMap<String, Double>();
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("RecipeShare");
            Bson group = new Document("$group", new Document("_id", "$AuthorName").
                    append("count",new Document("$count",new Document())));
            //Bson match = match(gt("count", 1));
            Bson sort = sort(descending("_id"));

            MongoCollection<Document> collectionRecipe = database.getCollection("recipe");
            for (Document document : collectionRecipe.aggregate(Arrays.asList(group))) {
                map.put(String.valueOf(document.getString("_id")),document.getInteger("count"));
            }
            //map.forEach((key, value) -> System.out.println(key + ":" + value));

            MongoCollection<Document> collectionReportedRecipes = database.getCollection("reportedRecipes");
            for (Document document : collectionReportedRecipes.aggregate(Arrays.asList(group))) {
                String authorName = document.getString("_id");
                Integer count = document.getInteger("count");
                Integer recipeTot = map.get(authorName);
                if (map.containsKey(authorName) && recipeTot>1){ //con questo tolgo quelli che hanno creato 1 ricetta (si può aumentare la soglia o togliere)
                    mapRatio.put(authorName,((double)count)/map.get(authorName));
                }
            }
            Map<String, Double> sortedMapRatioAsc = sortByValue(mapRatio,false);
            sortedMapRatioAsc.forEach((key, value) -> System.out.println(key + ":" + value));
        }
    }
    private static Map<String, Double> sortByValue(Map<String, Double> unsortMap, final boolean order)
    {
        List<Map.Entry<String, Double>> list = new LinkedList<>(unsortMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> order ? o1.getValue().compareTo(o2.getValue()) == 0
                ? o1.getKey().compareTo(o2.getKey())
                : o1.getValue().compareTo(o2.getValue()) : o2.getValue().compareTo(o1.getValue()) == 0
                ? o2.getKey().compareTo(o1.getKey())
                : o2.getValue().compareTo(o1.getValue()));
        return list.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String uri = Configuration.MONGODB_URL;
        List<Document> listaReportedRecipes = new ArrayList<>();
        startingX = recipeText.getLayoutX();

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB); //da scegliere il nome uguale per tutti
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_REPORTED_RECIPE);

            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext())
                listaReportedRecipes.add(cursor.next());
        }

        for (int i = 0; i < listaReportedRecipes.size(); i++)
        {
            setReportedLabels(listaReportedRecipes,i);
        }

        //questa parte sotto è quella che setta il Button per la promozione
        if (DataSingleton.getInstance().getTypeOfUser().equals("moderator")) {
            Button promoteAuthorButton = new Button("PROMOTE AUTHOR");
            promoteAuthorButton.setLayoutX(64);
            promoteAuthorButton.setLayoutY(120);
            promoteAuthorButton.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
                try (MongoClient mongoClient = MongoClients.create(uri)) {
                    MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB); //da scegliere il nome uguale per tutti
                    MongoCollection<Document> collectionAuthor = database.getCollection(Configuration.MONGODB_AUTHOR);
                    Document query = new Document().append("authorName", authorNameClicked);
                    Bson updates = Updates.combine(
                            Updates.set("promotion",1)
                    );
                    UpdateOptions options = new UpdateOptions().upsert(true);
                    try {
                        UpdateResult result = collectionAuthor.updateOne(query, updates, options);
                        System.out.println("Modified document count: " + result.getModifiedCount());
                    } catch (MongoException me) {
                        System.err.println("Unable to update due to an error: " + me);
                    }
                    authorNameClicked = null;
                }
            });
            anchorPane.getChildren().add(promoteAuthorButton);
        }
        createTableView(tableAuthor);
    }

    public void createTableView (ClassTableAuthor TableViewObject) {
        TableViewObject.initializeTableView();
        searchInDBAndLoadInTableView(authorName,pageNumber);
        TableViewObject.setTabellaDB();
        setEventForTableCells();
        anchorPane.getChildren().add(TableViewObject.getTabellaDB());
    }

    public void setEventForTableCells() {
        tableAuthor.getTabellaDB().addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { //evento per il mouse clickato
                    TableCell cell = findCell(evt,tableAuthor.getTabellaDB());
                    if (cell != null && !cell.isEmpty()) {
                        if(cell.getTableColumn().getText().equals("Name")){
                            authorNameClicked = cell.getText();
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

    public void searchInDBAndLoadInTableView(String nameToSearch, Integer pageNumber){
        Document authorDoc;
        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_AUTHOR);
            MongoCursor<Document> cursor;
            //Bson filter = Filters.regex("Name", "^(?)" + nameToSearch); //da togliere era il vecchio filtro
            Bson filter = new Document("Name",new Document("$regex",nameToSearch).append("$options","i"));
            Bson match = match(filter);
            Bson project = project(new Document("authorName",1).append("promotion",1).append("image", 1));
            if(nameToSearch == null){
                cursor = collection.aggregate(Arrays.asList(skip(10*pageNumber),limit(10),project)).iterator();
            }else{
                cursor = collection.aggregate(Arrays.asList(match, skip(10*pageNumber),limit(10),project)).iterator();
                System.out.println(nameToSearch);
            }
            tableAuthor.resetObservableArrayList();
            while (cursor.hasNext()){
                authorDoc = cursor.next();
                Author author = new Author(authorDoc.getString("authorName"),
                        authorDoc.getInteger("promotion"),
                        new ClassTableAuthor.CustomImageAuthor(new ImageView(Configuration.AVATAR.get(authorDoc.getInteger("image") - 1))).getImage());
                tableAuthor.addToObservableArrayList(author);
            }
            tableAuthor.setItems();
        }
    }


    public void setReportedLabels(List<Document> listaReportedRecipes,int i) {
        int documentSize = listaReportedRecipes.get(0).size() - 1;
        List<String> listaLabelNames = new ArrayList<>();
        listaLabelNames.add("RecipeId");
        listaLabelNames.add("RecipeName");
        listaLabelNames.add("Images");
        listaLabelNames.add("AuthorId");
        listaLabelNames.add("AuthorName");
        listaLabelNames.add("ReporterId");
        listaLabelNames.add("ReporterName");

        double copiaStartingX = startingX - 200;
        for (int j = 0;j < documentSize; j++) {
            Label currentLabel = new Label();
            Object valore = listaReportedRecipes.get(i).get(listaLabelNames.get(j));
            currentLabel.setText((String.valueOf(valore)));
            currentLabel.setLayoutX(copiaStartingX + j*10);
            currentLabel.setLayoutY(recipeText.getLayoutY() + 50 + i * 10);
            currentLabel.setMaxWidth(50);
            copiaStartingX += currentLabel.getMaxWidth();
            anchorPane.getChildren().add(currentLabel);
        }
    }
    public void onBrowseAuthorsClick(ActionEvent actionEvent) throws IOException {
        String nomeSchermata = "Ricerca_Utente.fxml";
        DataSingleton.getInstance().setTypeOfUser("moderator");
        cambiaSchermata(actionEvent,nomeSchermata);
    }

    public void cambiaSchermata(ActionEvent actionEvent,String nomeSchermata) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(nomeSchermata));
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setScene(scene);
        stage.show();
    }
}
