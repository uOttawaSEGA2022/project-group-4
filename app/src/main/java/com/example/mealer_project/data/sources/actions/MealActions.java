package com.example.mealer_project.data.sources.actions;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.entity_models.MealEntityModel;
import com.example.mealer_project.data.handlers.MealHandler;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Meal;
import com.example.mealer_project.utils.Preconditions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    /**
     * Add meal to list of meals in Firebase
     * @param meal The meal to be added
     */
    public void addMeal(Meal meal){

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

            // Add meal to specific chef's list of meals
            database.collection(CHEF_COLLECTION)
                    .document(App.getUserId())
                    .collection("meals")
                    .add(databaseMeal)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // update complaint id
                            meal.setMealID(documentReference.getId());
                            App.MEAL_HANDLER.successAddingMeal(meal);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            App.MEAL_HANDLER.errorAddingMeal("Failed to add meal to specific chef in database: " + e.getMessage());
                        }
                    });

            database.collection(MEAL_COLLECTION)
                    .document(meal.getMealID())
                    .set(databaseMeal)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //App.MEAL_HANDLER.successAddingMeal(meal);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //App.MEAL_HANDLER.errorAddingMeal("Failed to add meal to list of meals in database: " + e.getMessage());
                        }
                    });


        } else {
            // if Preconditions fail
            Log.e("addMeal", "Invalid object value for meal");
        }
    }

    /**
     * Remove meal from searchable list of meals in Firebase
     * @param mealId The mealId of meal to be removed
     */
    public void removeMeal(String mealId){

        if (Preconditions.isNotNull(mealId)) {

            Chef chef = (Chef) App.getUser();

            database.collection(CHEF_COLLECTION)
                    .document(chef.getUserId())
                    .collection("meals")
                    .document(mealId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            App.MEAL_HANDLER.successRemovingMeal(mealId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            App.MEAL_HANDLER.errorRemovingMeal("Failed to remove meal to searchable list in database: " + e.getMessage());
                        }
                    });

            database.collection(MEAL_COLLECTION)
                    .document(mealId)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            App.MEAL_HANDLER.successRemovingMeal(mealId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            App.MEAL_HANDLER.errorRemovingMeal("Failed to remove meal to searchable list in database: " + e.getMessage());
                        }
                    });
        } else {
            // if Preconditions fail
            Log.e("removeMealFromSearch", "Invalid object value for meal");
        }
    }

    /**
     * Set isOffered property to true to a meal in a specific chef's list of meals in Firebase
     * @param mealId The mealId of meal to be updated
     */
    public void addMealToOfferedList(String mealId){

        if (Preconditions.isNotNull(mealId)) {

            database.collection(CHEF_COLLECTION)
                    .document(App.getUserId())
                    .collection("meals")
                    .document(mealId)
                    .update("isOffered", true)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            App.MEAL_HANDLER.successAddingMealToOfferedList(mealId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            App.MEAL_HANDLER.errorAddingMealToOfferedList("Failed to add meal to offered list in chef in database: " + e.getMessage());
                        }
                    });

            // Set isOffered to false in list of meals in firebase
            database.collection(MEAL_COLLECTION)
                    .document(mealId)
                    .update("isOffered", false)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //App.MEAL_HANDLER.successAddingMealToOfferedList(mealId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //App.MEAL_HANDLER.errorAddingMealToOfferedList("Failed to add meal to offered list in chef in database: " + e.getMessage());
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
    public void removeMealFromOfferedList(String mealId){

        if (Preconditions.isNotNull(mealId)) {

            // Set isOffered to false in specific chef's list of meals
            database.collection(CHEF_COLLECTION)
                    .document(App.getUserId())
                    .collection("meals")
                    .document(mealId)
                    .update("isOffered", false)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            App.MEAL_HANDLER.successRemovingMeal(mealId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            App.MEAL_HANDLER.errorRemovingMeal("Failed to add meal to offered list in chef in database: " + e.getMessage());
                        }
                    });

            // Set isOffered to false in list of meals in firebase
            database.collection(MEAL_COLLECTION)
                    .document(mealId)
                    .update("isOffered", false)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //App.MEAL_HANDLER.successRemovingMealFromOfferedList(mealId);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //App.MEAL_HANDLER.errorRemovingMealFromOfferedList("Failed to add meal to offered list in chef in database: " + e.getMessage());
                        }
                    });

        } else {
            // if Preconditions fail
            Log.e("removeMealFromOffered", "Invalid object value for mealId");
        }
    }


    /**
     * Get meal from Firebase for the current logged in chef
     * @param mealId The mealId of meal
     */
    public void getMealById (String mealId) {

        Chef chef;

        try {
            chef = (Chef) App.getUser();

            DocumentReference mealReference = database.collection(CHEF_COLLECTION).document(chef.getUserId()).collection(MEAL_COLLECTION).document(mealId);

            mealReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            if (document.getData() != null){
                                App.MEAL_HANDLER.successGettingMealById(makeMealFromFirebase(document));
                            }

                        } else {
                            Log.d(TAG, "No such document");
                            App.MEAL_HANDLER.errorGettingMealById("Could not find a meal with provided ID");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                        App.MEAL_HANDLER.errorGettingMealById("Failed to retrieve meal");
                    }
                }
            });

        } catch (Exception e) {
            Log.e("getMealById", "Current logged in user is not a chef. Wrong getMealById overloaded method called?: " + e.getMessage());
            App.MEAL_HANDLER.errorGettingMealById("Unable to process request at this moment");
        }
    }

    /**
     * Get meal from Firebase given the mealId AND chefId
     * @param mealId The mealId of meal
     */
    public void getMealById (String mealId, String chefId) {

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
                           App.MEAL_HANDLER.successGettingMealById(makeMealFromFirebase(document));
                        }

                    } else {
                        Log.d(TAG, "No such document");
                        App.MEAL_HANDLER.errorGettingMealById("Could not find a meal with provided ID");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    App.MEAL_HANDLER.errorGettingMealById("Failed to retrieve meal");
                }
            }
        });
    }

    /**
     * Return map of meals from Firebase using app instance's current chef's ID
     */
    /////////////IMPLEMENT THIS!!!////////////////////
    public Map getMeals(){

        return null;
    }

    /**
     * Return map of offered meals from Firebase using app instance's current chef's ID
     */
    /////////////IMPLEMENT THIS!!!////////////////////
    public Map getOfferedMeals(){

        return null;
    }

    /**
     * Update meal info in Firebase in chef object and searchable meals list if it is offered
     * Removes list from offered list and searchable list if isOffered boolean is updated to false
     * @param meal new Meal object with info to update
     */
    public void updateMealInfo(Meal meal){

        if (Preconditions.isNotNull(meal)) {

                database.collection(CHEF_COLLECTION)
                        .document(App.getUserId())
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
                                App.MEAL_HANDLER.successUpdatingMealInfo(meal);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                App.MEAL_HANDLER.errorUpdatingMealInfo("Failed to update meal to list in chef in database: " + e.getMessage());
                            }
                        });

                database.collection(MEAL_COLLECTION)
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
                                //App.MEAL_HANDLER.successUpdatingMealInfo(meal);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                //App.MEAL_HANDLER.errorUpdatingMealInfo("Failed to update meal to searchable list in database: " + e.getMessage());
                            }
                        });
        } else {
            // if Preconditions fail
            Log.e("updateMealInfo", "Invalid object value for meal");
        }
    }

    /**
     * Update offered meals list and searchable list of meals in firebase accordingly
     * @param meals Map of meal IDs and boolean isOffered of each meal
     */
    /////////////IMPLEMENT THIS!!!////////////////////
    public void updateOfferedMeals(Map <String,Boolean> meals){

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
