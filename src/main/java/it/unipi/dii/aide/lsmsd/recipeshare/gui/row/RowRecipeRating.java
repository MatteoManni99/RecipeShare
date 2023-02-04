package it.unipi.dii.aide.lsmsd.recipeshare.gui.row;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class RowRecipeRating implements RowTableView {
    private final SimpleStringProperty recipeNameTable;
    private final SimpleDoubleProperty ratingTable;
    private final ImageView imageLinkTable;


    public RowRecipeRating(String name, Double rating, ImageView image) {
        recipeNameTable = new SimpleStringProperty(name);
        ratingTable = new SimpleDoubleProperty(rating);
        imageLinkTable = new RowImage(image).getImage();
    }

    public String getName() { return recipeNameTable.get(); }
    public Double getRating() { return ratingTable.get(); }
    public ImageView getImage() { return imageLinkTable; }

}
