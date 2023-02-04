package it.unipi.dii.aide.lsmsd.recipeshare.gui.tableview;

import it.unipi.dii.aide.lsmsd.recipeshare.gui.row.RowTableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

public class TableViewReportedRecipe extends TableViewAbstract {
    private final TableColumn<RowTableView, String> nameCol;
    private final TableColumn<RowTableView, String> authorNameCol;
    private final TableColumn<RowTableView, String> reporterNameCol;
    private final TableColumn<RowTableView, String> dateReportingCol;
    private final TableColumn<RowTableView, ImageView> imageCol;

    public TableViewReportedRecipe() {
        table = new TableView<>();

        nameCol = new TableColumn<>("Name");
        authorNameCol = new TableColumn<>("Author");
        reporterNameCol = new TableColumn<>("ReporterName");
        dateReportingCol = new TableColumn<>("DateReporting");
        imageCol = new TableColumn<>("Image");

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorNameCol.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        reporterNameCol.setCellValueFactory(new PropertyValueFactory<>("reporterName"));
        dateReportingCol.setCellValueFactory(new PropertyValueFactory<>("dateReporting"));
        imageCol.setCellValueFactory(new PropertyValueFactory<>("image"));

        table.setPrefHeight(400);
        table.setPrefWidth(500);
        table.setLayoutX(40);
        table.setLayoutY(200);
    }

    @Override
    public void setTable() {
        setColumns(imageCol, nameCol, authorNameCol, reporterNameCol, dateReportingCol);
    }

}
