package com.example.demo1.gui.tableview;

import com.example.demo1.gui.DataSingleton;
import com.example.demo1.gui.row.RowTableView;
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
    @Override
    public void setTable(){
        if (DataSingleton.getInstance().getTypeOfUser().equals("moderator")) setColumns(imageCol, authorNameCol, promotionCol);
        else setColumns(imageCol, authorNameCol);
    }


}

