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

    /**
     * Specify DB operations handled by Meal Handler
     */
    public enum dbOperations {
        ADD_MEAL,
        ADD_MEAL_TO_OFFERED_LIST,
        REMOVE_MEAL,
        REMOVE_MEAL_FROM_OFFERED_LIST,
        UPDATE_MEAL_INFO,
        UPDATE_OFFERED_MEALS,
        GET_MENU,
        GET_MEAL_BY_ID,
        ERROR
    };

    private StatefulView uiScreen;

    /**
     * Using the Dispatch-Action Pattern to handle actions dispatched to Meal Handler
     * @param operationType one of the specified DB operations handled by MealHandler
     * @param payload an input data for the handler's operation
     * @param uiScreen instance of the view which needs to know of the operation's success or failure
     */
    public void dispatch(dbOperations operationType, Object payload, StatefulView uiScreen) {

        // guard-clause
        if (Preconditions.isNotNull(uiScreen)) {

            // set the ui screen, so it can be interacted with later on
            this.uiScreen = uiScreen;

            try {
                switch (operationType) {

                    case ADD_MEAL:
                        if (Preconditions.isNotNull(payload) && payload instanceof MealEntityModel) {
                            addMeal((MealEntityModel) payload);
                        } else {
                            errorAddingMeal("Invalid Meal Object provided");
                        }
                        break;

                    case ADD_MEAL_TO_OFFERED_LIST:
                        // update meal on remote database first
                        if (Preconditions.isNotNull(payload) && payload instanceof String) {
                            App.getPrimaryDatabase().MEALS.addMealToOfferedList((String) payload);
                        } else {
                            errorAddingMealToOfferedList("Invalid meal ID provided");
                        }
                        break;

                    case REMOVE_MEAL:
                        // remove meal on remote database first
                        if (Preconditions.isNotNull(payload) && payload instanceof String) {
                            App.getPrimaryDatabase().MEALS.removeMeal((String) payload);
                        } else {
                            errorRemovingMeal("Invalid meal ID provided");
                        }
                        break;

                    case REMOVE_MEAL_FROM_OFFERED_LIST:
                        if (Preconditions.isNotNull(payload) && payload instanceof String) {
                            App.getPrimaryDatabase().MEALS.removeMealFromOfferedList((String) payload);
                        } else {
                            errorRemovingMealFromOfferedList("Invalid meal ID provided");
                        }
                        break;

                    case UPDATE_MEAL_INFO:
                        if (Preconditions.isNotNull(payload) && payload instanceof Meal) {
                            App.getPrimaryDatabase().MEALS.updateMealInfo((Meal) payload);
                        } else {
                            errorUpdatingMealInfo("Invalid Meal instance provided");
                        }
                        break;

                    case UPDATE_OFFERED_MEALS:
                        if (Preconditions.isNotNull(payload) && payload instanceof Map) {
                            App.getPrimaryDatabase().MEALS.updateOfferedMeals((Map<String, Boolean>) payload);
                        } else {
                            errorUpdatingOfferedMeals("Invalid object provided for map");
                        }
                        break;

                    case GET_MENU:
                        App.getPrimaryDatabase().MEALS.getMeals();
                        break;

                    case GET_MEAL_BY_ID:
                        if (Preconditions.isNotNull(payload)) {
                            // if we have been provided with both meal id and a chef id
                            if (payload instanceof String[]) {
                                String[] ids = (String[]) payload;
                                App.getPrimaryDatabase().MEALS.getMealById(ids[0], ids[1]);
                            }

                            // if we only have meal id
                            else if (payload instanceof String) {
                                App.getPrimaryDatabase().MEALS.getMealById((String) payload);
                            }

                            else {
                                errorGettingMealById("Invalid arguments provided for getting meal by id");
                            }
                        } else {
                            errorGettingMealById("No arugments provided for getMealById");
                        }
                }
            } catch (Exception e) {
                Log.e("MealHandler Dispatch", "Exception: " + e.getMessage());
                uiScreen.dbOperationFailureHandler(null, "Dispatch failed: " + e.getMessage());
            }

        } else {
            Log.e("MealHandler Dispatch", "Invalid instance provided for uiScreen");
        }
    }

    public void handleActionSuccess(dbOperations operationType, Object payload) {
        // ensure we have a valid uiScreen to inform of success
        if(Preconditions.isNotNull(uiScreen)) {

            switch (operationType) {

                case ADD_MEAL:
                    successAddingMeal((Meal) payload);
                case REMOVE_MEAL:
                    if (Preconditions.isNotNull(payload) && payload instanceof String) {
                        ((Chef) App.getUser()).MEALS.removeMeal((String) payload);
                    } else {
                        errorRemovingMeal("");
                    }

            }

        } else {
            Log.e("handleActionSuccess", "No UI Screen initialized");
        }
    }

    public void handleActionFailure(dbOperations operationType, String message) {
        // ensure we have a valid uiScreen to inform of failure
        if(Preconditions.isNotNull(uiScreen)) {

            String tag = "handleActionFailure";
            String userMessage = "Failed to process request";

            switch (operationType) {

                case ADD_MEAL:
                    tag = "addMeal";
                    userMessage= "Failed to add meal!";
                    break;

                case ADD_MEAL_TO_OFFERED_LIST:
                    tag = "addingMealToOffered";
                    userMessage = "Failed to set meal as offered!";
                    break;

                case REMOVE_MEAL:
                    tag = "errorRemovingMeal";
                    userMessage = "Failed to remove meal!";
                    break;

                case REMOVE_MEAL_FROM_OFFERED_LIST:
                    tag = "removeMealFromOffered";
                    userMessage = "Failed to remove meal from offered list!";
                    break;

                case UPDATE_MEAL_INFO:
                    tag = "updateMealInfo";
                    userMessage = "Failed to update meal info!";
                    break;

                case UPDATE_OFFERED_MEALS:
                    tag = "error";
                    userMessage = "Failed to update offered meals!";
                    break;

                case GET_MENU:
                    tag = "errorGetMenu";
                    userMessage = "Failed to get menu!";
                    break;

                case GET_MEAL_BY_ID:
                    tag = "errorGettingMealById";
                    userMessage = "Failed to get meal by id!";
                    break;
            }

            // send error
            Log.e(tag, message);
            uiScreen.dbOperationFailureHandler(operationType, userMessage);

        } else {
            Log.e("handleActionSuccess", "No UI Screen initialized");
        }
    }

    /* ------                   Add Meal                         ------- */
    private void addMeal(MealEntityModel meal){
        // try to build Meal instance first
        try {
            // below code might cause exception if validation fails or instance can't be created
            Meal newMeal = new Meal(meal);
            // if meal creation was success, add the meal to database
            App.getPrimaryDatabase().MEALS.addMeal(newMeal);
        } catch (Exception e) {
            handleActionFailure(dbOperations.ADD_MEAL, "Failed to create Meal instance: " + e.getMessage());
        }
    }

    public void successAddingMeal(Meal meal){
        try {
            if (Preconditions.isNotNull(meal) && Preconditions.isNotNull(uiScreen)) {
                // add meal locally
                Chef chef = (Chef) App.getUser();
                chef.MEALS.addMeal(meal);
                // let UI know about success
                uiScreen.dbOperationSuccessHandler(dbOperations.ADD_MEAL, "Meal added successfully!");
            } else {
                handleActionFailure( dbOperations.ADD_MEAL, "Failed to add meal locally: meal is null");
            }
        } catch (Exception e) {
            handleActionFailure( dbOperations.ADD_MEAL, "Failed to add meal locally: " + e.getMessage());
        }
    }

    /* ------               Add Meal To Offered Lis             ------- */

    public void successAddingMealToOfferedList(String mealId){
        try {
            if (Preconditions.isNotEmptyString(mealId) && Preconditions.isNotNull(uiScreen)) {
                // add meal locally
                ((Chef) App.getUser()).MEALS.addMealToOfferedList(mealId);
                // let UI know about success
                uiScreen.dbOperationSuccessHandler(dbOperations.ADD_MEAL_TO_OFFERED_LIST, "Meal set as offered!");
            } else {
                handleActionFailure( dbOperations.ADD_MEAL_TO_OFFERED_LIST, "meal ID or uiScreen is null");
            }
        } catch (Exception e) {
            handleActionFailure( dbOperations.ADD_MEAL_TO_OFFERED_LIST, "Failed to set meal as offered: " + e.getMessage());
        }
    }

    /* ------                   Remove Meal                    ------- */

    public void successRemovingMeal(String mealId) {
        if (Preconditions.isNotEmptyString(mealId) && Preconditions.isNotNull(uiScreen)) {

        } else {

        }
    }

    /* ------        Remove Meal From Offered List              ------- */

    public void successRemovingMealFromOfferedList(String mealId){}

    /* ------                   Update Meal                    ------- */

    public void successUpdatingMealInfo(Meal meal){}

    /* ------               Update Offered Meals                 ------- */

    public void successUpdatingOfferedMeals(Meal meal){}


    /* ------             Add to Searchable List               ------- */

    public void successAddingMealToSearchableList(Map meal){}

    public void errorAddingMealToSearchableList(Map meal){}

    /* ------                   Get Menu                         ------- */

    public void successGettingMenu(Meal meal){}

    /* ------                   Get Meal By ID                   ------- */

    public void successGettingMealById(Meal meal){}

}
