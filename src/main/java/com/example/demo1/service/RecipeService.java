package com.example.demo1.service;

import com.example.demo1.dao.mongo.RecipeMongoDAO;
import com.example.demo1.model.Recipe;
import com.example.demo1.model.RecipeReducted;
import com.mongodb.MongoException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeService {

    public static boolean deleteRecipe(Recipe recipe) throws MongoException {
        try{
            RecipeMongoDAO.deleteRecipe(recipe);
            return true;
        }catch (Exception e){
            //TODO rollback
            return false;
        }
    }

    public static Recipe getRecipeByName(String name){
        return RecipeMongoDAO.getRecipeByName(name);
    }

    public static void addReview(String recipeName, String reviewer, Integer rating, String review){
        RecipeMongoDAO.addReview(recipeName, reviewer, rating, review);
        RecipeMongoDAO.setAggregatedRating(recipeName,RecipeMongoDAO.calculateAggregatedRating(recipeName));
    }

    public static void addRecipe(Recipe recipe){
        RecipeMongoDAO.addRecipe(recipe);
    }

    public static boolean checkIfNameIsAvailable(String name){
        return RecipeMongoDAO.checkIfNameIsAvailable(name);
    }

    public static List<RecipeReducted> getRecipeFromAuthor(String authorName, Integer elementToSkip, Integer elementsToLimit){
        return RecipeMongoDAO.getRecipeFromAuthor(authorName, elementToSkip, elementsToLimit);
    }

    public static List<RecipeReducted> getRecipeFromName(String authorName, Integer elementToSkip, Integer elementsToLimit){
        return RecipeMongoDAO.getRecipeFromName(authorName, elementToSkip, elementsToLimit);
    }

    public static List<Recipe> findTopRecipesForRangesOfPreparationTime(Integer lowerLimit, Integer upperLimit,
                                                                        Integer minNumberReviews, Integer limitRecipes){
        return RecipeMongoDAO.findTopRecipesForRangesOfPreparationTime(lowerLimit, upperLimit, minNumberReviews, limitRecipes);
    }

    public static Map<String,Integer> findMostUsedIngredients(Integer limitIngredients, Integer minNumberReviews){
        return RecipeMongoDAO.findMostUsedIngredients(limitIngredients, minNumberReviews);
    }

    public static List<Recipe> findRecipesWithHighestRating(Integer limitRecipes, Integer minNumberReviews){
        return RecipeMongoDAO.findRecipesWithHighestRating(limitRecipes, minNumberReviews);
    }

    public static List<Recipe> findTopRecipesForEachCategory(Integer minNumberReviews){
        return RecipeMongoDAO.findTopRecipesForEachCategory(minNumberReviews);
    }

}
