package com.example.demo1.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class TableViewReview {
    public TableView<ReviewTableView> table;
    private ObservableList<ReviewTableView> reviews;
    private TableColumn descriptionCol;
    private TableColumn authorNameCol;
    private TableColumn ratingCol;
    private Stage stage;
    private DataSingleton data = DataSingleton.getInstance();

    public void initializeTableView() {
        table = new TableView<>();
        descriptionCol = new TableColumn<RecipeTableView, String>("Review");
        authorNameCol = new TableColumn<RecipeTableView, String>("AuthorName");
        ratingCol = new TableColumn<RecipeTableView, String>("Rating");

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

    public TableView<ReviewTableView> getTableDB() {
        return table;
    }

    public void resetObservableArrayList() {
        reviews = FXCollections.observableArrayList();
    }

    public void addToObservableArrayList(ReviewTableView review){
        reviews.add(review);
    }
}
