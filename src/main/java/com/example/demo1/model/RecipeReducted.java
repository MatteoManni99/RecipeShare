package com.example.demo1.model;

public class RecipeReducted {

    private String name;

    private String authorName;

    private String image;

    public RecipeReducted(String name, String authorName, String image){
        this.name = name;
        this.authorName = authorName;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
