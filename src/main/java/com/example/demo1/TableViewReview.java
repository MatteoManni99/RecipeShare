package com.example.demo1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TableViewReview {
    public TableView<Review> table;
    private ObservableList<Review> reviews;
    private TableColumn descriptionCol;
    private TableColumn authorNameCol;
    private TableColumn ratingCol;
    private Stage stage;
    private DataSingleton data = DataSingleton.getInstance();

    public void initializeTableView() {
        table = new TableView<>();
        descriptionCol = new TableColumn<Recipe, String>("Review");
        authorNameCol = new TableColumn<Recipe, String>("AuthorName");
        ratingCol = new TableColumn<Recipe, String>("Rating");

        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("review"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));

        table.setPrefHeight(200);
        table.setPrefWidth(500);
        table.setLayoutX(480);
        table.setLayoutY(370);

        //table.pol(TableView.C);
    }

    public void setTableDB() {
        table.getColumns().addAll(ratingCol, authorNameCol, descriptionCol);
    }

    public void setItems(){
        table.setItems(reviews);
    }

    public TableView<Review> getTableDB() {
        return table;
    }

    public void resetObservableArrayList() {
        reviews = FXCollections.observableArrayList();
    }

    public void addToObservableArrayList(Review review){
        reviews.add(review);
    }
}
