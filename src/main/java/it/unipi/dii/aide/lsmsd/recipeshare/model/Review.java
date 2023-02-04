package it.unipi.dii.aide.lsmsd.recipeshare.model;

public class Review {
    private String authorName;
    private Integer rating;
    private String review;

    public Review( String authorName, Integer rating, String review){
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

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
