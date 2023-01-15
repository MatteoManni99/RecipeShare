package com.example.demo1;

public class DataSingleton {
    private static final DataSingleton instance = new DataSingleton();

    private Integer recipeId;

    private DataSingleton(){}

    public static DataSingleton getInstance(){
        return instance;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }
}
