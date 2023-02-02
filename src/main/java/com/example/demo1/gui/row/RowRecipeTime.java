package com.example.demo1.gui.row;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class RowRecipeTime implements RowTableView {

    private final SimpleStringProperty recipeNameTable;
    private final SimpleIntegerProperty timeTable;
    private final SimpleDoubleProperty ratingTable;
    private final ImageView imageLinkTable;


    public RowRecipeTime(String name, Integer time, Double rating, ImageView image) {
        recipeNameTable = new SimpleStringProperty(name);
        timeTable = new SimpleIntegerProperty(time);
        ratingTable = new SimpleDoubleProperty(rating);
        imageLinkTable = new RowImage(image).getImage();
    }

    public String getName() { return recipeNameTable.get(); }
    public Integer getTotaltime() { return timeTable.get(); }
    public Double getRating() { return ratingTable.get(); }
    public ImageView getImage() { return imageLinkTable; }
}
