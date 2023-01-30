package com.example.demo1.gui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class RowAuthorScore {
    private Object authorName;
    private Object score;
    //private Object image;

    private SimpleStringProperty authorNameTable;
    private SimpleDoubleProperty scoreTable;
    //private ImageView imageTable;

    public RowAuthorScore(String authorName, Double score) {
        authorNameTable = new SimpleStringProperty(authorName);
        scoreTable = new SimpleDoubleProperty(score);
        //imageTable = new TableViewAuthor.CustomImageAuthor(image).getImage();
    }

    public String getAuthorName() { return authorNameTable.get(); }
    public Double getScore() { return scoreTable.get(); }
    //public ImageView getImage() { return imageTable; }
}
