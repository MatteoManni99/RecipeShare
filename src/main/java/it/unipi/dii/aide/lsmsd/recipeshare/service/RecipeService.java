package it.unipi.dii.aide.lsmsd.recipeshare.service;

import it.unipi.dii.aide.lsmsd.recipeshare.dao.mongo.RecipeMongoDAO;
import it.unipi.dii.aide.lsmsd.recipeshare.dao.neo4j.AuthorNeoDAO;
import it.unipi.dii.aide.lsmsd.recipeshare.dao.neo4j.RecipeNeoDAO;
import it.unipi.dii.aide.lsmsd.recipeshare.model.Recipe;
import it.unipi.dii.aide.lsmsd.recipeshare.model.RecipeReduced;
import com.mongodb.MongoException;
import org.neo4j.driver.exceptions.Neo4jException;

import java.util.List;
import java.util.Map;

public class RecipeService {

    public static boolean deleteRecipe(Recipe recipe) {
        try{
            RecipeMongoDAO.deleteRecipe(recipe);
        }catch (MongoException e){return false;}
        try {
            RecipeNeoDAO.deleteRecipe(recipe.getName());
            return true;
        }catch(Neo4jException e){
            RecipeMongoDAO.addRecipe(recipe);
            return false;
        }
    }

    public static Recipe getRecipeByName(String name){
        return RecipeMongoDAO.getRecipeByName(name);
    }

    public static boolean addReview(String recipeName, String reviewer, Integer rating, String reviewText){
        try{ RecipeMongoDAO.addReview(recipeName, reviewer, rating, reviewText); }
        catch(MongoException e){ return false; }
        try{
            AuthorNeoDAO.addRelationReview(reviewer, recipeName, rating);
            RecipeMongoDAO.setAggregatedRating(recipeName,RecipeMongoDAO.calculateAggregatedRating(recipeName));
            return true;
        }catch(Neo4jException e){
            RecipeMongoDAO.deleteReview(recipeName, reviewer);//rollback su mongo
            return false;
        }
    }

    public static boolean addRecipe(Recipe recipe){
        try{RecipeMongoDAO.addRecipe(recipe);}
        catch (MongoException e){ return false;}
        try{RecipeNeoDAO.addRecipe(new RecipeReduced(recipe.getName(),recipe.getAuthorName(),recipe.getImages().get(0)));}
        catch (Neo4jException e){
            RecipeMongoDAO.deleteRecipe(recipe); //rollback su mongo
            return false;
        }
        try{
            RecipeNeoDAO.addRelationWrite(recipe.getAuthorName(), recipe.getName());
            return true;
        }catch (Neo4jException e){
            RecipeNeoDAO.deleteRecipe(recipe.getName()); //rollback su neo
            RecipeMongoDAO.deleteRecipe(recipe); //rollback su mongo
            return false;
        }
    }

    public static boolean checkIfNameIsAvailable(String name){
        return RecipeMongoDAO.checkIfNameIsAvailable(name);
    }

    public static List<RecipeReduced> getRecipeFromAuthor(String authorName, Integer elementToSkip, Integer elementsToLimit){
        return RecipeMongoDAO.getRecipeFromAuthor(authorName, elementToSkip, elementsToLimit);
    }

    public static List<RecipeReduced> getRecipeFromName(String authorName, Integer elementToSkip, Integer elementsToLimit){
        return RecipeMongoDAO.getRecipeFromName(authorName, elementToSkip, elementsToLimit);
    }

    public static List<Recipe> findTopRecipesForRangesOfPreparationTime(Integer lowerLimit, Integer upperLimit,
                                                                        Integer minNumberReviews, Integer limitRecipes){
        return RecipeMongoDAO.findTopRecipesForRangesOfPreparationTime(lowerLimit, upperLimit, minNumberReviews, limitRecipes);
    }

    public static Map<String,Integer> findMostUsedIngredients(Integer limitIngredients, Integer minNumberReviews){
        return RecipeMongoDAO.findMostUsedIngredients(limitIngredients, minNumberReviews);
    }

    public static List<Recipe> findRecipesWithHighestRating(Integer skipRecipes, Integer limitRecipes, Integer minNumberReviews){
        return RecipeMongoDAO.findRecipesWithHighestRating(skipRecipes,limitRecipes, minNumberReviews);
    }

    public static List<Recipe> findTopRecipesForEachCategory(Integer minNumberReviews){
        return RecipeMongoDAO.findTopRecipesForEachCategory(minNumberReviews);
    }

}
