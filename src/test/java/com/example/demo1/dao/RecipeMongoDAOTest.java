package com.example.demo1.dao;

import com.example.demo1.dao.mongo.RecipeMongoDAO;
import com.example.demo1.model.Recipe;
import com.example.demo1.model.RecipeReducted;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class RecipeMongoDAOTest {

    static Recipe recipe = new Recipe("Test", "1", null, null, null , null,
            null, null, null, null, null, null,
            null, null);

    static List<RecipeReducted> listMyRecipes = new ArrayList<>();

    @Test
    void addRecipeTest(){
        RecipeMongoDAO.addRecipe(recipe);
        assertFalse(RecipeMongoDAO.checkIfNameIsAvailable("Test"));
        RecipeMongoDAO.deleteRecipe(recipe);
    }

    @Test
    void getRecipeByNameTest(){
        RecipeMongoDAO.addRecipe(recipe);
        assertEquals(recipe.getName(), RecipeMongoDAO.getRecipeByName("Test").getName());
        RecipeMongoDAO.deleteRecipe(recipe);
    }

    @Test
    void getRecipeFromName(){

    }

    @Test
    void checkIfNameIsAvailableTest(){
        RecipeMongoDAO.addRecipe(recipe);
        assertFalse(RecipeMongoDAO.checkIfNameIsAvailable("Test"));
        assertFalse(RecipeMongoDAO.checkIfNameIsAvailable("Low-Fat Berry Blue Frozen Dessert"));
        assertTrue(RecipeMongoDAO.checkIfNameIsAvailable("Low-Fat"));
        assertTrue(RecipeMongoDAO.checkIfNameIsAvailable("ddd"));
        RecipeMongoDAO.deleteRecipe(recipe);
    }

    @Test
    void getRecipeFromAuthorTest(){
        RecipeMongoDAO.addRecipe(recipe);
        listMyRecipes.add(new RecipeReducted("Test", "1", null));
        assertEquals(listMyRecipes.get(0).getName(),
                RecipeMongoDAO.getRecipeFromAuthor("1", 0, 1).get(0).getName());
        RecipeMongoDAO.deleteRecipe(recipe);
    }

    @Test
    void deleteRecipeTest(){
        RecipeMongoDAO.addRecipe(recipe);
        RecipeMongoDAO.deleteRecipe(recipe);
        assertTrue(RecipeMongoDAO.checkIfNameIsAvailable("Test"));
    }





}