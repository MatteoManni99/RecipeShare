package com.example.demo1;

public class DataSingleton {
    private static final DataSingleton instance = new DataSingleton();

    private Integer recipeId;
    private Integer pageNumber;
    private DataSingleton(){}

    public static DataSingleton getInstance(){
        return instance;
    }

    public Integer getPageNumber(){return pageNumber; }
    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
