package com.example.demo1;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class Author {
    private Object authorName;
    private Object rating;
    private Object image;

    private SimpleStringProperty authorNameTable;
    private SimpleIntegerProperty promotionTable;

    private ImageView imageTable;

    public Author(String authorName, Integer promotion, ImageView image) {
        authorNameTable = new SimpleStringProperty(authorName);
        promotionTable = new SimpleIntegerProperty(promotion);
        imageTable = new ClassTableAuthor.CustomImageAuthor(image).getImage();
    }

    public String getAuthorName() { return authorNameTable.get(); }
    public Integer getPromotion() { return promotionTable.get(); }
    public ImageView getImage() { return imageTable; }
}
