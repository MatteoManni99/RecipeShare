package com.example.demo1.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class RowRecipe {
    private Object recipeName;
    private Object authorName;
    private Object imageLink;
    private final SimpleStringProperty recipeNameTable;
    private final SimpleStringProperty authorNameTable;
    private final ImageView imageLinkTable;


    public RowRecipe(String name, String authorName, ImageView image) {
        recipeNameTable = new SimpleStringProperty(name);
        authorNameTable = new SimpleStringProperty(authorName);
        imageLinkTable = new TableViewRecipe.CustomImage(image).getImage();
    }

    public String getName() { return recipeNameTable.get(); }
    public String getAuthorName() { return authorNameTable.get(); }
    public ImageView getImage() { return imageLinkTable; }

}