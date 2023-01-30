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

public class TableViewAuthorScore {

    public TableView<RowAuthorScore> tabellaDB;
    private ObservableList<RowAuthorScore> ol;
    private TableColumn<RowAuthorScore, String> authorNameCol;
    private TableColumn<RowAuthorScore, Double> scoreCol;
    private TableColumn imageCol;

    private DataSingleton data = DataSingleton.getInstance();

    public void initializeTableView() {
        tabellaDB = new TableView<>();

        authorNameCol = new TableColumn<>("Name");
        imageCol = new TableColumn<TableViewRecipe.CustomImage, ImageView>("Image");
        scoreCol = new TableColumn<>("Score");

        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));

        tabellaDB.setPrefHeight(400);
        tabellaDB.setPrefWidth(300);
        tabellaDB.setLayoutX(600);
        tabellaDB.setLayoutY(200);
    }

    public void setTable() {tabellaDB.getColumns().addAll(authorNameCol, scoreCol);}

    public void setItems(){tabellaDB.setItems(ol);}

    public TableView<RowAuthorScore> getTabellaDB() {
        return tabellaDB;
    }

    public void resetObservableArrayList(){
        ol = FXCollections.observableArrayList();
    }
    public void addToObservableArrayList(RowAuthorScore author){
        ol.add(author);
    }
    public void setEventForTableCells() {
        tabellaDB.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { //evento per il mouse clickato
                    TableCell cell = findCell(evt,tabellaDB);
                    if (cell != null && !cell.isEmpty()) {
                        if(cell.getTableColumn().getText().equals("Name")){
                            data.setOtherAuthorName(cell.getText());
                            Utils.changeScene(evt,"Author.fxml");
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
    static class CustomImageAuthor {
        private ImageView image;
        CustomImageAuthor(ImageView img) {
            this.image = img;
            this.image.setFitHeight(50);
            this.image.setFitWidth(50);
        }
        public void setImage(ImageView value) {image = value;}
        public ImageView getImage() {return image;}
    }
}