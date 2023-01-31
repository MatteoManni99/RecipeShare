package com.example.demo1.gui;

import com.example.demo1.Configuration;
import com.example.demo1.service.AuthorService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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


    private TableViewAbstract tableViewAuthor = new TableViewAuthor();

    private TableViewAbstract tabella;

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
        createTableView();
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

    public void createTableView () {
        tableViewAuthor.getTable().setLayoutX(220);
        tableViewAuthor.getTable().setLayoutY(150);
        tableViewAuthor.getTable().setPrefHeight(400);
        tableViewAuthor.getTable().setPrefWidth(210);
        tableViewAuthor.setEventForTableCells();
        searchInDBAndLoadInTableView(nameToSearch,pageNumber);
        tableViewAuthor.setTable();
        tabella = tableViewAuthor;
        setEventForTableCells();
        anchorPane.getChildren().add(tableViewAuthor.getTable());
    }

    public void searchInDBAndLoadInTableView(String nameToSearch, Integer pageNumber){
        tableViewAuthor.resetObservableArrayList();
        AuthorService.searchAuthors(nameToSearch,pageNumber*10,10).forEach( author -> {
            RowAuthor authorTableView = new RowAuthor(author.getName(), author.getPromotion(),
                    new ImageTableView(new ImageView(Configuration.AVATAR.get(author.getImage() - 1))).getImage());
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
        tabella.getTable().addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { //evento per il mouse clickato
                    TableCell cell = Utils.findCell(evt,tabella.getTable());
                    if (cell != null && !cell.isEmpty()) {
                        if(cell.getTableColumn().getText().equals("Author")){
                            authorNameClicked = cell.getText();
                            System.out.println("COSA SERVE??");
                        }
                        evt.consume();
                    }
                }
        );
    }
}
