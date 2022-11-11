package com.example.mealer_project.data.sources.actions;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.handlers.MealHandler;
import com.example.mealer_project.data.models.Meal;
import com.example.mealer_project.utils.Preconditions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

public class MealActions {

    FirebaseFirestore database;
    private final static String MEAL_COLLECTION = "Meals";
    private final static String CHEF_COLLECTION = "Chefs";


    public MealActions(FirebaseFirestore database) {
        this.database = database;
    }

    /*
     * Add meal to a specific chef's list of meals in Firebase
     * @param meal The meal to be added
     */
    protected void addMeal(Meal meal){

        if (Preconditions.isNotNull(meal)) {

            // Retrieve chefId and MealHandler from App instance
            String chefId = App.getAppInstance().getUser().getUserId();
            MealHandler mealHandler = App.getMealHandler();

            Map<String, Object> databaseMeal = new HashMap<>();

            databaseMeal.put("name", meal.getName());
            databaseMeal.put("mealID", meal.getMealID());
            databaseMeal.put("chefID", meal.getChefID());
            databaseMeal.put("cuisineType", meal.getCuisineType());
            databaseMeal.put("mealType", meal.getMealType());
            databaseMeal.put("ingredients", meal.getIngredients());
            databaseMeal.put("allergens", meal.getAllergens());
            databaseMeal.put("description", meal.getDescription());
            databaseMeal.put("isOffered", meal.isOffered());
            databaseMeal.put("price", meal.getPrice());


            database.collection(CHEF_COLLECTION)
                    .document(chefId)
                    .collection("meals")
                    .add(databaseMeal)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // update complaint id
                            meal.setMealID(documentReference.getId());
                            mealHandler.successAddingMeal(meal);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mealHandler.errorAddingMeal("Failed to add meal to chef in database: " + e.getMessage());
                        }
                    });

            // Adds meal to searchable list of meals in firebase only if offered
            if (meal.isOffered()){
                addMealToSearchableList(databaseMeal);
            }


        } else {
            // if Preconditions fail
            Log.e("addMeal", "Invalid object value for meal");
        }
    }

    /*
     * Add meal to a specific chef's offered list of meals in Firebase
     * @param meal The meal to be added
     */
    protected void addMealToOfferedList(String mealId){

        if (Preconditions.isNotNull(mealId)) {

            // Retrieve chefId and MealHandler from App instance
            String chefId = App.getAppInstance().getUser().getUserId();
            MealHandler mealHandler = App.getMealHandler();


            database.collection(CHEF_COLLECTION)
                    .document(chefId)
                    .collection("meals")
                    .document(mealId)
                    .update("isOffered", true)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            mealHandler.successAddingMealToOfferedList(mealId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mealHandler.errorAddingMealToOfferedList("Failed to set meal to offered in chef in database: " + e.getMessage());
                        }
                    });
        } else {
            // if Preconditions fail
            Log.e("addMeal", "Invalid object value for meal");
        }
    }


    protected void addMealToSearchableList(Map meal){

        MealHandler mealHandler = App.getMealHandler();

        database.collection(MEAL_COLLECTION)
                .document(String.valueOf(meal.get("mealID")))
                .set(meal)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        mealHandler.successAddingMealToSearchableList(meal);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mealHandler.errorAddingMealToSearchableList("Failed to add meal to meals list in database: " + e.getMessage());
                    }
                });

    }




}
