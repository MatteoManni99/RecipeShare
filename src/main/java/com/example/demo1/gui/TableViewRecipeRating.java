package com.example.demo1.gui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class TableViewRecipeRating extends TableViewAbstract {
    private final TableColumn<RowTableView, String> nameCol;
    private final TableColumn imageCol;
    private final TableColumn<RowTableView, Double> ratingCol;


    public TableViewRecipeRating() {
        super.table = new TableView<>();

        nameCol = new TableColumn<>("Name");
        ratingCol = new TableColumn<>("Rating");
        imageCol = new TableColumn<ImageTableView, ImageView>("Image");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        imageCol.setCellValueFactory(new PropertyValueFactory<ImageTableView,ImageView>("image"));

        super.table.setPrefHeight(350);
        super.table.setPrefWidth(350);
        super.table.setLayoutX(50);
        super.table.setLayoutY(230);
    }

    @Override
    public void setTable() {
        super.table.getColumns().addAll(imageCol, nameCol, ratingCol);
    }

}
