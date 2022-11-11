package com.example.mealer_project.data.handlers;

import com.example.mealer_project.data.entity_models.MealEntityModel;
import com.example.mealer_project.data.models.Meal;

import java.util.Map;

public class MealHandler {

    public void successAddingMeal(Meal meal){}

    public void errorAddingMeal(String message){}

    public void successAddingMealToOfferedList(String mealId){}

    public void successAddingMealToSearchableList(Map meal){}

    public void errorRemovingMeal(String message){}

    public void successRemovingMeal(String mealId){}

    public void successUpdatingMealInfo(Meal meal){}

    public void errorUpdatingMealInfo(String message){}

    public void addMeal(MealEntityModel mealEntityModel){}


}
