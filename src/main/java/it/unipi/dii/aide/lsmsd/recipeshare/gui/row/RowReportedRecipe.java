package it.unipi.dii.aide.lsmsd.recipeshare.gui.row;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class RowReportedRecipe implements RowTableView {
    private final SimpleStringProperty recipeNameTable;
    private final SimpleStringProperty authorNameTable;
    private final SimpleStringProperty reporterNameTable;
    private final SimpleStringProperty dateReportingTable;
    private final ImageView imageLinkTable;

    public RowReportedRecipe(String name, String authorName, String reporterName, String dateReporting, ImageView image) {
        recipeNameTable = new SimpleStringProperty(name);
        authorNameTable = new SimpleStringProperty(authorName);
        reporterNameTable = new SimpleStringProperty(reporterName);
        dateReportingTable = new SimpleStringProperty(dateReporting);;
        imageLinkTable = new RowImage(image).getImage();
    }

    public String getName() { return recipeNameTable.get(); }
    public String getAuthorName() { return authorNameTable.get(); }
    public String getReporterName() { return reporterNameTable.get(); }
    public String getDateReporting() { return dateReportingTable.get(); }
    public ImageView getImage() { return imageLinkTable; }

}
