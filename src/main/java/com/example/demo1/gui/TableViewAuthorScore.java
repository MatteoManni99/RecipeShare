package com.example.demo1.gui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class TableViewAuthorScore extends TableViewAbstract {

    private final TableColumn authorNameCol;
    private final TableColumn scoreCol;
    private final TableColumn imageCol;

    private DataSingleton data = DataSingleton.getInstance();

    public TableViewAuthorScore() {
        super.table = new TableView<>();

        authorNameCol = new TableColumn<>("Author");
        imageCol = new TableColumn<ImageTableView, ImageView>("Image");
        scoreCol = new TableColumn<>("Score");

        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));

        super.table.setPrefHeight(400);
        super.table.setPrefWidth(300);
        super.table.setLayoutX(600);
        super.table.setLayoutY(200);
    }

    public void setTable() {super.table.getColumns().addAll(authorNameCol, scoreCol);}

}