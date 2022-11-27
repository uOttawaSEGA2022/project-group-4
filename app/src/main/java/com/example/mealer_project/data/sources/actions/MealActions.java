package com.example.mealer_project.data.sources.actions;

import static android.content.ContentValues.TAG;

import static com.example.mealer_project.data.handlers.MealHandler.dbOperations.*;
import static com.example.mealer_project.data.sources.FirebaseCollections.*;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.entity_models.AddressEntityModel;
import com.example.mealer_project.data.entity_models.MealEntityModel;
import com.example.mealer_project.data.handlers.UserHandler;
import com.example.mealer_project.data.models.Address;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.data.models.orders.ChefInfo;
import com.example.mealer_project.ui.screens.search.SearchMealItem;
import com.example.mealer_project.ui.screens.LoginScreen;
import com.example.mealer_project.utils.Preconditions;
import com.example.mealer_project.utils.Result;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MealActions {

    FirebaseFirestore database;

    public MealActions(FirebaseFirestore database) {
        this.database = database;
    }


    private void addChefMeal(String chefMealsId, Meal meal, String chefName, String chefAddress) {

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
        databaseMeal.put("keywords", meal.getSearchMealItemKeywords(chefName, chefAddress));

        database.collection(MEALS_COLLECTION)
                .document(chefMealsId)
                .collection(CHEF_MEALS_COLLECTION)
                .add(databaseMeal)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // update meal id
                        meal.setMealID(documentReference.getId());
                        Log.e("MEAL ID", documentReference.getId());
                        App.MEAL_HANDLER.handleActionSuccess(ADD_MEAL, meal);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        App.MEAL_HANDLER.handleActionFailure(ADD_MEAL, "Failed to add meal to chef in database: " + e.getMessage());
                    }
                });
    }

    /**
     * Add meal to list of meals in Firebase
     * @param meal The meal to be added
     */
    public void addMeal(Meal meal) {

        if (Preconditions.isNotNull(meal)) {
            // confirm a Chef is logged in and get the chef
            Chef chef;
            try {
                chef = (Chef) App.getUser();

                // Add meal to chef's list in firebase
                database.collection(MEALS_COLLECTION)   // top-level meals collection
                        .whereEqualTo(MEALS_COLLECTION_CHEF_KEY, chef.getUserId()) // get meals document of the chef
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    // if chef currently doesn't have a meal
                                    if (task.getResult() == null || task.getResult().isEmpty()) {
                                        Log.e("addMeal", "Chef not in meals collection, adding to it");
                                        // add the chef first
                                        Map<String, Object> mealsCollectionData = new HashMap<>();
                                        mealsCollectionData.put("chef", chef.getUserId());
                                        database.collection(MEALS_COLLECTION)
                                                .add(mealsCollectionData)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                        addChefMeal(documentReference.getId(), meal, chef.getFirstName() + " " + chef.getLastName(), chef.getAddress().toString());
                                                    }
                                                })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            App.MEAL_HANDLER.handleActionFailure(ADD_MEAL, "Failed to add meal to chef in database: " + e.getMessage());
                                                        }
                                                    });
                                    } else {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            addChefMeal(document.getId(), meal, chef.getFirstName() + " " + chef.getLastName(), chef.getAddress().toString());
                                        }
                                    }
                                } else {
                                    App.MEAL_HANDLER.handleActionFailure(ADD_MEAL, "Failed to add meal to chef in database");
                                    Log.d("addMeal", "Error getting chef's meals: ", task.getException());
                                }
                            }
                        });

            } catch (Exception e) {
                App.MEAL_HANDLER.handleActionFailure(ADD_MEAL, "Failed to add meal to chef in database: " + e.getMessage());
            }
        }
    }

    /**
     * Remove meal from searchable list of meals in Firebase
     * @param mealId The mealId of meal to be removed
     */
    public void removeMeal(String mealId){

        Chef chef;

        try {
            if (Preconditions.isNotNull(mealId)) {

                chef = (Chef) App.getUser();

                database.collection(MEALS_COLLECTION)   // top-level meals collection
                        .whereEqualTo(MEALS_COLLECTION_CHEF_KEY, chef.getUserId()) // get meals document of the chef
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        // Remove meal from chef's list in firebase
                                        database.collection(MEALS_COLLECTION)
                                                .document(document.getId())
                                                .collection(CHEF_MEALS_COLLECTION)
                                                .document(mealId)
                                                .delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        App.MEAL_HANDLER.handleActionSuccess(REMOVE_MEAL, mealId);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        App.MEAL_HANDLER.handleActionFailure(REMOVE_MEAL, "Failed to remove meal in chef's list in Firebase: " + e.getMessage());
                                                    }
                                                });
                                    }
                                } else {
                                    App.MEAL_HANDLER.handleActionFailure(ADD_MEAL, "Unable to get chef's meals: " + task.getException());
                                }
                            }
                        });

            } else {
                // if Preconditions fail
                App.MEAL_HANDLER.handleActionFailure(REMOVE_MEAL, "Invalid object value for meal");
            }
        } catch (Exception e) {
            App.MEAL_HANDLER.handleActionFailure(REMOVE_MEAL, "Unable to get Chef instance: " + e.getMessage());
        }
    }

    /**
     * Set isOffered property to true to a meal in a specific chef's list of meals in Firebase
     * @param mealId The mealId of meal to be updated
     */
    public void addMealToOfferedList(String mealId){

        if (Preconditions.isNotNull(mealId)) {
            try {
                // ensure a chef is logged in & get the chef instance
                Chef chef = (Chef) App.getUser();
                // Set isOffered to true in chef's meals in firebase
                database.collection(MEALS_COLLECTION)   // top-level meals collection
                        .whereEqualTo(MEALS_COLLECTION_CHEF_KEY, chef.getUserId()) // get meals document of the chef
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        database.collection(MEALS_COLLECTION)
                                                .document(document.getId())
                                                .collection(CHEF_MEALS_COLLECTION)
                                                .document(mealId)
                                                .update("isOffered", true)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        App.MEAL_HANDLER.handleActionSuccess(ADD_MEAL_TO_OFFERED_LIST, mealId);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        App.MEAL_HANDLER.handleActionFailure(ADD_MEAL_TO_OFFERED_LIST, "Failed to add meal to offered list in chef in database: " + e.getMessage());
                                                    }
                                                });
                                    }
                                } else {
                                    App.MEAL_HANDLER.handleActionFailure(ADD_MEAL_TO_OFFERED_LIST, "Failed to retrieve chef's meals");
                                }
                            }
                        });

            } catch (Exception e) {
                App.MEAL_HANDLER.handleActionFailure(ADD_MEAL_TO_OFFERED_LIST, "Failed to add meal to offered list in chef in database: " + e.getMessage());
            }
        } else {
            // if Preconditions fail
            App.MEAL_HANDLER.handleActionFailure(ADD_MEAL_TO_OFFERED_LIST, "Invalid object value for mealId");
        }
    }


    /**
     * Set isOffered property to false to a meal in a specific chef's list of meals in Firebase
     * @param mealId The mealId of meal to be updated
     */
    public void removeMealFromOfferedList(String mealId){

        if (Preconditions.isNotNull(mealId)) {
            try {
                // ensure a chef is logged in & get the chef instance
                Chef chef = (Chef) App.getUser();
                // Set isOffered to true in chef's meals in firebase
                database.collection(MEALS_COLLECTION)   // top-level meals collection
                        .whereEqualTo(MEALS_COLLECTION_CHEF_KEY, chef.getUserId()) // get meals document of the chef
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        database.collection(MEALS_COLLECTION)
                                                .document(document.getId())
                                                .collection(CHEF_MEALS_COLLECTION)
                                                .document(mealId)
                                                .update("isOffered", false)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        App.MEAL_HANDLER.handleActionSuccess(REMOVE_MEAL_FROM_OFFERED_LIST, mealId);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        App.MEAL_HANDLER.handleActionFailure(REMOVE_MEAL_FROM_OFFERED_LIST, "Failed to remove meal from offered list in database: " + e.getMessage());
                                                    }
                                                });
                                    }
                                } else {
                                    App.MEAL_HANDLER.handleActionFailure(REMOVE_MEAL_FROM_OFFERED_LIST, "Error getting chef's meals");
                                }
                            }
                        });

            } catch (Exception e) {
                App.MEAL_HANDLER.handleActionFailure(REMOVE_MEAL_FROM_OFFERED_LIST, "Unable to retrieve a Chef: " + e.getMessage());
            }
        } else {
            // if Preconditions fail
            App.MEAL_HANDLER.handleActionFailure(REMOVE_MEAL_FROM_OFFERED_LIST, "Invalid object value for mealID");
        }
    }

    /**
     * Get meal from Firebase given the mealId AND chefId
     * @param mealId The mealId of meal
     */
    public void getMealById (String mealId, String chefId) {

        database.collection(MEALS_COLLECTION + "/" + chefId + "/" + CHEF_MEALS_COLLECTION)
                .document(mealId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists() && document.getData() != null) {
                                try  {
                                    Meal meal = makeMealFromFirebase(document);
                                    // set the meal id
                                    meal.setMealID(document.getId());
                                    App.MEAL_HANDLER.handleActionSuccess(GET_MEAL_BY_ID, meal);
                                } catch (Exception e) {
                                    App.MEAL_HANDLER.handleActionFailure(GET_MEAL_BY_ID, "Error making a meal from data retrieved");
                                }
                            } else {
                                App.MEAL_HANDLER.handleActionFailure(GET_MEAL_BY_ID, "Error getting the meal for given id");
                            }
                        } else {
                            App.MEAL_HANDLER.handleActionFailure(GET_MEAL_BY_ID, "Error getting chef's meals");
                        }
                    }
                });
    }

    /**
     * Set meals list to specific chef locally using App instance user
     */
    public void getMeals(){
        try {
            Chef chef = (Chef) App.getUser();
            database.collection(MEALS_COLLECTION)   // top-level meals collection
                    .whereEqualTo(MEALS_COLLECTION_CHEF_KEY, chef.getUserId()) // get meals document of the chef
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {

                                    database.collection(MEALS_COLLECTION)
                                            .document(document.getId())
                                            .collection(CHEF_MEALS_COLLECTION)
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        Map<String, Meal> meals = new HashMap<String, Meal>();
                                                        Meal meal;
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            meal = makeMealFromFirebase(document);
                                                            // set the meal id
                                                            meal.setMealID(document.getId());
                                                            meals.put(document.getId(), meal);
                                                        }
                                                        App.MEAL_HANDLER.handleActionSuccess(GET_MENU, meals);
                                                    } else {
                                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                                        App.MEAL_HANDLER.handleActionFailure(GET_MENU, "Failed to retrieve meals from firebase");
                                                    }
                                                }
                                            });
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            App.MEAL_HANDLER.handleActionFailure(GET_MENU, "Failed to get menu: " + e.getMessage());
        }
    }

    /**
     * Set meals list to specific chef locally using chefID
     */
    public void loadChefMeals(LoginScreen loginScreen){
        try {
            Chef chef = (Chef) App.getUser();
            database.collection(MEALS_COLLECTION)   // top-level meals collection
                    .whereEqualTo(MEALS_COLLECTION_CHEF_KEY, chef.getUserId()) // get meals document of the chef
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                // check if chef has any meals currently
                                if (task.getResult() == null || task.getResult().isEmpty()) {
                                    Log.e("loadMeals", "Chef has no meals currently");
                                    loginScreen.showNextScreen();
                                } else {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //Log.e("loadMeals", "creating meals");
                                        database.collection(MEALS_COLLECTION)
                                                .document(document.getId())
                                                .collection(CHEF_MEALS_COLLECTION)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            Map<String, Meal> meals = new HashMap<String, Meal>();
                                                            Meal meal;
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                meal = makeMealFromFirebase(document);
                                                                // set the meal id
                                                                meal.setMealID(document.getId());
                                                                meals.put(document.getId(), meal);
                                                            }
                                                            // add meals to Chef
                                                            ((Chef) App.getUser()).MEALS.setMeals(meals);
                                                            // let login screen show Chef screen
                                                            loginScreen.showNextScreen();
                                                        } else {
                                                            Log.d("loadMeals", "Error getting documents: ", task.getException());
                                                            loginScreen.dbOperationFailureHandler(UserHandler.dbOperations.USER_LOG_IN, "Failed to retrieve meals from firebase");
                                                        }
                                                    }
                                                });
                                    }
                                }
                            } else {
                                loginScreen.dbOperationFailureHandler(UserHandler.dbOperations.USER_LOG_IN, "Failed to load meals");
                                Log.e("loadMeals", "failed to load meals: " + task.getException());
                            }
                        }
                    });
        } catch (Exception e) {
            loginScreen.dbOperationFailureHandler(UserHandler.dbOperations.USER_LOG_IN, "Failed to get Chef's meals");
            Log.e("loadMeals", "failed to load meals: " + e.getMessage());
        }
    }

    /**
     * get all meals of all chefs
     */
    public void getAllMeals() {

        Log.e("searchMeals", "Initiating request to get all meals");

        // get all documents from meals collection
        database.collection(MEALS_COLLECTION)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    // for each Meals row (which contains a chef id and a nested meals collection
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // make the next call to get Chef data
                                        getChefForSearchMeals(document.getId(), (String) document.getData().get("chef"));
                                    }
                                } else {
                                    App.MEAL_HANDLER.handleActionFailure(ADD_MEALS_TO_SEARCH_LIST, "Failed to retrieve chef from Meals: " + task.getException());
                                }
                            }
                        });
    }

    private void getChefForSearchMeals(String mealDocumentId, String chefId) {

        database.collection(CHEF_COLLECTION)
                .document(chefId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                if (document.getData() != null){
                                    // if chef is suspended, we don't need to get the meals of this chef
                                    // get value of isSuspended for check
                                    boolean isSuspended = (Boolean) document.getData().get("isSuspended");
                                    // we load the meals only if chef is not suspended
                                    if (!isSuspended) {
                                        // create ChefInfo for the chef
                                        Result<ChefInfo, String> result = getChefInfoInstance(document);
                                        if (result.isSuccess()) {
                                            // call next method to retrieve meals of the chef
                                            getChefMeals(mealDocumentId, result.getSuccessObject());
                                        } else {
                                            App.MEAL_HANDLER.handleActionFailure(ADD_MEALS_TO_SEARCH_LIST, result.getErrorObject());
                                        }
                                    }
                                }

                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private Result<ChefInfo, String> getChefInfoInstance(DocumentSnapshot document) {

        try {
            String chefName =
                    String.valueOf(document.getData().get("firstName")) +
                            " "  + String.valueOf(document.getData().get("lastName"));

            // TODO: implement chef rating
            // int chefRating = (Integer) document.getData().get("rating");

            AddressEntityModel newAddress = new AddressEntityModel();

            newAddress.setStreetAddress(String.valueOf(document.getData().get("addressStreet")));
            newAddress.setCity(String.valueOf(document.getData().get("addressCity")));
            newAddress.setCountry(String.valueOf(document.getData().get("country")));
            newAddress.setPostalCode(String.valueOf(document.getData().get("postalCode")));

            return new Result<ChefInfo, String>(new ChefInfo(document.getId(), chefName, 4, new Address(newAddress)), null);
        } catch (Exception e) {
            return new Result<ChefInfo, String>(null, "Failed to create ChefInfo: " + e.getMessage());
        }
    }

    private void getChefMeals(String mealDocumentId, ChefInfo chefInfo) {

        Log.e("searchMeals", "Initiating request to get meals of chef: " + chefInfo.getChefName());
        Log.e("searchMeals", "path: " + MEALS_COLLECTION + "/" + mealDocumentId + "/" + CHEF_MEALS_COLLECTION);

        database.collection(MEALS_COLLECTION + "/" + mealDocumentId + "/" + CHEF_MEALS_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<SearchMealItem> smItems = new ArrayList<>();
                            SearchMealItem smItem;
                            Meal meal;
                            if (task.getResult() == null || task.getResult().isEmpty()) {
                                Log.e("searchMeals", "empty result");
                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // create the meal
                                meal = makeMealFromFirebase(document);
                                // set the meal id
                                meal.setMealID(document.getId());
                                // add keywords to meal instance (only need to do this when we need search meal functionality i.e., for a client)
                                meal.setKeywords((ArrayList<String>) document.getData().get("keywords"));
                                // create SearchMealItem adding to it the meal and chefInfo
                                smItem = new SearchMealItem(meal, chefInfo);
                                Log.e("searchMeals", "adding meal: " + meal.getName());
                                // store current SearchMealItem in our list
                                smItems.add(smItem);
                            }
                            Log.e("searchMeals", "updating meals");
                            // pass list containing SearchMealItems to handler so our App's search meal list can be updated
                            App.MEAL_HANDLER.handleActionSuccess(ADD_MEALS_TO_SEARCH_LIST, smItems);
                        } else {
                            Log.d("searchMeals", "Error getting documents: ", task.getException());
                            App.MEAL_HANDLER.handleActionFailure(ADD_MEALS_TO_SEARCH_LIST, "Failed to retrieve meals from firebase");
                        }
                    }
                });
    }

    protected Meal makeMealFromFirebase(DocumentSnapshot document) {

        if (document.getData() == null) {
            throw new NullPointerException("makeClientFromFirebase: invalid document object");
        }

        MealEntityModel newMeal = new MealEntityModel();

        newMeal.setMealID(document.getId());
        newMeal.setName(String.valueOf(document.getData().get("name")));
        newMeal.setChefID(String.valueOf(document.getData().get("chefId")));
        newMeal.setCuisineType(String.valueOf(document.getData().get("cuisineType")));
        newMeal.setMealType(String.valueOf(document.getData().get("mealType")));
        newMeal.setIngredients(String.valueOf(document.getData().get("ingredients")));
        newMeal.setAllergens((ArrayList<String>) (document.getData().get("allergens")));
        newMeal.setDescription(String.valueOf(document.getData().get("description")));
        newMeal.setOffered((Boolean) document.getData().get("isOffered"));
        newMeal.setPrice((Double) document.getData().get("price"));

        return new Meal(newMeal);

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
