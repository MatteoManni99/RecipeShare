package com.example.demo1;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class RecipeTableView {
    private Object recipeName;
    private Object authorName;
    private Object imageLink;
    private final SimpleStringProperty recipeNameTable;
    private final SimpleStringProperty authorNameTable;
    private final ImageView imageLinkTable;


    public RecipeTableView(String name, String authorName, ImageView image) {
        recipeNameTable = new SimpleStringProperty(name);
        authorNameTable = new SimpleStringProperty(authorName);
        imageLinkTable = new ClassForTableView.CustomImage(image).getImage();
    }

    public String getName() { return recipeNameTable.get(); }
    public String getAuthorName() { return authorNameTable.get(); }
    public ImageView getImage() { return imageLinkTable; }

}
