package com.example.demo1.gui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class TableViewRecipe extends TableViewAbstract {
    private final TableColumn<RowTableView, String> nameCol;
    private final TableColumn<RowTableView, String> authorNameCol;
    private final TableColumn<RowTableView, ImageView> imageCol;

    public TableViewRecipe() {
        table = new TableView<>();

        nameCol = new TableColumn<>("Name");
        authorNameCol = new TableColumn<>("Author");
        imageCol = new TableColumn<>("Image");


        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));

        table.setPrefHeight(400);
        table.setPrefWidth(600);
        table.setLayoutX(150);
        table.setLayoutY(150);
    }


    public void setTable() {table.getColumns().addAll(imageCol, nameCol, authorNameCol);}
    public void setTableWithoutAuthorNameCol() {table.getColumns().addAll(imageCol, nameCol);}

}
