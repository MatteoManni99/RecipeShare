package com.example.demo1.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public abstract class TableViewRecipeQuery {

    public TableView<RowRecipeQuery> table;
    private ObservableList<RowRecipeQuery> ol;

    public void setTable() {

    }

    public void setEventForTableCells() {

    }


    public void setItems() {table.setItems(ol);}

    public TableView<RowRecipeQuery> getTable() {
        return table;
    }

    public void resetObservableArrayList() {
        ol = FXCollections.observableArrayList();
    }

    public void addToObservableArrayList(RowRecipeQuery recipe) {
        ol.add(recipe);
    }

    private static TableCell findCell(MouseEvent event, TableView table) { //metodo chiamato dall'evento
        Node node = event.getPickResult().getIntersectedNode();
        // go up in node hierarchy until a cell is found or we can be sure no cell was clicked
        while (node != table && !(node instanceof TableCell)) {
            node = node.getParent();
        }
        return node instanceof TableCell ? (TableCell) node : null;
    }
}
