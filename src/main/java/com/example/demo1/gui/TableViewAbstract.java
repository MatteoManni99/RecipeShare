package com.example.demo1.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public abstract class TableViewAbstract {

    public TableView<RowTableView> table;
    private ObservableList<RowTableView> ol;
    private final DataSingleton data = DataSingleton.getInstance();

    public void setTable() {

    }

    public void setEventForTableCells() {
        table.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> {
                    TableCell cell = Utils.findCell(evt, table);
                    if (cell != null && !cell.isEmpty()) {
                        if(cell.getTableColumn().getText().equals("Name")){
                            data.setRecipeName(cell.getText());
                            Utils.changeScene(evt,"Recipe.fxml");
                        }
                        if(cell.getTableColumn().getText().equals("Author")){
                            data.setOtherAuthorName(cell.getText());
                            Utils.changeScene(evt,"Author.fxml");
                        }
                        evt.consume();
                    }
                }
        );
    }

    public void setItems() {table.setItems(ol);}

    public TableView<RowTableView> getTable() {
        return table;
    }

    public void resetObservableArrayList() {
        ol = FXCollections.observableArrayList();
    }

    public void addToObservableArrayList(RowTableView recipe) {
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
