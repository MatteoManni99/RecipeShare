package com.example.demo1.gui.tableview;

import com.example.demo1.gui.row.RowTableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class TableViewRecipeRating extends TableViewAbstract {
    private final TableColumn<RowTableView, String> nameCol;
    private final TableColumn<RowTableView, ImageView> imageCol;
    private final TableColumn<RowTableView, Double> ratingCol;


    public TableViewRecipeRating() {
        table = new TableView<>();

        nameCol = new TableColumn<>("Name");
        ratingCol = new TableColumn<>("Rating");
        imageCol = new TableColumn<>("Image");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));

        table.setPrefHeight(350);
        table.setPrefWidth(350);
        table.setLayoutX(50);
        table.setLayoutY(230);
    }

    @Override
    public void setTable() {
        setColumns(imageCol, nameCol, ratingCol);
    }

}
