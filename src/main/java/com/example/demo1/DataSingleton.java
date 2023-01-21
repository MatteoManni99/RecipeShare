package com.example.demo1;

import javafx.scene.image.ImageView;

public class DataSingleton {
    private static final DataSingleton instance = new DataSingleton();

    private String recipeName;
    private Integer pageNumber;
    private String authorName;
    private String password;
    private String otherAuthorName;
    private ImageView avatar;
    private DataSingleton(){}

    public static DataSingleton getInstance(){
        return instance;
    }

    public Integer getPageNumber(){return pageNumber; }
    public String getRecipeName(){return recipeName; }
    public String getPassword() {
        return password;
    }
    public String getAuthorName() {return authorName; }
    public String getOtherAuthorName() {return otherAuthorName;}

    public void setPassword(String password) {
        this.password = password;
    }
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
    public void setOtherAuthorName(String otherAuthorName) {this.otherAuthorName = otherAuthorName;}

    public void setAvatar(int index) {this.avatar = new ImageView("C:\\Users\\HP\\IdeaProjects\\RecipeShareGit\\src\\main\\resources\\avatarImages\\avatar" + index + ".png");}
    public ImageView getAvatar() {return this.avatar;}
}
