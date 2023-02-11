package it.unipi.dii.aide.lsmsd.recipeshare.service;

import com.mongodb.MongoException;
import it.unipi.dii.aide.lsmsd.recipeshare.dao.mongo.ReportedRecipeMongoDAO;
import it.unipi.dii.aide.lsmsd.recipeshare.model.Author;
import it.unipi.dii.aide.lsmsd.recipeshare.model.Recipe;
import it.unipi.dii.aide.lsmsd.recipeshare.model.ReportedRecipe;

import java.util.List;

public class ReportedRecipeService {
    public static boolean addReportedRecipe(ReportedRecipe reportedRecipe) {
        try {
            ReportedRecipeMongoDAO.addReportedRecipe(reportedRecipe);
            return true;
        }catch (MongoException e){return false;}
    }
    public static boolean checkIfRecipeAlreadyReported(ReportedRecipe reportedRecipe) throws MongoException{
        return ReportedRecipeMongoDAO.checkIfRecipeAlreadyReported(reportedRecipe);
    }

    public static boolean approveReportedRecipe(Recipe recipe) {
        try{
            ReportedRecipeMongoDAO.removeReportedRecipe(recipe.getName());
            return true;
        }catch (Exception e){return false;}
    }
    public static boolean notApproveReportedRecipe(Recipe recipe) {
        if(RecipeService.deleteRecipe(recipe)){
            try{
                ReportedRecipeMongoDAO.removeReportedRecipe(recipe.getName());
                return true;
            }catch (MongoException e){ return false; }
        }else return false;
    }

    public static List<ReportedRecipe> getListReportedRecipes(String nameToSearch, Integer elementToSkip, Integer elementsToLimit) {
        return ReportedRecipeMongoDAO.getListReportedRecipes(nameToSearch, elementToSkip, elementsToLimit);
    }

    public static List<Author> onLowestAuthorScoreQueryClick(){
        return ReportedRecipeMongoDAO.onLowestAuthorScoreQueryClick();
    }

}
