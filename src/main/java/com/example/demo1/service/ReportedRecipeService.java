package com.example.demo1.service;

import com.example.demo1.Configuration;
import com.example.demo1.dao.mongo.ReportedRecipeMongoDAO;
import com.example.demo1.model.ReportedRecipe;
import com.example.demo1.persistence.MongoDBDriver;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReportedRecipeService {
    public static boolean addReportedRecipe(ReportedRecipe reportedRecipe) {
        return ReportedRecipeMongoDAO.addReportedRecipe(reportedRecipe);
    }

    public static List<ReportedRecipe> getListReportedRecipes() {
        return ReportedRecipeMongoDAO.getListReportedRecipes();
    }

    public static Map<String, Double> onHighestRatioQueryClick(){
        return ReportedRecipeMongoDAO.onHighestRatioQueryClick();
    }

}
