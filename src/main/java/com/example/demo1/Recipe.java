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

    public Recipe(Object recipeId, Object recipeName, Object authorId, Object authorName, Object image) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.authorId = authorId;
        this.authorName = authorName;
        this.imageLink = image;
        recipeImage = new ImageView((String) image);
    }

    public void RecipeSimpleProperty(SimpleIntegerProperty recipeId, SimpleStringProperty recipeName, SimpleIntegerProperty authorId, SimpleStringProperty authorName, SimpleStringProperty image) {
        recipeIdTable = recipeId;
        recipeNameTable = recipeName;
        authorIdTable = authorId;
        authorNameTable = authorName;
        imageLinkTable = new ImageView(String.valueOf(image));
    }

    public int getRecipeId() { return recipeIdTable.get(); }
    public String getRecipeNameTable() { return recipeNameTable.get(); }
    public int getAuthorIdTable() { return authorIdTable.get(); }
    public String getAuthorNameTable() { return authorNameTable.get(); }
    //public String getImageTable() { return imageLinkTable.get(); }

}
