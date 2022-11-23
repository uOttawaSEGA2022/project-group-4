package com.example.mealer_project.data.models.orders;
import com.example.mealer_project.data.models.meals.Meal;

public class MealInfo {

    private String name;
    private String mealID;
    private double price;
    /**
     * Create an instance of mealInfo using what is stored in an order object in Firebase
     * @param name Name of the meal
     * @param mealID ID number of the meal
     * @param price Current price of the meal
     */
    public MealInfo(String name, String mealID, double price) {

        this.setName(name);
        this.setMealID(mealID);
        this.setPrice(price);
    }


    /**
     * Create an instance of MealInfo using Meal
     * @param meal  meal data used to create
     */
    public MealInfo (Meal meal) {

        this.setName( meal.getName());
        this.setMealID( meal.getMealID());
        this.setPrice( meal.getPrice());

    }


    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getMealID() { return mealID; }

    public void setMealID(String mealID) {
        this.mealID = mealID;
    }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

}
