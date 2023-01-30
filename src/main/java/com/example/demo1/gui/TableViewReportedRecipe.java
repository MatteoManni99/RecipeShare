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

import java.util.List;

public class TableViewReportedRecipe {
    public TableView<RowReportedRecipe> tabellaDB;
    private ObservableList<RowReportedRecipe> ol;
    private TableColumn nameCol;
    private TableColumn authorNameCol;
    private TableColumn reporterNameCol;
    private TableColumn dateReportingCol;
    private TableColumn imageCol;

    public void initializeTableView() {
        tabellaDB = new TableView<>();

        nameCol = new TableColumn<RowReportedRecipe, String>("Name");
        authorNameCol = new TableColumn<RowReportedRecipe, String>("AuthorName");
        reporterNameCol = new TableColumn<RowReportedRecipe, String>("ReporterName");
        dateReportingCol = new TableColumn<RowReportedRecipe, String>("DateReporting");
        imageCol = new TableColumn<CustomImage, ImageView>("Image");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        reporterNameCol.setCellValueFactory(new PropertyValueFactory<>("reporterName"));
        dateReportingCol.setCellValueFactory(new PropertyValueFactory<>("dateReporting"));
        imageCol.setCellValueFactory(new PropertyValueFactory<CustomImage,ImageView>("image"));

        tabellaDB.setPrefHeight(400);
        tabellaDB.setPrefWidth(500);
        tabellaDB.setLayoutX(40);
        tabellaDB.setLayoutY(200);
    }


    public void setTabellaDB() {
        tabellaDB.getColumns().addAll(imageCol, nameCol, authorNameCol, reporterNameCol, dateReportingCol);
    }

    public void setItems(){
        tabellaDB.setItems(ol);
    }

    public TableView<RowReportedRecipe> getTabellaDB() {
        return tabellaDB;
    }

    public void resetObservableArrayList(){
        ol = FXCollections.observableArrayList();
    }

    public void setObservableArrayList(List<RowReportedRecipe> list){
        ol = FXCollections.observableList(list);
    }
    public void addToObservableArrayList(RowReportedRecipe reportedRecipe){
        ol.add(reportedRecipe);
    }

    public void setEventForTableCells() {
        tabellaDB.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { //evento per il mouse clickato
                TableCell cell = findCell(evt,tabellaDB);
                if (cell != null && !cell.isEmpty()) {
                    if(cell.getTableColumn().getText().equals("Name")){
                        DataSingleton.getInstance().setRecipeName(cell.getText());
                        Utils.changeScene(evt,"Recipe.fxml");
                    }
                    if(cell.getTableColumn().getText().equals("AuthorName") || cell.getTableColumn().getText().equals("ReporterName")){
                        System.out.println(cell.getText()); // Andare alla pagina relativa all'autore
                        DataSingleton.getInstance().setOtherAuthorName(cell.getText());
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
