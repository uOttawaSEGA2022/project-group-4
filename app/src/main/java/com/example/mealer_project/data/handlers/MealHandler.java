package com.example.mealer_project.data.handlers;

import com.example.mealer_project.data.entity_models.MealEntityModel;
import com.example.mealer_project.data.models.Meal;
import com.example.mealer_project.ui.core.UIScreen;

import java.util.Map;

public class MealHandler {

    /* ------                   Add Meal                         ------- */
    public void addMeal(MealEntityModel meal, UIScreen ActivityScreen){}

    public void successAddingMeal(Meal meal){}

    public void errorAddingMeal(String message){}

    /* ------               Add Meal To Offered Lis             ------- */

    public void addMealToOfferedList(Meal meal, UIScreen ActivityScreen){}

    public void successAddingMealToOfferedList(String mealId){}

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
