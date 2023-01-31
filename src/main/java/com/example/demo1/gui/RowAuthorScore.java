package com.example.demo1.gui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class RowAuthorScore implements RowTableView {

    private final SimpleStringProperty authorNameTable;
    private final SimpleDoubleProperty scoreTable;

    public RowAuthorScore(String authorName, Double score) {
        authorNameTable = new SimpleStringProperty(authorName);
        scoreTable = new SimpleDoubleProperty(score);
        //imageTable = new ImageTable(image).getImage();
    }

    public String getAuthorName() { return authorNameTable.get(); }
    public Double getScore() { return scoreTable.get(); }
    //public ImageView getImage() { return imageTable; }
}
