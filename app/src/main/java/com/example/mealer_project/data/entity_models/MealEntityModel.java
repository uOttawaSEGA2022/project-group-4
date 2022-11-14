package com.example.mealer_project.data.entity_models;

import java.util.ArrayList;

public class MealEntityModel {

    private String name;
    private String mealID;
    private String chefID;
    private String cuisineType;
    private String mealType;
    private String ingredients;
    private ArrayList<String> allergens;
    private String description;
    private boolean offered;
    private double price;

    public MealEntityModel() {
    }

    public MealEntityModel(String name, String mealID, String chefID, String cuisineType, String mealType,
                           String ingredients, ArrayList<String> allergens, String description, boolean offered, double price) {

        this.setName(name);
        this.setMealID(mealID);
        this.setChefID(chefID);
        this.setCuisineType(cuisineType);
        this.setMealType(mealType);
        this.setIngredients(ingredients);
        this.setAllergens(allergens);
        this.setDescription(description);
        this.setOffered(offered);
        this.setPrice(price);
    }

    public MealEntityModel(String name, String chefID, String cuisineType, String mealType,
                           String ingredients, ArrayList<String> allergens, String description, boolean offered, double price) {

        this.setName(name);
        this.setMealID(mealID);
        this.setChefID(chefID);
        this.setCuisineType(cuisineType);
        this.setMealType(mealType);
        this.setIngredients(ingredients);
        this.setAllergens(allergens);
        this.setDescription(description);
        this.setOffered(offered);
        this.setPrice(price);
    }


    public String getName() { return name;}

    public void setName(String name) {
        this.name = name;
    }

    public String getMealID() { return mealID;}

    public void setMealID(String mealID) {
        this.mealID = mealID;
    }

    public String getChefID() { return chefID;}

    public void setChefID(String chefID) {
        this.chefID = chefID;
    }

    public String getCuisineType() { return cuisineType;}

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getMealType() { return mealType;}

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getIngredients() { return ingredients;}

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getAllergens() { return allergens;}

    public void setAllergens(ArrayList<String> allergens) {
        this.allergens = allergens;
    }

    public String getDescription() { return description;}

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOffered() { return offered;}

    public void setOffered(boolean offered) {
        this.offered = offered;
    }

    public double getPrice() { return price;}

    public void setPrice(double price) {
        this.price = price;
    }

}