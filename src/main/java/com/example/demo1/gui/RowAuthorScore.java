package com.example.demo1.gui;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.ImageView;

public class RowAuthorScore implements RowTableView {

    private final SimpleStringProperty authorNameTable;
    private final SimpleDoubleProperty scoreTable;
    private final ImageView imageTable;

    public RowAuthorScore(String authorName, Double score, ImageView image) {
        authorNameTable = new SimpleStringProperty(authorName);
        scoreTable = new SimpleDoubleProperty(score);
        imageTable = new ImageTableView(image).getImage();
    }

    public String getAuthorName() { return authorNameTable.get(); }
    public Double getScore() { return scoreTable.get(); }
    public ImageView getImage() { return imageTable; }
}
