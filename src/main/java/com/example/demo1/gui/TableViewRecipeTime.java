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
import javafx.stage.Stage;

public class TableViewRecipeTime {

    public TableView<RowRecipeTime> tabellaDB;
    private ObservableList<RowRecipeTime> ol;
    private TableColumn nameCol;
    private TableColumn timeCol;
    private TableColumn imageCol;
    private TableColumn ratingCol;

    private Stage stage;

    private DataSingleton data = DataSingleton.getInstance();

    public void initializeTableView() {
        tabellaDB = new TableView<>();

        nameCol = new TableColumn<RowRecipe, String>("Name");
        timeCol = new TableColumn<RowRecipe, Integer>("Time");
        ratingCol = new TableColumn<RowRecipe, Double>("Rating");
        imageCol = new TableColumn<TableViewRecipe.CustomImage, ImageView>("Image");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("totaltime"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        imageCol.setCellValueFactory(new PropertyValueFactory<TableViewRecipe.CustomImage,ImageView>("image"));

        tabellaDB.setPrefHeight(400);
        tabellaDB.setPrefWidth(600);
        tabellaDB.setLayoutX(150);
        tabellaDB.setLayoutY(150);
    }


    public void setTable() {
        tabellaDB.getColumns().addAll(imageCol, nameCol, timeCol, ratingCol);
    }

    public void setItems(){
        tabellaDB.setItems(ol);
    }

    public TableView<RowRecipeTime> getTable() {
        return tabellaDB;
    }


    public void resetObservableArrayList(){
        ol = FXCollections.observableArrayList();
    }

    public void addToObservableArrayList(RowRecipeTime recipe){
        ol.add(recipe);
    }

    public void setEventForTableCells() {
        tabellaDB.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { //evento per il mouse clickato
                    TableCell cell = findCell(evt,tabellaDB);
                    if (cell != null && !cell.isEmpty()) {
                        if(cell.getTableColumn().getText().equals("Name")){
                            //System.out.println(cell.getText()); // Andare alla pagina relativa alla ricetta
                            data.setRecipeName(cell.getText());
                            Utils.changeScene(evt,"Recipe.fxml");
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

    static class CustomImage {
        private ImageView image;
        CustomImage(ImageView img) {
            this.image = img;
            this.image.setFitHeight(50);
            this.image.setFitWidth(50);
        }
        public void setImage(ImageView value) {image = value;}
        public ImageView getImage() {return image;}
    }
}
