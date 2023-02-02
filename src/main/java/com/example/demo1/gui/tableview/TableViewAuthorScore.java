package com.example.demo1.gui.tableview;

import com.example.demo1.gui.row.RowTableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class TableViewAuthorScore extends TableViewAbstract {

    private final TableColumn<RowTableView, String> authorNameCol;
    private final TableColumn<RowTableView, Double> scoreCol;
    private final TableColumn<RowTableView, ImageView> imageCol;

    public TableViewAuthorScore() {
        table = new TableView<>();

        authorNameCol = new TableColumn<>("Author");
        imageCol = new TableColumn<>("Image");
        scoreCol = new TableColumn<>("Score");

        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));

        table.setPrefHeight(500);
        table.setPrefWidth(350);
        table.setLayoutX(20);
        table.setLayoutY(100);
    }

    public void setTable() {table.getColumns().addAll(imageCol, authorNameCol, scoreCol);}

}