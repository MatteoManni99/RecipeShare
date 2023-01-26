package com.example.demo1.model;

public class Author {
    private String name;
    private String password;
    private int promotion;
    private int image;

    public Author(String name, String password, int image, int promotion){
        this.name = name;
        this.password = password;
        this.image = image;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPromotion() {
        return promotion;
    }

    public void setPromotion(int promotion) {
        this.promotion = promotion;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
