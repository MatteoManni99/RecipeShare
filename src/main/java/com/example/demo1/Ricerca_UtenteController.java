package com.example.demo1;

import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Aggregates.limit;

public class Ricerca_UtenteController implements Initializable {
    @FXML
    private Button cercaUtenteButton;
    @FXML
    private TextField utenteCercato;
    private Stage stage;
    private String authorNameClicked; //qui ci salvo l'utente della tabella che è stato clickato e che quindi si vuole promuovere
    public AnchorPane anchorPane;
    private Integer pageNumber = 0;
    private String nameToSearch = null;

    private ClassTableAuthor tableAuthor = new ClassTableAuthor();

    @FXML
    private TextField authorToSearchTextField;

    @FXML
    public void onLogoutClick(ActionEvent actionEvent) throws IOException {
        String nomeSchermata = "hello-view.fxml";
        cambiaSchermata(actionEvent,nomeSchermata);
    }
    @FXML
    public void onNextPageClick(){
        pageNumber = pageNumber + 1;
        searchInDBAndLoadInTableView(nameToSearch,pageNumber);
    }
    @FXML
    public void onPreviousPageClick(){
        if(pageNumber>=1){
            pageNumber = pageNumber - 1;
            searchInDBAndLoadInTableView(nameToSearch,pageNumber);
        }
    }

    @FXML
    public void onBackClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("loggato.fxml"));
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String uri = Configuration.MONGODB_URL;
        List<Document> listaAuthors = new ArrayList<>();

        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB); //da scegliere il nome uguale per tutti
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_AUTHOR);

            MongoCursor<Document> cursor = collection.find().iterator();
            while (cursor.hasNext())
                listaAuthors.add(cursor.next());
        }

        for (int i = 0; i < listaAuthors.size() - 220000; i++)
            setAuthorLabels(listaAuthors,i);

        //questa parte sotto è quella che setta il Button per la promozione
        if (DataSingleton.getInstance().getTypeOfUser().equals("moderator")) {
            Button promoteAuthorButton = new Button("PROMOTE AUTHOR");
            promoteAuthorButton.setLayoutX(164);
            promoteAuthorButton.setLayoutY(120);
            promoteAuthorButton.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
                authorNameClicked = "1"; //lo metto statico perché ancora non ho la tabella a disposizione
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

        createTableView(tableAuthor, DataSingleton.getInstance().getTypeOfUser().equals("moderator"));
    }

    public void createTableView (ClassTableAuthor TableViewObject, Boolean showPromotion) {
        TableViewObject.initializeTableView();
        searchInDBAndLoadInTableView(nameToSearch,pageNumber);
        if(showPromotion)
            TableViewObject.setTabellaDB();
        else
            TableViewObject.setTableDB();
        anchorPane.getChildren().add(TableViewObject.getTabellaDB());
    }

    public void searchInDBAndLoadInTableView(String nameToSearch, Integer pageNumber){
        Document authorDoc;
        try (MongoClient mongoClient = MongoClients.create(Configuration.MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB);
            MongoCollection<Document> collection = database.getCollection(Configuration.MONGODB_AUTHOR);
            MongoCursor<Document> cursor;
            //Bson filter = Filters.regex("Name", "^(?)" + nameToSearch); //da togliere era il vecchio filtro
            Bson filter = new Document("authorName",new Document("$regex",nameToSearch).append("$options","i"));
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


    public void setAuthorLabels(List<Document> listaAuthors,int i) {
        int documentSize = listaAuthors.get(0).size() - 2;
        List<String> listaLabelNames = new ArrayList<>();
        listaLabelNames.add("authorId");
        listaLabelNames.add("authorName");

        double copiaStartingX = cercaUtenteButton.getLayoutX() - 200;
        for (int j = 0;j < documentSize; j++) {
            Label currentLabel = new Label();
            Object valore = listaAuthors.get(i).get(listaLabelNames.get(j));
            currentLabel.setText((String.valueOf(valore)));
            currentLabel.setLayoutX(copiaStartingX + j*10);
            currentLabel.setLayoutY(cercaUtenteButton.getLayoutY() + 50 + i * 10);
            currentLabel.setMaxWidth(50);
            copiaStartingX += currentLabel.getMaxWidth();
            anchorPane.getChildren().add(currentLabel);
        }
    }

    @FXML
    public void onCercaUtenteClick(ActionEvent actionEvent) {

        nameToSearch = authorToSearchTextField.getText();
        if(nameToSearch.isBlank()) nameToSearch = null;
        System.out.println(nameToSearch); //solo per debug sarà da togliere
        pageNumber = 0;
        searchInDBAndLoadInTableView(nameToSearch,pageNumber);

        /*String uri = Configuration.MONGODB_URL;
        List<Document> listaAuthors = new ArrayList<>();
        System.out.println(999);
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase(Configuration.MONGODB_DB); //da scegliere il nome uguale per tutti
            MongoCollection<Document> collectionAuthor = database.getCollection(Configuration.MONGODB_AUTHOR);

            Bson filterAuthor = Filters.regex("authorName", "^" + utenteCercato.getText());
            MongoCursor<Document> cursorAuthor = collectionAuthor.find(filterAuthor).iterator();

            while (cursorAuthor.hasNext())
                listaAuthors.add(cursorAuthor.next());

            for (int i = 0; i < listaAuthors.size(); i++) {
                setReportedLabels(listaAuthors, i);
            }
        }*/
    }

    public void setReportedLabels(List<Document> listaAuthors,int i) {
        int documentSize = listaAuthors.get(0).size() - 2;
        List<String> listaLabelNames = new ArrayList<>();
        listaLabelNames.add("authorId");
        listaLabelNames.add("authorName");

        double copiaStartingX = cercaUtenteButton.getLayoutX();
        for (int j = 0;j < documentSize; j++) {
            Label currentLabel = new Label();
            Object valore = listaAuthors.get(i).get(listaLabelNames.get(j));
            currentLabel.setText((String.valueOf(valore)));
            currentLabel.setLayoutX(copiaStartingX + j*10);
            currentLabel.setLayoutY(cercaUtenteButton.getLayoutY() + 50 + i * 10);
            currentLabel.setMaxWidth(50);
            copiaStartingX += currentLabel.getMaxWidth();
            anchorPane.getChildren().add(currentLabel);
        }
    }

    public void cambiaSchermata(ActionEvent actionEvent,String nomeSchermata) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(nomeSchermata));
        stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setScene(scene);
        stage.show();
    }
}
