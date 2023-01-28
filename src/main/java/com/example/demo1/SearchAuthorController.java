package com.example.demo1;

import com.example.demo1.gui.Utils;
import com.example.demo1.service.AuthorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchAuthorController implements Initializable {
    @FXML
    public Button cercaUtenteButton;
    public TextField authorToSearchTextField;
    private Stage stage;
    private String authorNameClicked; //qui ci salvo l'utente della tabella che è stato clickato e che quindi si vuole promuovere
    public AnchorPane anchorPane;
    private Integer pageNumber = 0;
    private String nameToSearch = null;


    private ClassTableAuthor tableViewAuthor = new ClassTableAuthor();

    private ClassTableAuthor tabella;

    @FXML
    public void onLogoutClick(ActionEvent actionEvent){
        Utils.changeScene(actionEvent,"Login.fxml");
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
    public void onBackClick(ActionEvent actionEvent){
        Utils.changeScene(actionEvent,"loggato.fxml");
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (DataSingleton.getInstance().getTypeOfUser().equals("moderator")) {
            Button promoteAuthorButton = new Button("PROMOTE AUTHOR");
            promoteAuthorButton.setLayoutX(164);
            promoteAuthorButton.setLayoutY(140);
            promoteAuthorButton.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
                AuthorService.updatePromotion(authorNameClicked,1);
                authorNameClicked = null;
            });
            anchorPane.getChildren().add(promoteAuthorButton);
        }
        createTableView(tableViewAuthor, DataSingleton.getInstance().getTypeOfUser().equals("moderator"));
        /*
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
        */

        //questa parte sotto è quella che setta il Button per la promozione

    }

    public void createTableView (ClassTableAuthor TableViewObject, Boolean showPromotion) {
        TableViewObject.initializeTableView();
        searchInDBAndLoadInTableView(nameToSearch,pageNumber);
        if(showPromotion)
            TableViewObject.setTableWithPromotion();
        else
            TableViewObject.setTableWithoutPromotion();

        tabella = TableViewObject;
        setEventForTableCells();
        anchorPane.getChildren().add(TableViewObject.getTabellaDB());
    }

    public void searchInDBAndLoadInTableView(String nameToSearch, Integer pageNumber){
        tableViewAuthor.resetObservableArrayList();
        AuthorService.searchAuthors(nameToSearch,pageNumber*10,10).forEach( author -> {
            AuthorTableView authorTableView = new AuthorTableView(author.getName(), author.getPromotion(),
                    new ClassTableAuthor.CustomImageAuthor(new ImageView(Configuration.AVATAR.get(author.getImage() - 1))).getImage());
            tableViewAuthor.addToObservableArrayList(authorTableView);
        });
        tableViewAuthor.setItems();
    }

    /*
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
    */

    @FXML
    public void onCercaUtenteClick(ActionEvent actionEvent) {

        nameToSearch = authorToSearchTextField.getText();
        if(nameToSearch.isBlank()) nameToSearch = null;
        pageNumber = 0;
        searchInDBAndLoadInTableView(nameToSearch,pageNumber);
    }

    public void setEventForTableCells() {
        tabella.getTabellaDB().addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { //evento per il mouse clickato
                    TableCell cell = findCell(evt,tabella.getTabellaDB());
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
}
