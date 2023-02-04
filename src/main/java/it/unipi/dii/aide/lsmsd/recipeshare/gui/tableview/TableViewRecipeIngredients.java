package it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview;

import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowTableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableViewRecipeIngredients extends TableViewAbstract {
    private final TableColumn<RowTableView, String> countCol;
    private final TableColumn<RowTableView, String> ingredientCol;

    public TableViewRecipeIngredients() {
        table = new TableView<>();

        ingredientCol = new TableColumn<>("Ingredient");
        countCol = new TableColumn<>("Count");

        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));
        ingredientCol.setCellValueFactory(new PropertyValueFactory<>("ingredient"));

        table.setPrefHeight(270);
        table.setPrefWidth(150);
        table.setLayoutX(50);
        table.setLayoutY(230);
    }
    @Override
    public void setTable() {setColumns(ingredientCol, countCol);}


}
