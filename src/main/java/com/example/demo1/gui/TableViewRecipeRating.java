package com.example.demo1.gui;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class TableViewRecipeRating extends TableViewRecipeQuery{
    private final TableColumn nameCol;
    private final TableColumn imageCol;
    private final TableColumn ratingCol;
    private final DataSingleton data = DataSingleton.getInstance();

    public TableViewRecipeRating() {
        super.table = new TableView<>();

        nameCol = new TableColumn<RowRecipe, String>("Name");
        ratingCol = new TableColumn<RowRecipe, Double>("Rating");
        imageCol = new TableColumn<TableViewRecipe.CustomImage, ImageView>("Image");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        imageCol.setCellValueFactory(new PropertyValueFactory<ImageTableView,ImageView>("image"));

        super.table.setPrefHeight(350);
        super.table.setPrefWidth(350);
        super.table.setLayoutX(50);
        super.table.setLayoutY(230);
    }

    @Override
    public void setTable() {
        super.table.getColumns().addAll(imageCol, nameCol, ratingCol);
    }
    @Override
    public void setEventForTableCells() {
        table.addEventHandler(MouseEvent.MOUSE_CLICKED, evt -> { //evento per il mouse clickato
                    TableCell cell = Utils.findCell(evt, table);
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

}
