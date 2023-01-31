package com.example.demo1.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class TableViewAuthor {

    public TableView<RowAuthor> tabellaDB;
    private ObservableList<RowAuthor> ol;
    private TableColumn<RowAuthor, Integer> promotionCol;
    private TableColumn<RowAuthor, String> authorNameCol;
    private TableColumn imageCol;

    private DataSingleton data = DataSingleton.getInstance();

    public void initializeTableView() {
        tabellaDB = new TableView<>();

        authorNameCol = new TableColumn<>("Name");
        promotionCol = new TableColumn<>("Promotion");
        imageCol = new TableColumn<ImageTableView, ImageView>("Image");

        promotionCol.setCellValueFactory(new PropertyValueFactory<>("promotion"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));

        tabellaDB.setPrefHeight(400);
        tabellaDB.setPrefWidth(300);
        tabellaDB.setLayoutX(600);
        tabellaDB.setLayoutY(200);
    }

    public void setTableWithPromotion() {
        tabellaDB.getColumns().addAll(imageCol, authorNameCol, promotionCol);
    }

    public void setTableWithoutPromotion() {
        tabellaDB.getColumns().addAll(imageCol, authorNameCol);
    }

    public void setItems() {
        tabellaDB.setItems(ol);
    }

    public TableView<RowAuthor> getTabellaDB() {
        return tabellaDB;
    }

    public void resetObservableArrayList() {
        ol = FXCollections.observableArrayList();
    }

    public void addToObservableArrayList(RowAuthor author) {
        ol.add(author);
    }

    public void setEventForTableCells() {
        tabellaDB.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { //evento per il mouse clickato
                    TableCell cell = findCell(evt, tabellaDB);
                    if (cell != null && !cell.isEmpty()) {
                        if (cell.getTableColumn().getText().equals("Name")) {
                            data.setOtherAuthorName(cell.getText());
                            Utils.changeScene(evt, "Author.fxml");
                        }
                        evt.consume();
                    }
                }
        );
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

