package com.example.demo1.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class ClassTableAuthor {

    public TableView<AuthorTableView> tabellaDB;
    private ObservableList<AuthorTableView> ol;
    private TableColumn<AuthorTableView, Integer> promotionCol;
    private TableColumn<AuthorTableView, String> authorNameCol;
    private TableColumn imageCol;


    public void initializeTableView() {
        tabellaDB = new TableView<>();

        authorNameCol = new TableColumn<>("Name");
        promotionCol = new TableColumn<>("Promotion");
        imageCol = new TableColumn<ClassForTableView.CustomImage, ImageView>("Image");

        promotionCol.setCellValueFactory(new PropertyValueFactory<>("promotion"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));

        tabellaDB.setPrefHeight(400);
        tabellaDB.setPrefWidth(300);
        tabellaDB.setLayoutX(150);
        tabellaDB.setLayoutY(200);
    }

    public void setTableWithPromotion() {tabellaDB.getColumns().addAll(imageCol, authorNameCol, promotionCol);}

    public void setTableWithoutPromotion() {tabellaDB.getColumns().addAll(imageCol, authorNameCol);}
    public void setItems(){tabellaDB.setItems(ol);}

    public TableView<AuthorTableView> getTabellaDB() {
        return tabellaDB;
    }

    public void resetObservableArrayList(){
        ol = FXCollections.observableArrayList();
    }
    public void addToObservableArrayList(AuthorTableView author){
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
