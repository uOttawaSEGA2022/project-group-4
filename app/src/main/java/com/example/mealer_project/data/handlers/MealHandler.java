package com.example.mealer_project.data.handlers;

import android.util.Log;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.entity_models.MealEntityModel;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Meal;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.example.mealer_project.ui.screens.AdminScreen;
import com.example.mealer_project.ui.screens.NewMealScreen;
import com.example.mealer_project.utils.Preconditions;

import java.util.Map;

public class MealHandler {

    private StatefulView uiScreen;

    /* ------                   Add Meal                         ------- */
    public void addMeal(MealEntityModel meal, StatefulView uiScreen){

        // guard-clause
        if (Preconditions.isNotNull(meal) && Preconditions.isNotNull(uiScreen)) {

            // set the ui screen, so it can be interacted with later on
            this.uiScreen = uiScreen;

            // try to build Meal instance first
            try {
                // below code might cause exception if validation fails or instance can't be created
                Meal newMeal = new Meal(meal);
                // if meal creation was success, add the meal to database
                App.getPrimaryDatabase().MEALS.addMeal(newMeal);
            } catch (Exception e) {
                errorAddingMeal("Failed to create Meal instance: " + e.getMessage());
            }

        } else {
            errorAddingMeal("Invalid meal or uiScreen instance provided to addMeal");
        }
    }

    public void successAddingMeal(Meal meal){
        try {
            if (Preconditions.isNotNull(meal) && Preconditions.isNotNull(uiScreen)) {
                // add meal locally
                Chef chef = (Chef) App.getUser();
                chef.MEALS.addMeal(meal);
                // let UI know about success
                uiScreen.dbOperationSuccessHandler(NewMealScreen.dbOperations.ADD_MEAL, "Meal added successfully!");
            } else {
                errorAddingMeal("Failed to add meal locally: meal is null");
            }
        } catch (Exception e) {
            errorAddingMeal("Failed to add meal locally: " + e.getMessage());
        }
    }

    public void errorAddingMeal(String message){
        Log.e("addingMeal", message);
        uiScreen.dbOperationFailureHandler(NewMealScreen.dbOperations.ADD_MEAL, "Failed to add meal!");
    }

    /* ------               Add Meal To Offered Lis             ------- */

    public void addMealToOfferedList(String mealId, StatefulView uiScreen){
        // guard-clause
        if (Preconditions.isNotNull(mealId) && Preconditions.isNotNull(uiScreen)) {

            // set the ui screen, so it can be interacted with later on
            this.uiScreen = uiScreen;

            // update meal on remote database first
            App.getPrimaryDatabase().MEALS.addMealToOfferedList(mealId);
        } else {
            errorAddingMeal("Invalid meal ID or uiScreen instance provided to addMealToOfferedList");
        }
    }

    public void successAddingMealToOfferedList(String mealId){
        try {
            if (Preconditions.isNotNull(mealId) && Preconditions.isNotNull(uiScreen)) {
                // add meal locally
                ((Chef) App.getUser()).MEALS.addMealToOfferedList(mealId);
                // let UI know about success
                uiScreen.dbOperationSuccessHandler(NewMealScreen.dbOperations.ADD_MEAL, "Meal set as offered!");
            } else {
                errorAddingMealToOfferedList("meal ID or uiScreen is null");
            }
        } catch (Exception e) {
            errorAddingMealToOfferedList("Failed to set meal as offered: " + e.getMessage());
        }
    }

    public void errorAddingMealToOfferedList(String message){
        Log.e("addingMealToOffered", message);
        uiScreen.dbOperationFailureHandler(NewMealScreen.dbOperations.ADD_MEAL, "Failed to set meal as offered!");
    }

    public void successAddingMealToSearchableList(Map meal){}

    /* ------                   Remove Meal                    ------- */

    public void removeMeal(String mealId, UIScreen ActivityScreen){}

    public void removeMealFromOfferedList(String mealId, UIScreen ActivityScreen){}

    public void successRemovingMeal(String mealId){}

    public void errorRemovingMeal(String message){}

    /* ------                   Get Chef's Menu               ------- */

    public void getMenu(UIScreen ActivityScreen){}

    /* ------                   Update Meal(s)                ------- */

    public void updateMealInfo(Meal meal, UIScreen ActivityScreen){}

    public void updateOfferedMeals(Map<String, Boolean> meals, UIScreen ActivityScreen){}


    public void successUpdatingMealInfo(Meal meal){}

    public void errorUpdatingMealInfo(String message){}

}
