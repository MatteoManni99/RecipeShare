package com.example.demo1.gui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableViewRecipeIngredients extends TableViewRecipeQuery {
    private final TableColumn countCol;
    private final TableColumn ingredientCol;

    public TableViewRecipeIngredients() {
        super.table = new TableView<>();

        ingredientCol = new TableColumn<RowRecipe, String>("Ingredient");
        countCol = new TableColumn<RowRecipe, Integer>("Count");

        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        ingredientCol.setCellValueFactory(new PropertyValueFactory<>("ingredient"));

        super.table.setPrefHeight(270);
        super.table.setPrefWidth(150);
        super.table.setLayoutX(50);
        super.table.setLayoutY(230);
    }
    @Override
    public void setTable() {
        super.table.getColumns().addAll(ingredientCol, countCol);
    }


}
