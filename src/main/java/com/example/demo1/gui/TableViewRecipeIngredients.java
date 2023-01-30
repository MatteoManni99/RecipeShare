package com.example.demo1.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableViewRecipeIngredients {
    public TableView<RowRecipeIngredients> tabellaDB;
    private ObservableList<RowRecipeIngredients> ol;
    private TableColumn countCol;
    private TableColumn ingredientCol;

    public void initializeTableView() {
        tabellaDB = new TableView<>();

        ingredientCol = new TableColumn<RowRecipe, String>("Ingredient");
        countCol = new TableColumn<RowRecipe, Integer>("Count");

        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        ingredientCol.setCellValueFactory(new PropertyValueFactory<>("ingredient"));

        tabellaDB.setPrefHeight(270);
        tabellaDB.setPrefWidth(150);
        tabellaDB.setLayoutX(50);
        tabellaDB.setLayoutY(230);
    }


    public void setTable() {
        tabellaDB.getColumns().addAll(ingredientCol, countCol);
    }

    public void setItems(){
        tabellaDB.setItems(ol);
    }

    public TableView<RowRecipeIngredients> getTable() {
        return tabellaDB;
    }


    public void resetObservableArrayList(){
        ol = FXCollections.observableArrayList();
    }

    public void addToObservableArrayList(RowRecipeIngredients recipe){
        ol.add(recipe);
    }

}
