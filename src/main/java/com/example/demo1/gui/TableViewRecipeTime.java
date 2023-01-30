package com.example.demo1.gui;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class TableViewRecipeTime extends TableViewRecipeQuery {
    private final TableColumn nameCol;
    private final TableColumn timeCol;
    private final TableColumn imageCol;
    private final TableColumn ratingCol;

    private final DataSingleton data = DataSingleton.getInstance();

    public TableViewRecipeTime() {
        super.table = new TableView<>();

        nameCol = new TableColumn<RowRecipe, String>("Name");
        timeCol = new TableColumn<RowRecipe, Integer>("Time");
        ratingCol = new TableColumn<RowRecipe, Double>("Rating");
        imageCol = new TableColumn<TableViewRecipe.CustomImage, ImageView>("Image");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("totaltime"));
        ratingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        imageCol.setCellValueFactory(new PropertyValueFactory<ImageTableView,ImageView>("image"));

        super.table.setPrefHeight(350);
        super.table.setPrefWidth(500);
        super.table.setLayoutX(50);
        super.table.setLayoutY(230);
    }

    @Override
    public void setTable() {
        super.table.getColumns().addAll(imageCol, nameCol, timeCol, ratingCol);
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
