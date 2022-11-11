package com.example.mealer_project.data.models;


import com.example.mealer_project.data.entity_models.MealEntityModel;

/**
 * This class is a template/blueprint for each instance of a Meal on a chef's menu
 */
public class Meal {
    private String errorMsg = "";

    private String name;
    private String mealID;
    private String chefID;
    private String cuisineType;
    private String mealType;
    private String ingredients;
    private String allergens;
    private String description;
    private boolean offered;
    private double price;
    /**
     * Create an instance of an existing meal with a mealID from FireBase
     * @param name Name of the meal
     * @param mealID ID number of the meal
     * @param chefID Chef ID of the meal
     * @param cuisineType Cuisine Type of the meal (Italian, Chinese, Greek)
     * @param mealType Meal Type (Main dish, Soup, Desert)
     * @param ingredients Ingredients used in the meal
     * @param allergens Potential allergens in meal
     * @param description Short description of the meal
     * @param offered Whether the meal is currently offered or not
     * @param price Current price of the meal
     */
    public Meal(String name, String mealID, String chefID, String cuisineType, String mealType,
         String ingredients, String allergens, String description, boolean offered, double price) {

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

    /**
     * Create an instance of meal using MealEntityModel
     * @param mealEntityModel meal info to create
     */
    public Meal (MealEntityModel mealEntityModel){

        this.setName( mealEntityModel.getName());
        this.setMealID( mealEntityModel.getMealID());
        this.setChefID( mealEntityModel.getChefID());
        this.setCuisineType( mealEntityModel.getCuisineType());
        this.setMealType( mealEntityModel.getMealType());
        this.setIngredients( mealEntityModel.getIngredients());
        this.setAllergens( mealEntityModel.getAllergens());
        this.setDescription( mealEntityModel.getDescription());
        this.setOffered( mealEntityModel.isOffered());
        this.setPrice( mealEntityModel.getPrice());

    }

    /**
     * Get the name of the meal
     * @return Meal name
     */
    public String getName() { return name; }

    /**
     * validates and sets/changes the name of the meal
     * @param name Meal's name
     */
    public void setName(String name) {

        // Process: validating the name
        if (validateName(name) == true) { //valid

            this.name = name; //setting name

        }
        else { //invalid

            // Output: error message
            throw new IllegalArgumentException(errorMsg);

        }

    }

    /**
     * this helper method validates the name and checks that it's not empty
     * @param name the name of the meal
     * @return whether the name is valid or not
     */
    private boolean validateName(String name) {

        // Process: checking name length
        if (name.length() > 0) { //at least 1 char

            if (name.length() > 50) { //too long

                errorMsg = "Please limit the meal name to 50 characters"; //updating error msg

                return false;

            }

            return true;

        }
        else { //nothing inputted

            errorMsg = "Meal cannot be unnamed"; //updating error msg

            return false;

        }

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
     * validates & sets/changes Meal's Cuisine Type
     * @param cuisineType of meal
     */
    public void setCuisineType(String cuisineType) {

        // Process: validating the cuisine type
        if (validateCuisine(cuisineType)) { //valid

            this.cuisineType = cuisineType;

        }
        else { //invalid

            throw new IllegalArgumentException(errorMsg);

        }

    }

    /**
     * this helper method validates the cuisine type
     * @param cuisineType the cuisine type
     * @return whether it is valid or not
     */
    private boolean validateCuisine(String cuisineType) {

        if (cuisineType.length() > 0) { //at least 1 char

            // Variable Declaration
            char[] charsInCuisine = cuisineType.toCharArray();

            // Process: validating input
            for (int i = 0; i < charsInCuisine.length; i++) {

                // Process: checking for all letters
                if (!Character.isLetter(charsInCuisine[i])) { //is not letter

                    if (!(charsInCuisine[i] == 45 || charsInCuisine[i] == 32)) { //not hyphen or space

                        errorMsg = "Invalid characters in cuisine type";

                        return false;

                    }

                }

            }

            return true;

        }
        else { //nothing inputted

            errorMsg = "Cuisine type cannot be empty";

            return false;

        }

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

        // Process: validating the cuisine type
        if (ingredients.length() > 0) { //valid

            this.ingredients = ingredients;

        }
        else { //invalid

            throw new IllegalArgumentException("Please specify the ingredients in this meal");

        }

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

        if (description.length() >= 20) { //valid

            this.description = description;

        }
        else { //too short or nothing inputted

            throw new IllegalArgumentException("Description should be at least 20 characters long");

        }

    }

    /**
     * Check if a meal is currently being offered by a chef or not
     * @return Boolean representing True if offered, False if not offered
     */
    public boolean isOffered() { return offered; }

    /**
     * Set/Change the state of whether meal is offered or not
     * @param offered offered or not
     */
    public void setOffered(boolean offered) {
        this.offered = offered;
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
