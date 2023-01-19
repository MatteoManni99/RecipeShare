package com.example.demo1;

public class DataSingleton {
    private static final DataSingleton instance = new DataSingleton();

    private Integer recipeId;
    private Integer pageNumber;
    private String authorName;
    private String password;
    private DataSingleton(){}

    public static DataSingleton getInstance(){
        return instance;
    }

    public Integer getPageNumber(){return pageNumber; }
    public Integer getRecipeId(){return pageNumber; }
    public String getPassword() {
        return password;
    }
    public String getAuthorName() {return authorName; }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
