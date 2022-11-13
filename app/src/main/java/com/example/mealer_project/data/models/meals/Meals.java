package com.example.mealer_project.data.models.meals;

import androidx.annotation.NonNull;

import com.example.mealer_project.data.models.Meal;
import com.example.mealer_project.utils.Preconditions;
import com.example.mealer_project.utils.Response;
import com.example.mealer_project.utils.Result;

import java.util.HashMap;
import java.util.Map;

public class Meals {

    // Store Meals in a Map<MealID, Meal> key-value pairs
    private Map<String, Meal> meals;

    /**
     * Default constructor initializes a HashMap for storing Meals
     */
    public Meals() {
        this.meals = new HashMap<>(); //<MealID, Meal> key-value pair
    }

    /**
     * Retrieve a meal from all meals of the Chef by meal ID
     * @param mealID representing the ID of the meal
     * @return a Result object containing the meal is successful in getting the associated meal, else error message
     */
    public Result<Meal, String> getMeal(@NonNull String mealID) {
        // guard-clause
        if (Preconditions.isNotEmptyString(mealID)) {
            // check if meal exists
            if (this.meals.get(mealID) != null) {
                return new Result<>(this.meals.get(mealID), null);
            } else {
                return new Result<>(null, "Could not find any meal for the provided meal ID");
            }
        } else {
            return new Result<>(null, "Invalid meal ID provided");
        }
    }

    public void setMeals(@NonNull Map<String, Meal> mealsData) {
        this.meals = mealsData;
    }

    /**
     * Add a new meal to the chef's list of meals (menu)
     * @param newMeal Meal to be added
     */
    public Response addMeal(@NonNull Meal newMeal) {
        // guard-clause
        // meal must have a valid id which will be used as a key
        if (Preconditions.isNotEmptyString(newMeal.getMealID())) {
            // check if meal already exists
            if (this.meals.get(newMeal.getMealID()) != null) {
                return new Response(false, "Meal with same ID already exists! Use updateMeal to update an existing meal");
            }
            // add the new meal
            this.meals.put(newMeal.getMealID(), newMeal);
            // return success
            return new Response(true);
        } else {
            return new Response(false, "Meal does not have a valid ID");
        }
    }

    /**
     * Add a meal to the chef's offered meal
     * @param mealId ID of the meal that needs to be added to the menu
     * @return Response object indicating success, or failure (with an error message)
     */
    public Response addMealToOfferedList(@NonNull String mealId) {
        // guard-clause
        if (Preconditions.isNotEmptyString(mealId)) {
            // check if meal exists and has a valid object
            if (this.meals.get(mealId) != null) {
                // add meal to the menu
                this.meals.get(mealId).setOffered(true);
                // return success
                return new Response(true);
            } else {
                return new Response(false, "Could not find the any meal for the provided ID");
            }
        } else {
            return new Response(false, "Meal does not have a valid ID");
        }
    }

    /**
     * Remove a meal from the list of all meals added by a Chef
     * @param mealId ID of the meal to be removed
     */
    public Response removeMeal(@NonNull String mealId) {
        // guard-clause
        if (Preconditions.isNotEmptyString(mealId)) {
            // check if meal exists
            if (this.meals.get(mealId) != null) {
                // remove the meal
                this.meals.remove(mealId);
                // return operation success
                return new Response(true);
            } else {
                return new Response(false, "Could not find any meal for the provided meal ID");
            }
        } else {
            return new Response(false, "Invalid meal ID provided");
        }
    }

    /**
     * Remove a meal from Chef's offered meals
     * @param mealId ID of the meal to be removed
     */
    public Response removeMealFromOfferedList(@NonNull String mealId) {
        // guard-clause
        if (Preconditions.isNotEmptyString(mealId)) {
            // check if meal exists
            if (this.meals.get(mealId) != null) {
                // remove meal from menu
                this.meals.get(mealId).setOffered(false);
                // return operation success
                return new Response(true);
            } else {
                return new Response(false, "Could not find any meal for the provided meal ID");
            }
        } else {
            return new Response(false, "Invalid meal ID provided");
        }
    };

    /**
     * Update an existing meal
     * @param meal meal instance, must have a valid meal ID
     * @return Response indicating operation success or failure
     */
    public Response updateMeal(@NonNull Meal meal) {
        // guard-clause
        // meal must have a valid id which will be used as a key
        if (Preconditions.isNotEmptyString(meal.getMealID())) {
            // check if meal doesn't exists
            if (!this.meals.containsKey(meal.getMealID())) {
                return new Response(false, "Could not find a meal for the given meal ID");
            }
            // update the meal
            this.meals.put(meal.getMealID(), meal);
            // return success
            return new Response(true);
        } else {
            return new Response(false, "Meal does not have a valid ID");
        }
    }

    /**
     * Method to retrieve a map object containing meals which are currently being offered by Chef
     * @return a Map containing Meal ID's as keys and Meal objects as values
     */
    public Map<String, Meal> getOfferedMeals() {
        // map to store the result
        HashMap<String, Meal> offeredMeals = new HashMap<>();
        // filter and add offered meals to above map
        for (Meal meal : this.meals.values()) {
            if (meal.isOffered()) {
                offeredMeals.put(meal.getMealID(), meal);
            }
        }
        // return the result
        return offeredMeals;
    }

    /**
     * Method to retrieve a map object containing all meals added by the Chef
     * @return a Map containing Meal ID's as keys and Meal objects as values
     */
    public Map<String, Meal> getMenu() {
        return this.meals;
    };
}
