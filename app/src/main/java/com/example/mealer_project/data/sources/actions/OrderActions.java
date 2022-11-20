package com.example.mealer_project.data.sources.actions;

import static com.example.mealer_project.data.handlers.MealHandler.dbOperations.ADD_MEAL;
import static com.example.mealer_project.data.sources.FirebaseCollections.CHEF_COLLECTION;
import static com.example.mealer_project.data.sources.FirebaseCollections.MEAL_COLLECTION;
import static com.example.mealer_project.data.sources.FirebaseCollections.ORDER_COLLECTION;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.utils.Preconditions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OrderActions {

    FirebaseFirestore database;


    public OrderActions(FirebaseFirestore database) {
        this.database = database;
    }


    /**
    * Adds order to firebase
    * @param order order object to be added
     */
    public void addOrder(Order order){

        if (Preconditions.isNotNull(order)) {

            Map<String, Object> databaseOrder = new HashMap<>();

            databaseOrder.put("orderID", order.getOrderID());
            databaseOrder.put("chefID", order.getChefID());
            databaseOrder.put("clientID", order.getClientID());
            databaseOrder.put("listOfMeals", order.get());
            databaseOrder.put("pending", order.getPendingStatus());
            databaseOrder.put("completed", order.getIsCompleted());
            databaseOrder.put("date", order.getOrderDate());

            database.collection(ORDER_COLLECTION)
                    .document(order.getChefID())
                    .set(databaseOrder)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // update meal id
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
}
