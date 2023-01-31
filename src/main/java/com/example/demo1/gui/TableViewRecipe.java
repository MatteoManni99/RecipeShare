package com.example.demo1.gui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class TableViewRecipe extends TableViewAbstract {
    private final TableColumn<RowTableView, String> nameCol;
    private final TableColumn<RowTableView, String> authorNameCol;
    private final TableColumn imageCol;

    public TableViewRecipe() {
        super.table = new TableView<>();

        nameCol = new TableColumn<>("Name");
        authorNameCol = new TableColumn<>("Author");
        imageCol = new TableColumn<ImageTableView, ImageView>("Image");


        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        imageCol.setCellValueFactory(new PropertyValueFactory<ImageTableView,ImageView>("image"));

        super.table.setPrefHeight(400);
        super.table.setPrefWidth(600);
        super.table.setLayoutX(150);
        super.table.setLayoutY(150);
    }


    public void setTable() {
        super.table.getColumns().addAll(imageCol, nameCol, authorNameCol);
    }


}
