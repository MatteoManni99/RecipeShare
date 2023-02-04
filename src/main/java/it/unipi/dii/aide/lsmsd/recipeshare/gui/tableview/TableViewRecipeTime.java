package it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview;

import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowTableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class TableViewRecipeTime extends TableViewAbstract {
    private final TableColumn<RowTableView, String> nameCol;
    private final TableColumn<RowTableView, String> timeCol;
    private final TableColumn<RowTableView, ImageView> imageCol;
    private final TableColumn<RowTableView, Double> ratingCol;

    public TableViewRecipeTime() {
        table = new TableView<>();

        nameCol = new TableColumn<>("Name");
        timeCol = new TableColumn<>("Time");
        ratingCol = new TableColumn<>("Rating");
        imageCol = new TableColumn<>("Image");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("totaltime"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));

        table.setPrefHeight(350);
        table.setPrefWidth(500);
        table.setLayoutX(50);
        table.setLayoutY(230);
    }

    @Override
    public void setTable() {
        setColumns(imageCol, nameCol, timeCol, ratingCol);
    }


}
