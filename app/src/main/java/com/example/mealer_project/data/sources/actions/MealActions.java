package com.example.mealer_project.data.sources.actions;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Meal;
import com.example.mealer_project.data.sources.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

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

        // Retrieve chefId from App instance
        String chefId = App.getAppInstance().getUser().getUserId();

        // Reference to database path to specific chef
        DocumentReference userReference = database.collection(CHEF_COLLECTION).document(chefId);

        Map<String, Object> databaseMeal = new HashMap<>();

    }
}
