package com.example.mealer_project.data.handlers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.entity_models.MealEntityModel;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.ui.core.StatefulView;
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
        ADD_TO_SEARCHABLE_LIST,
        REMOVE_FROM_SEARCHABLE_LIST,
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
                            // below code might cause exception if validation fails or instance can't be created
                            Meal newMeal = new Meal((MealEntityModel) payload);
                            // if meal creation was success, add the meal to database
                            App.getPrimaryDatabase().MEALS.addMeal(newMeal);
                        } else {
                            handleActionFailure( operationType, "Invalid Meal Object provided");
                        }
                        break;

                    case ADD_MEAL_TO_OFFERED_LIST:
                        // update meal on remote database first
                        if (Preconditions.isNotNull(payload) && payload instanceof String) {
                            App.getPrimaryDatabase().MEALS.addMealToOfferedList((String) payload);
                        } else {
                            handleActionFailure( operationType, "Invalid meal ID provided");
                        }
                        break;

                    case REMOVE_MEAL:
                        // remove meal on remote database first
                        if (Preconditions.isNotNull(payload) && payload instanceof String) {
                            App.getPrimaryDatabase().MEALS.removeMeal((String) payload);
                        } else {
                            handleActionFailure( operationType, "Invalid meal ID provided");
                        }
                        break;

                    case REMOVE_MEAL_FROM_OFFERED_LIST:
                        if (Preconditions.isNotNull(payload) && payload instanceof String) {
                            App.getPrimaryDatabase().MEALS.removeMealFromOfferedList((String) payload);
                        } else {
                            handleActionFailure( operationType, "Invalid meal ID provided" + payload);
                        }
                        break;

                    case UPDATE_MEAL_INFO:
                        if (Preconditions.isNotNull(payload) && payload instanceof Meal) {
                            App.getPrimaryDatabase().MEALS.updateMealInfo((Meal) payload);
                        } else {
                            handleActionFailure( operationType, "Invalid Meal instance provided");
                        }
                        break;

                    case UPDATE_OFFERED_MEALS:
                        if (Preconditions.isNotNull(payload) && payload instanceof Map) {
                            updateOfferedMeals((Map<String, Boolean>) payload);
                        } else {
                            handleActionFailure( operationType, "Invalid object provided for map");
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
                                handleActionFailure( operationType, "Invalid arguments provided for getting meal by id");
                            }
                        } else {
                            handleActionFailure( operationType, "No arugments provided for getMealById");
                        }
                        break;

                    default:
                        Log.e("MealHandler dispatch", "Action not implemented yet");
                }
            } catch (Exception e) {
                Log.e("MealHandler Dispatch", "Exception: " + e.getMessage());
                uiScreen.dbOperationFailureHandler(dbOperations.ERROR, "Dispatch failed: " + e.getMessage());
            }

        } else {
            Log.e("MealHandler Dispatch", "Invalid instance provided for uiScreen");
        }
    }

    /**
     * Method which is called AFTER a successful database operation to make updates locally
     * @param operationType type of database operation which was successful
     * @param payload data for making changes locally
     */
    public void handleActionSuccess(dbOperations operationType, Object payload) {
        // ensure we have a valid uiScreen to inform of success
        if(Preconditions.isNotNull(uiScreen)) {

            try {
                switch (operationType) {

                    case ADD_MEAL:
                        if (Preconditions.isNotNull(payload) && payload instanceof Meal) {
                            // add meal locally
                            ((Chef) App.getUser()).MEALS.addMeal((Meal) payload);
                            // let UI know about success
                            uiScreen.dbOperationSuccessHandler(operationType, "Meal added successfully!");
                        } else {
                            handleActionFailure(operationType, "Invalid meal object provided");
                        }
                        break;

                    case ADD_MEAL_TO_OFFERED_LIST:
                        if (Preconditions.isNotNull(payload) && payload instanceof String) {
                            // update meal locally
                            ((Chef) App.getUser()).MEALS.addMealToOfferedList((String) payload);
                        } else {
                            handleActionFailure(operationType, "unable to update meal locally as offered, invalid payload");
                        }

                        break;

                    case REMOVE_MEAL:
                        if (Preconditions.isNotNull(payload) && payload instanceof String) {
                            ((Chef) App.getUser()).MEALS.removeMeal((String) payload);
                            // let UI know about success
                            uiScreen.dbOperationSuccessHandler(operationType, "Meal removed successfully!");
                        } else {
                            handleActionFailure(operationType, "Invalid meal ID");
                        }
                        break;

                    case REMOVE_MEAL_FROM_OFFERED_LIST:
                        if (Preconditions.isNotNull(payload) && payload instanceof String) {
                            ((Chef) App.getUser()).MEALS.removeMealFromOfferedList((String) payload);
                            // let UI know about success
                            uiScreen.dbOperationSuccessHandler(operationType, "Meal removed from offered list!");
                        } else {
                            handleActionFailure(operationType, "invalid meal ID");
                        }
                        break;

                    case UPDATE_MEAL_INFO:
                        if (Preconditions.isNotNull(payload) && payload instanceof Meal) {
                            // update meal locally
                            ((Chef) App.getUser()).MEALS.updateMeal((Meal) payload);
                            uiScreen.dbOperationSuccessHandler(operationType, "updated meal info!");
                        } else {
                            handleActionFailure( operationType, "Invalid Meal instance provided");
                        }
                        break;

                    case UPDATE_OFFERED_MEALS:
                        uiScreen.dbOperationSuccessHandler(operationType, "updated offered meals list!");
                        break;

                    case GET_MENU:
                        // update the Chef's meals locally
                        if (Preconditions.isNotNull(payload) && payload instanceof Map) {
                            Map<String, Meal> meals = (Map<String, Meal>) payload;
                            ((Chef) App.getUser()).MEALS.setMeals(meals);
                            uiScreen.dbOperationSuccessHandler(operationType, meals);
                        } else {
                            handleActionFailure(operationType, "Invalid payload for getMenu");
                        }
                        break;

                    case GET_MEAL_BY_ID:
                        if (Preconditions.isNotNull(payload) && payload instanceof Meal) {
                            uiScreen.dbOperationSuccessHandler(operationType, (Meal) payload);
                        } else {
                            handleActionFailure(operationType, "Failed to get meal by ID");
                        }
                        break;

                    default:
                        Log.e("handleActionSuccess", "Action not implemented yet");

                }
            } catch (Exception e) {
                Log.e("handleActionSuccess", "Success handler exception: " + e.getMessage());
                uiScreen.dbOperationFailureHandler(dbOperations.ERROR, "Failed to process request");
            }

        } else {
            Log.e("handleActionSuccess", "No UI Screen initialized");
        }
    }

    /**
     * Method which is called AFTER a failure in a database operation to inform UI
     * @param operationType type of database operation which failed
     * @param message a descriptive error message for the developers and analyst (not for client or chef)
     */
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

                default:
                    Log.e("handleActionSuccess", "Action not implemented yet");
            }

            // send error
            Log.e(tag, message);
            uiScreen.dbOperationFailureHandler(operationType, userMessage);

        } else {
            Log.e("handleActionFailure", "No UI Screen initialized");
        }
    }

    /**
     * Update meals to be offered or un-offered
     * @param data map containing IDs of the meals to be updated
     */
    private void updateOfferedMeals(@NonNull Map<String, Boolean> data) {
        Chef chef = (Chef) App.getUser();
        Map<String, Meal> meals = chef.MEALS.getMenu();
        // guard-clause
        if (Preconditions.isNotNull(data)) {
            for (String mealId: data.keySet()) {
                // check if meal doesn't exists
                if (meals.get(mealId) == null) {
                    handleActionFailure(dbOperations.UPDATE_OFFERED_MEALS, "Could not find a meal for the given meal ID");
                    return;
                }

                // check if meal needs to be updated
                if (meals.get(mealId).isOffered() != data.get(mealId)) {

                    // if meal needs to be added to offered list
                    if (data.get(mealId)) {
                        dispatch(dbOperations.ADD_MEAL_TO_OFFERED_LIST, mealId, uiScreen);
                    } else {
                        dispatch(dbOperations.REMOVE_MEAL_FROM_OFFERED_LIST, mealId, uiScreen);
                    }
                }
            }
            handleActionSuccess(dbOperations.UPDATE_OFFERED_MEALS, null);
        } else {
            handleActionFailure(dbOperations.UPDATE_OFFERED_MEALS, "Invalid data provided to updateOfferedMeals");
        }
    }

}
