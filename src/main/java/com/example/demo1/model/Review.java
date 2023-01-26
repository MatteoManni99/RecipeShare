package com.example.demo1.model;

public class Review {
    private String authorName;
    private String rating;
    private String review;

    Review( String authorName, String rating, String review){
        this.authorName = authorName;
        this.rating = rating;
        this.review = review;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
