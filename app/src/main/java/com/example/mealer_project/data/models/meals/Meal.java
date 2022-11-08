package com.example.mealer_project.data.models.meals;


/**
 * This class is a template/blueprint for each instance of a Meal on a chef's menu
 */
public class Meal {
    private String name;
    private String mealID;
    private String chefID;
    private String cuisineType;
    private String mealType;
    private String ingredients;
    private String allergens;
    private String description;
    private boolean offered;
    private boolean onMenu;
    private double price;
    /**
     * Create an instance of meal
     * @param name Name of the meal
     * @param mealID ID number of the meal
     * @param chefID Chef ID of the meal
     * @param cuisineType Cuisine Type of the meal (Italian, Chinese, Greek)
     * @param mealType Meal Type (Main dish, Soup, Desert)
     * @param ingredients Ingredients used in the meal
     * @param allergens Potential allergens in meal
     * @param description Short description of the meal
     * @param offered Whether the meal is currently offered or not
     * @param onMenu Whether the meal is part of menu or not
     * @param price Current price of the meal
     */
    Meal(String name, String mealID, String chefID, String cuisineType, String mealType,
         String ingredients, String allergens, String description, boolean offered, boolean onMenu, double price) {

        this.setName(name);
        this.setMealID(mealID);
        this.setChefID(chefID);
        this.setCuisineType(cuisineType);
        this.setMealType(mealType);
        this.setIngredients(ingredients);
        this.setAllergens(allergens);
        this.setDescription(description);
        this.setOffered(offered);
        this.setOnMenu(onMenu);
        this.setPrice(price);
    }

    /**
     * Get the name of the meal
     * @return Meal name
     */
    public String getName() { return name; }

    /**
     * Set/Change the name of the meal
     * @param name Meal's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the ID String of the meal
     * @return Meal's ID (Identification)
     */
    public String getMealID() { return mealID; }

    /**
     * Set/Change the ID of the meal
     * @param mealID of meal
     */
    public void setMealID(String mealID) {
        this.mealID = mealID;
    }

    /**
     * Get the ID of the chef who cooked meal
     * @return Chef's ID
     */
    public String getChefID() { return chefID; }

    /**
     * Set/Change Chef's ID
     * @param chefID of meal
     */
    public void setChefID(String chefID) {
        this.chefID = chefID;
    }

    /**
     * Get the cuisine type of the meal
     * @return Meal's Cuisine Type
     */
    public String getCuisineType() { return cuisineType; }

    /**
     * Change Meal's Cuisine Type
     * @param cuisineType of meal
     */
    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    /**
     * Get the type of the meal (Main dish, soup, etc)
     * @return Meal's type
     */
    public String getMealType() { return mealType; }

    /**
     * Change the type of the meal
     * @param mealType of meal
     */
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    /**
     * Get the list of ingredients used in the meal
     * @return Ingredients used in meal
     */
    public String getIngredients() { return ingredients; }

    /**
     * Set/Change the list of ingredients used in meal
     * @param ingredients list
     */
    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * Get the list of allergens of the meal
     * @return Allergen list for meal
     */
    public String getAllergens() { return allergens; }

    /**
     * Set/Change the list of allergens of the meal
     * @param allergens list
     */
    public void setAllergens(String allergens) {
        this.allergens = allergens;
    }

    /**
     * Get a short description of the meal
     * @return Meal Description
     */
    public String getDescription() { return description; }

    /**
     * Set/Change the description of the meal
     * @param description of meal
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Check if a meal is currently being offered by a chef or not
     * @return Boolean representing True if offered, False if not offered
     */
    public boolean isOffered() { return this.offered; }

    /**
     * Set/Change the state of whether meal is offered or not
     * @param offered offered or not
     */
    public void setOffered(boolean offered) {
        this.offered = offered;
    }

    /**
     * Check if a meal is currently part of Chef's menu or not
     * @return Boolean representing True if part of menu, False if not
     */
    public boolean isOnMenu() { return this.onMenu; }

    /**
     * Set/Change the state of whether meal is part of menu or not
     * @param onMenu part of menu or not
     */
    public void setOnMenu(boolean onMenu) {
        this.onMenu = onMenu;
    }

    /**
     * Get the price of the meal offered by chef
     * @return Meal price
     */
    public double getPrice() { return price; }

    /**
     * Set/Change the price of the meal
     * @param price Meal Price
     */
    public void setPrice(double price) {
        this.price = price;
    }
}
