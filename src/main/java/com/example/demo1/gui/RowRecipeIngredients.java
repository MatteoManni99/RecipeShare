package com.example.demo1.gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RowRecipeIngredients implements RowRecipeQuery {

    private final SimpleStringProperty ingredientTable;
    private final SimpleIntegerProperty countTable;


    public RowRecipeIngredients(String name, Integer count) {
        ingredientTable = new SimpleStringProperty(name);
        countTable = new SimpleIntegerProperty(count);
    }

    public String getIngredient() { return ingredientTable.get(); }
    public Integer getCount() { return countTable.get(); }

}
