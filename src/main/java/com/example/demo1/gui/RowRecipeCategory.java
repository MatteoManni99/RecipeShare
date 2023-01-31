package com.example.demo1.gui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class RowRecipeCategory implements RowTableView {

    private final SimpleStringProperty recipeNameTable;
    private final SimpleStringProperty categoryTable;
    private final SimpleDoubleProperty ratingTable;
    private final ImageView imageLinkTable;


    public RowRecipeCategory(String name, String category, Double rating, ImageView image) {
        recipeNameTable = new SimpleStringProperty(name);
        categoryTable = new SimpleStringProperty(category);
        ratingTable = new SimpleDoubleProperty(rating);
        imageLinkTable = new ImageTableView(image).getImage();
    }

    public String getName() { return recipeNameTable.get(); }
    public String getRecipecategory() { return categoryTable.get(); }
    public Double getRating() { return ratingTable.get(); }
    public ImageView getImage() { return imageLinkTable; }
}
