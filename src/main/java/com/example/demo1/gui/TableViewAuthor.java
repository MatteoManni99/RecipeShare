package com.example.demo1.gui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class TableViewAuthor extends TableViewAbstract{
    private final TableColumn<RowTableView, Integer> promotionCol;
    private final TableColumn<RowTableView, String> authorNameCol;
    private final TableColumn<RowTableView, ImageView> imageCol;


    public TableViewAuthor() {
        table = new TableView<>();

        authorNameCol = new TableColumn<>("Author");
        promotionCol = new TableColumn<>("Promotion");
        imageCol = new TableColumn<>("Image");

        promotionCol.setCellValueFactory(new PropertyValueFactory<>("promotion"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));

        table.setPrefHeight(400);
        table.setPrefWidth(300);
        table.setLayoutX(600);
        table.setLayoutY(200);
    }

    public void setTableWithPromotion() {
        table.getColumns().addAll(imageCol, authorNameCol, promotionCol);
    }

    public void setTableWithoutPromotion() {
        table.getColumns().addAll(imageCol, authorNameCol);
    }

    public void setTable(){
        if (data.getTypeOfUser().equals("moderator")) setTableWithPromotion();
        else setTableWithoutPromotion();
    }

}

