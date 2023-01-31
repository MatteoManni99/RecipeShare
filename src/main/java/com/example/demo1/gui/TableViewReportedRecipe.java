package com.example.demo1.gui;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class TableViewReportedRecipe extends TableViewAbstract {
    private TableColumn nameCol;
    private TableColumn authorNameCol;
    private TableColumn reporterNameCol;
    private TableColumn dateReportingCol;
    private TableColumn imageCol;

    public void initializeTableView() {
        super.table = new TableView<>();

        nameCol = new TableColumn<RowReportedRecipe, String>("Name");
        authorNameCol = new TableColumn<RowReportedRecipe, String>("AuthorName");
        reporterNameCol = new TableColumn<RowReportedRecipe, String>("ReporterName");
        dateReportingCol = new TableColumn<RowReportedRecipe, String>("DateReporting");
        imageCol = new TableColumn<ImageTableView, ImageView>("Image");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        reporterNameCol.setCellValueFactory(new PropertyValueFactory<>("reporterName"));
        dateReportingCol.setCellValueFactory(new PropertyValueFactory<>("dateReporting"));
        imageCol.setCellValueFactory(new PropertyValueFactory<ImageTableView,ImageView>("image"));

        super.table.setPrefHeight(400);
        super.table.setPrefWidth(500);
        super.table.setLayoutX(40);
        super.table.setLayoutY(200);
    }


    public void setTabellaDB() {
        super.table.getColumns().addAll(imageCol, nameCol, authorNameCol, reporterNameCol, dateReportingCol);
    }

}
