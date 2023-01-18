package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AddRecipeController {


    public TextField name;
    public TextField category;
    public TextField servings;
    public TextField time;
    public TextField calories;
    public TextField description;
    public TextField keyword;
    public ListView keywordList;
    public TextField ingredient;
    public ListView ingredientList;
    public TextField image;
    public ListView imageList;
    public TextField instructions;
    public ListView instructionsList;

    public void onAddRecipeClick(ActionEvent actionEvent) {
        System.out.println(name.getText());
        System.out.println(category.getText());
    }

    public void onAddKeywordClick(ActionEvent actionEvent) {
    }

    public void onRemoveKeywordClick(ActionEvent actionEvent) {
    }

    public void onAddIngredientClick(ActionEvent actionEvent) {
    }

    public void onRemoveIngredientClick(ActionEvent actionEvent) {
    }

    public void onAddImageClick(ActionEvent actionEvent) {
    }

    public void onRemoveImageClick(ActionEvent actionEvent) {
    }

    public void onRemoveInstructionClick(ActionEvent actionEvent) {
    }
}
