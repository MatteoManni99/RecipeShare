package com.example.demo1.gui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class RowRecipeRating implements RowRecipeQuery{
    private final SimpleStringProperty recipeNameTable;
    private final SimpleDoubleProperty ratingTable;
    private final ImageView imageLinkTable;


    public RowRecipeRating(String name, Double rating, ImageView image) {
        recipeNameTable = new SimpleStringProperty(name);
        ratingTable = new SimpleDoubleProperty(rating);
        imageLinkTable = new TableViewRecipe.CustomImage(image).getImage();
    }

    public String getName() { return recipeNameTable.get(); }
    public Double getRating() { return ratingTable.get(); }
    public ImageView getImage() { return imageLinkTable; }

}
