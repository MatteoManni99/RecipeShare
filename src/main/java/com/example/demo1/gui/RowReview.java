package com.example.demo1.gui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RowReview {
    private Object authorName;
    private Object rating;
    private Object review;

    private final SimpleStringProperty authorNameTable;
    private final SimpleIntegerProperty ratingTable;
    private final SimpleStringProperty reviewTable;

    public RowReview(String authorName, Integer rating, String review) {
        authorNameTable = new SimpleStringProperty(authorName);
        ratingTable = new SimpleIntegerProperty(rating);
        reviewTable = new SimpleStringProperty(review);
    }

    public String getAuthorName() { return authorNameTable.get(); }

    public Integer getRating() { return ratingTable.get(); }

    public String getReview() { return reviewTable.get(); }

}
