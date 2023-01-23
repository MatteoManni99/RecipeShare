package com.example.demo1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ClassTableAuthor {

    public TableView<Author> tabellaDB;
    private ObservableList<Author> ol;
    private TableColumn promotionCol;
    private TableColumn authorNameCol;
    private TableColumn imageCol;

    private Stage stage;

    public void initializeTableView() {
        tabellaDB = new TableView<>();

        authorNameCol = new TableColumn<Author, String>("Name");
        promotionCol = new TableColumn<Author, Integer>("Promotion");
        imageCol = new TableColumn<ClassForTableView.CustomImage, ImageView>("Image");

        promotionCol.setCellValueFactory(new PropertyValueFactory<>("promotion"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        imageCol.setCellValueFactory(new PropertyValueFactory<ClassForTableView.CustomImage,ImageView>("image"));

        tabellaDB.setPrefHeight(400);
        tabellaDB.setPrefWidth(300);
        tabellaDB.setLayoutX(150);
        tabellaDB.setLayoutY(250);
    }

    public void setTabellaDB() {
        tabellaDB.getColumns().addAll(imageCol, authorNameCol, promotionCol);
    }
    public void setItems(){
        tabellaDB.setItems(ol);
    }

    public TableView<Author> getTabellaDB() {
        return tabellaDB;
    }

    public void resetObservableArrayList(){
        ol = FXCollections.observableArrayList();
    }
    public void addToObservableArrayList(Author author){
        ol.add(author);
    }

    static class CustomImageAuthor {
        private ImageView image;
        CustomImageAuthor(ImageView img) {
            this.image = img;
            this.image.setFitHeight(50);
            this.image.setFitWidth(50);
        }
        public void setImage(ImageView value) {image = value;}
        public ImageView getImage() {return image;}
    }
}
