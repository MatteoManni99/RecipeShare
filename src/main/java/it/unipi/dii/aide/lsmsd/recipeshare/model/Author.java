package it.unipi.dii.aide.lsmsd.recipeshare.model;

import java.util.Objects;

public class Author extends User{
    private Integer promotion;
    private Integer image;
    private Double score;

    public Author(String name, String password, Integer image, Integer promotion){
        super(name,password);
        this.image = image;
        this.promotion = promotion;
    }
    public Author(String name,Double score, Integer image){
        super(name);
        this.score = score;
        this.image = image;
    }
    public Author(String name,Integer image){
        super(name);
        this.image = image;
    }
    @Override
    public String toString() {
        return "name: " + getName() + "\t\t" + "image: " + image + "\n";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(image, author.image) &&
                Objects.equals(getName(), author.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), image);
    }

    public void setPromotion(Integer promotion) {
        this.promotion = promotion;
    }
    public void setImage(Integer image) {
        this.image = image;
    }
    public Double getScore() {
        return score;
    }
    public void setScore(Double score) {
        this.score = score;
    }
    public Integer getPromotion() {
        return promotion;
    }
    public Integer getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }
}
