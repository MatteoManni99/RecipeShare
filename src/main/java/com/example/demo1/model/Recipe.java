package com.example.demo1.model;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String name;
    private String authorName;
    private Integer totalTime;
    private String datePublished;
    private String description;
    private List<String> images;
    private String recipeCategory;
    private List<String> keywords;
    private List<String> recipeIngredientParts;
    private Double aggregatedRating;
    private Double calories;
    private Double recipeServings;
    private List<String> recipeInstructions;
    private List<Document> reviews;

    public Recipe(String name, String authorName,Integer totalTime, String datePublished, String description, List<String> images,
           String recipeCategory, List<String> keywords, List<String> recipeIngredientParts, Double aggregatedRating,
           Double calories, Double recipeServings, List<String> recipeInstructions, List<Document> reviews){
        this.name = name;
        this.authorName = authorName;
        this.totalTime = totalTime ;
        this.datePublished = datePublished;
        this.description = description;
        this.images = images;
        this.recipeCategory = recipeCategory;
        this.keywords = keywords;
        this.recipeIngredientParts = recipeIngredientParts;
        this.aggregatedRating = aggregatedRating;
        this.calories = calories;
        this.recipeServings = recipeServings;
        this.recipeInstructions = recipeInstructions;
        this.reviews = reviews;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getRecipeCategory() {
        return recipeCategory;
    }

    public void setRecipeCategory(String recipeCategory) {
        this.recipeCategory = recipeCategory;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getRecipeIngredientParts() {
        return recipeIngredientParts;
    }

    public void setRecipeIngredientParts(List<String> recipeIngredientParts) {
        this.recipeIngredientParts = recipeIngredientParts;
    }

    public Double getAggregatedRating() {
        return aggregatedRating;
    }

    public void setAggregatedRating(Double aggregatedRating) {
        this.aggregatedRating = aggregatedRating;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getRecipeServings() {
        return recipeServings;
    }

    public void setRecipeServings(Double recipeServings) {
        this.recipeServings = recipeServings;
    }

    public List<String> getRecipeInstructions() {
        return recipeInstructions;
    }

    public void setRecipeInstructions(List<String> recipeInstructions) {
        this.recipeInstructions = recipeInstructions;
    }

    public List<Document> getReviews() {
        return reviews;
    }

    public void setReviews(List<Document> reviews) {
        this.reviews = reviews;
    }
}
