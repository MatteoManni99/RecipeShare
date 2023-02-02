package com.example.demo1.gui.tableview;

import com.example.demo1.gui.row.RowTableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableViewReview extends TableViewAbstract {
    private final TableColumn<RowTableView, String> descriptionCol;
    private final TableColumn<RowTableView, String> authorNameCol;
    private final TableColumn<RowTableView, Double> ratingCol;

    public TableViewReview() {
        table = new TableView<>();
        descriptionCol = new TableColumn<>("Review");
        authorNameCol = new TableColumn<>("Author");
        ratingCol = new TableColumn<>("Rating");

        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("review"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));

        table.setPrefHeight(200);
        table.setPrefWidth(500);
        table.setLayoutX(480);
        table.setLayoutY(370);

        //table.pol(TableView.C);
    }

    public void setTable() {table.getColumns().addAll(ratingCol, authorNameCol, descriptionCol);}

}
