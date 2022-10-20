package com.example.mealer_project.data.models;

import androidx.annotation.NonNull;

import com.example.mealer_project.data.entity_models.UserEntityModel;

import java.util.Map;
import java.util.HashMap;

/**
 * This class instantiates an instance of Chef for Mealer App
 * Child Class of User
 */
public class Chef extends User {
    private String description;
    private String voidCheque;
    private int chefRating;
    private int numOfMealsSold;
    private Map<String, Meal> chefMenu;
    /**
     * Create a single instance of chef
     * @param firstName First name of the chef
     * @param lastName Last name of the chef
     * @param email email of the chef
     * @param password password for the chef
     * @param address address of the chef
     * @param role Role of the chef
     * @param description Description of the chef
     * @param voidCheque Chequing information of chef
     * Menu of a chef is stored in a HashMap
     */
    public Chef(String firstName, String lastName, String email, String password, Address address,
         UserRoles role, String description, String voidCheque, int numberOfMealsSold, int chefRating) {
        // instantiate Admins data members
        super(firstName, lastName, email, password, address, role);
        this.setDescription(description);
        this.setVoidCheque(voidCheque);
        this.setNumOfMealsSold(numOfMealsSold);
        this.setChefRating(chefRating);
        this.chefMenu = new HashMap<String, Meal>(); //<MealID, Meal> key-value pair
    }

    public Chef(UserEntityModel userData, UserRoles role, Address address, String description, String voidCheque) {
        // instantiate Admins data members
        super(userData.getFirstName(), userData.getLastName(), userData.getEmail(), userData.getPassword(), address, role);
        this.setDescription(description);
        this.setVoidCheque(voidCheque);
        this.setNumOfMealsSold(0);
        this.setChefRating(0);
        this.chefMenu = new HashMap<String, Meal>(); //<MealID, Meal> key-value pair
    }

    /**
     * Get a short description of the chef
     * @return String representing chef's description
     */
    public String getDescription() { return description; }

    /**
     * Set the chef's description
     * @param description String representing the chef's description
     */
    public void setDescription(String description) {
        // validate description
        this.description = description;
    }

    /**
     * Get chequing information about chef
     * @return String representing void cheque of chef
     */
    public String getVoidCheque() {
        return voidCheque;
    }

    /**
     * Set the chef's void cheque
     * @param voidCheque String representing void cheque of chef
     */
    public void setVoidCheque(String voidCheque) {
        // validate void cheque
        this.voidCheque = voidCheque;
    }

    /**
     * Get the average rating of a chef
     * @return Integer representing chef's overall rating
     */
    public int getChefRating() { return chefRating; }

    /**
     * Set the chef's rating
     * @param chefRating integer representing the chef's rating
     */
    public void setChefRating(int chefRating) {
        // validate rating
        this.chefRating = chefRating;
    }

    /**
     * Get the total number of meals sold by a chef
     * @return Integer representing chef's total sales
     */
    public int getNumOfMealsSold() { return numOfMealsSold; }

    /**
     * Set the chef's total meals sold
     * @param numOfMealsSold integer representing the chef's total sales
     */
    public void setNumOfMealsSold(int numOfMealsSold) {
        // validate num of meals sold
        this.numOfMealsSold = numOfMealsSold;
    }

    /**
     * Retrieve a meal from the chef's menu
     * @param mealID representing the ID of the meal
     * @return Meal instance from chef's menu
     */
    public Meal getMeal(String mealID) {
        return this.chefMenu.get(mealID);
    }

    /**
     * Add a new meal to the chef's menu
     * @param newMeal Meal to be added to menu
     */
    public void addMeal(@NonNull Meal newMeal) {
        chefMenu.put(newMeal.getMealID(), newMeal);
    }

    /**
     * Remove a meal from the chef's menu
     * @param removeMeal Meal to be removed from menu
     */
    public void removeMeal(@NonNull Meal removeMeal) {
        chefMenu.remove(removeMeal.getMealID());
    }
}
