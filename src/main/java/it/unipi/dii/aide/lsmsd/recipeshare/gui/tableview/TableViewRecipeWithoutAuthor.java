package it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview;

import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowTableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class TableViewRecipeWithoutAuthor extends TableViewAbstract{
    private final TableColumn<RowTableView, String> nameCol;
    private final TableColumn<RowTableView, ImageView> imageCol;

    public TableViewRecipeWithoutAuthor() {
        table = new TableView<>();
        nameCol = new TableColumn<>("Name");

        imageCol = new TableColumn<>("Image");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));
        table.setPrefHeight(400);
        table.setPrefWidth(600);
        table.setLayoutX(150);
        table.setLayoutY(150);
        }

    @Override
    public void setTable() {setColumns(imageCol, nameCol);}

}
