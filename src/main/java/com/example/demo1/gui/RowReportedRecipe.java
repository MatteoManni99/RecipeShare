package com.example.demo1.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class RowReportedRecipe {
    private final SimpleStringProperty recipeNameTable;
    private final SimpleStringProperty authorNameTable;
    private final SimpleStringProperty reporterNameTable;
    private final ImageView imageLinkTable;

    public RowReportedRecipe(String name, String authorName, String reporterName, ImageView image) {
        recipeNameTable = new SimpleStringProperty(name);
        authorNameTable = new SimpleStringProperty(authorName);
        reporterNameTable = new SimpleStringProperty(reporterName);
        imageLinkTable = new TableViewRecipe.CustomImage(image).getImage();
    }

    public String getName() { return recipeNameTable.get(); }
    public String getAuthorName() { return authorNameTable.get(); }
    public String getReporterName() { return reporterNameTable.get(); }
    public ImageView getImage() { return imageLinkTable; }

}
