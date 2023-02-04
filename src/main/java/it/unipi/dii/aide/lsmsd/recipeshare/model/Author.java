package it.unipi.dii.aide.lsmsd.recipeshare.model;

import java.util.Objects;

public class Author {
    private String name;
    private String password;
    private Integer promotion;
    private Integer image;
    private Double score;

    public Author(String name, String password, Integer image, Integer promotion){
        this.name = name;
        this.password = password;
        this.image = image;
        this.promotion = promotion;
    }
    public Author(String name,Double score, Integer image){
        this.name = name;
        this.score = score;
        this.image = image;
    }
    public Author(String name,Integer image){
        this.name = name;
        this.image = image;
    }
    @Override
    public String toString() {
        return "name: " + name + "\t\t" + "image: " + image + "\n";
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(image, author.image) &&
                Objects.equals(name, author.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, image);
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
