package com.example.mealer_project.data.sources.actions;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.entity_models.AddressEntityModel;
import com.example.mealer_project.data.entity_models.CreditCardEntityModel;
import com.example.mealer_project.data.entity_models.MealEntityModel;
import com.example.mealer_project.data.entity_models.UserEntityModel;
import com.example.mealer_project.data.handlers.MealHandler;
import com.example.mealer_project.data.models.Address;
import com.example.mealer_project.data.models.Client;
import com.example.mealer_project.data.models.CreditCard;
import com.example.mealer_project.data.models.Meal;
import com.example.mealer_project.data.models.UserRoles;
import com.example.mealer_project.utils.Preconditions;
import com.example.mealer_project.utils.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

public class MealActions {

    FirebaseFirestore database;
    private final MealHandler mealHandler;
    private String chefId;
    private final static String MEAL_COLLECTION = "Meals";
    private final static String CHEF_COLLECTION = "Chefs";


    public MealActions(FirebaseFirestore database) {
        this.database = database;
        this.mealHandler = App.getMealHandler();
        this.chefId = App.getAppInstance().getUser().getUserId();
    }

    /**
     * Add meal to a specific chef's list of meals in Firebase
     * @param meal The meal to be added
     */
    protected void addMeal(Meal meal){

        if (Preconditions.isNotNull(meal)) {

            Map<String, Object> databaseMeal = new HashMap<>();

            databaseMeal.put("name", meal.getName());
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

    /**
     * Set isOffered property to true to a meal in a specific chef's list of meals in Firebase
     * @param mealId The mealId of meal to be updated
     */
    protected void addMealToOfferedList(String mealId){

        if (Preconditions.isNotNull(mealId)) {

            database.collection(CHEF_COLLECTION)
                    .document(chefId)
                    .collection("meals")
                    .document(mealId)
                    .update("isOffered", true)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mealHandler.successAddingMealToOfferedList(mealId);
                            addMealToSearchableList(mealToMapConversion(getMealFromMealId(mealId, chefId)));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mealHandler.errorAddingMeal("Failed to add meal to offered list in chef in database: " + e.getMessage());
                        }
                    });
        } else {
            // if Preconditions fail
            Log.e("addMealToOfferedList", "Invalid object value for mealId");
        }
    }


    /**
     * Set isOffered property to false to a meal in a specific chef's list of meals in Firebase
     * @param mealId The mealId of meal to be updated
     */
    protected void removeMealFromOfferedList(String mealId){

        if (Preconditions.isNotNull(mealId)) {

            database.collection(CHEF_COLLECTION)
                    .document(chefId)
                    .collection("meals")
                    .document(mealId)
                    .update("isOffered", false)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mealHandler.successRemovingMeal(mealId);
                            removeMealFromSearchableList(mealId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mealHandler.errorRemovingMeal("Failed to add meal to offered list in chef in database: " + e.getMessage());
                        }
                    });
        } else {
            // if Preconditions fail
            Log.e("removeMealFromOffered", "Invalid object value for mealId");
        }
    }

    /**
    * Add meal to searchable list of meals in Firebase
    * @param meal The meal in a map format to be added
    */
    protected void addMealToSearchableList(Map meal){

        if (Preconditions.isNotNull(meal)) {

            database.collection(MEAL_COLLECTION)
                    .document(String.valueOf(meal.get("MealID")))
                    .set(meal)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mealHandler.successAddingMealToSearchableList(meal);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mealHandler.errorAddingMeal("Failed to add meal to searchable list in database: " + e.getMessage());
                        }
                    });
        } else {
            // if Preconditions fail
            Log.e("addMealToSearchableList", "Invalid object value for meal");
        }
    }

    /**
     * Remove meal from searchable list of meals in Firebase
     * @param mealId The mealId of meal to be removed
     */
    protected void removeMealFromSearchableList(String mealId){

        if (Preconditions.isNotNull(mealId)) {

            database.collection(MEAL_COLLECTION)
                    .document(mealId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mealHandler.successRemovingMeal(mealId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mealHandler.errorRemovingMeal("Failed to remove meal to searchable list in database: " + e.getMessage());
                        }
                    });
        } else {
            // if Preconditions fail
            Log.e("removeMealFromSearch", "Invalid object value for meal");
        }
    }

    /**
     * Get meal from Firebase given the mealId AND chefId
     * @param mealId The mealId of meal
     * @param chefId The chefId of chef
     */
    /////////////FIX THIS!!!////////////////////
    protected Meal getMealFromMealId (String mealId, String chefId){

        Meal meal;
        DocumentReference mealReference = database.collection(CHEF_COLLECTION).document(chefId).collection(MEAL_COLLECTION).document(mealId);

        mealReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        if (document.getData() != null){
                           meal = makeMealFromFirebase(document);
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return meal;
    }

    /**
     * Return map of meals from Firebase given the current chef's chefId
     */
    /////////////FIX THIS!!!////////////////////
    protected Map getMeals(){

        Map meals;
        CollectionReference mealReference = database.collection(CHEF_COLLECTION).document(chefId).collection(MEAL_COLLECTION);

        mealReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        if (document.getData() != null){
                            meals = (Map)document.getData();
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return meals;
    }


    protected void updateMealInfo(Meal meal){

        if (Preconditions.isNotNull(meal)) {

            database.collection(CHEF_COLLECTION)
                    .document(chefId)
                    .collection("meals")
                    .document(meal.getMealID())
                    .update("name",meal.getName(),
                            "cuisineType", meal.getCuisineType(),
                            "mealType", meal.getMealType(),
                            "ingredients", meal.getIngredients(),
                            "allergens", meal.getAllergens(),
                            "description", meal.getDescription(),
                            "isOffered", meal.isOffered(),
                            "price", meal.getPrice())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mealHandler.successUpdatingMealInfo(meal);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mealHandler.errorUpdatingMealInfo("Failed to update meal to list in chef in database: " + e.getMessage());
                        }
                    });
        } else {
            // if Preconditions fail
            Log.e("updateMealInfo", "Invalid object value for meal");
        }
    }


    private Meal makeMealFromFirebase(DocumentSnapshot document) {

        if (document.getData() == null) {
            throw new NullPointerException("makeClientFromFirebase: invalid document object");
        }

        MealEntityModel newMeal = new MealEntityModel();
        Meal meal = new Meal(newMeal);

        newMeal.setName(String.valueOf(document.getData().get("name")));
        newMeal.setChefID(String.valueOf(document.getData().get("chefId")));
        newMeal.setCuisineType(String.valueOf(document.getData().get("cuisineType")));
        newMeal.setMealType(String.valueOf(document.getData().get("mealType")));
        newMeal.setIngredients(String.valueOf(document.getData().get("ingredients")));
        newMeal.setAllergens(String.valueOf(document.getData().get("allergens")));
        newMeal.setDescription(String.valueOf(document.getData().get("description")));
        newMeal.setOffered((Boolean) document.getData().get("isOffered"));
        newMeal.setPrice((Double) document.getData().get("price"));

        return meal;

    }

    private Map mealToMapConversion(Meal meal){

        Map<String, Object> mealMap = new HashMap<>();

        mealMap.put("name", meal.getName());
        mealMap.put("mealId", meal.getMealID());
        mealMap.put("chefID", meal.getChefID());
        mealMap.put("cuisineType", meal.getCuisineType());
        mealMap.put("mealType", meal.getMealType());
        mealMap.put("ingredients", meal.getIngredients());
        mealMap.put("allergens", meal.getAllergens());
        mealMap.put("description", meal.getDescription());
        mealMap.put("isOffered", meal.isOffered());
        mealMap.put("price", meal.getPrice());

        return mealMap;
    }





}
