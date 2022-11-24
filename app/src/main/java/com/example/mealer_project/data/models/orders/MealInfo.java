package com.example.mealer_project.data.models.orders;
import com.example.mealer_project.data.models.meals.Meal;

import java.io.Serializable;

public class MealInfo implements Serializable {

    String name;
    double price;
    int quantity;

    /**
     * Create an instance of mealInfo using what is stored in an order object in Firebase
     * @param name Name of the meal
     * @param quantity Quantity of the meal
     * @param price Current price of the meal
     */
    public MealInfo(String name, double price, int quantity) {
        this.setName(name);
        this.setQuantity(quantity);
        this.setPrice(price);
    }


    /**
     * Create an instance of MealInfo using Meal
     * @param meal  meal data used to create
     */
    // must set quantity afterwards
    public MealInfo (Meal meal) {
        this.setName( meal.getName());
        this.setPrice( meal.getPrice());
    }


    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

}
