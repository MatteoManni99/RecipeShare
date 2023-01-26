package com.example.demo1.model;

import java.util.ArrayList;

public class Recipe {
    private String name;
    private String authorName;
    private Integer totalTime;
    private String datePublished;
    private String description;
    private ArrayList<String> images;
    private String recipeCategory;
    private ArrayList<String> keywords;
    private ArrayList<String> recipeIngredientParts;
    private Double aggregatedRating;
    private Double calories;
    private Double recipeServings;
    private ArrayList<String> recipeInstructions;
    private ArrayList<Review> reviews;

    Recipe(String name, String authorName,Integer totalTime, String datePublished, String description, ArrayList<String> images,
           String recipeCategory, ArrayList<String> keywords, ArrayList<String> recipeIngredientParts, Double aggregatedRating,
           Double calories, Double recipeServings, ArrayList<String> recipeInstructions, ArrayList<Review> reviews){
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

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getRecipeCategory() {
        return recipeCategory;
    }

    public void setRecipeCategory(String recipeCategory) {
        this.recipeCategory = recipeCategory;
    }

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public ArrayList<String> getRecipeIngredientParts() {
        return recipeIngredientParts;
    }

    public void setRecipeIngredientParts(ArrayList<String> recipeIngredientParts) {
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

    public ArrayList<String> getRecipeInstructions() {
        return recipeInstructions;
    }

    public void setRecipeInstructions(ArrayList<String> recipeInstructions) {
        this.recipeInstructions = recipeInstructions;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }
}
