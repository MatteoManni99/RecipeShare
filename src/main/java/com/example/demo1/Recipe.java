package com.example.demo1;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class Recipe {
    private Object recipeId;
    private Object recipeName;
    private Object authorId;
    private Object authorName;
    private Object imageLink;
    private Object recipeImage;

    private SimpleIntegerProperty recipeIdTable;
    private SimpleStringProperty recipeNameTable;
    private SimpleIntegerProperty authorIdTable;
    private SimpleStringProperty authorNameTable;
    private ImageView imageLinkTable;

    /*public Recipe(Object recipeId, Object recipeName, Object authorId, Object authorName, Object image) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.authorId = authorId;
        this.authorName = authorName;
        this.imageLink = image;
        recipeImage = new ImageView((String) image);
    }*/

    public Recipe(int recipeId, String name, int authorId, String authorName, ImageView image) {
        recipeIdTable = new SimpleIntegerProperty(recipeId);
        recipeNameTable = new SimpleStringProperty(name);
        authorIdTable = new SimpleIntegerProperty(authorId);
        authorNameTable = new SimpleStringProperty(authorName);
        imageLinkTable = new ClassForTableView.CustomImage(image).getImage();
    }

    public int getRecipeId() { return recipeIdTable.get(); }
    public String getName() { return recipeNameTable.get(); }
    public int getAuthorId() { return authorIdTable.get(); }
    public String getAuthorName() { return authorNameTable.get(); }
    public ImageView getImage() { return imageLinkTable; }

}
