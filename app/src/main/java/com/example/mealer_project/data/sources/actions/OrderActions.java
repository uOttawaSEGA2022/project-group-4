package com.example.mealer_project.data.sources.actions;

import static com.example.mealer_project.data.handlers.MealHandler.dbOperations.ADD_MEAL;
import static com.example.mealer_project.data.handlers.MealHandler.dbOperations.GET_MEAL_BY_ID;
import static com.example.mealer_project.data.sources.FirebaseCollections.CHEF_COLLECTION;
import static com.example.mealer_project.data.sources.FirebaseCollections.CHEF_MEALS_COLLECTION;
import static com.example.mealer_project.data.sources.FirebaseCollections.CLIENT_COLLECTION;
import static com.example.mealer_project.data.sources.FirebaseCollections.MEALS_COLLECTION;
import static com.example.mealer_project.data.sources.FirebaseCollections.MEALS_COLLECTION_CHEF_KEY;
import static com.example.mealer_project.data.sources.FirebaseCollections.ORDER_COLLECTION;
import static com.example.mealer_project.data.handlers.OrderHandler.dbOperations.*;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.handlers.OrderHandler;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.data.models.orders.OrderItem;
import com.example.mealer_project.data.models.orders.SearchMealItem;
import com.example.mealer_project.utils.Preconditions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class OrderActions {

    FirebaseFirestore database;


    public OrderActions(FirebaseFirestore database) {
        this.database = database;
    }


    /**
     * Adds order to firebase
     *
     * @param order order object to be added
     */
    public void addOrder(Order order) {

        if (Preconditions.isNotNull(order)) {

            // create order hashmap to be added to firebase Orders Collection
            Map<String, Object> databaseOrder = new HashMap<>();

            databaseOrder.put("chefId", order.getChef().getUserId());
            databaseOrder.put("clientId", order.getClientID());

            Map<String, Integer> mealIds = new HashMap<>();

            for (OrderItem key: order.getOrderItems().keySet()) {

                mealIds.put(key.getSearchMealItem().getMeal().getMealID(), key.getQuantity());
            }

            databaseOrder.put("mealIds", mealIds);
            databaseOrder.put("meals", order.getOrderItems());
            databaseOrder.put("isPending", order.getIsPending());
            databaseOrder.put("isRejected", order.getIsRejected());
            databaseOrder.put("isCompleted", order.getIsCompleted());
            databaseOrder.put("date", order.getOrderDate());

            // Add order to Orders Collection
            database.collection(ORDER_COLLECTION)
                    .add(databaseOrder)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            // if successful, update orderId
                            order.setOrderID(documentReference.getId());

                            // if successful, add orderId to specific chef's list of orders
                            database.collection(CHEF_COLLECTION)
                                            .document(order.getChef().getUserId())
                                            .update("orders", FieldValue.arrayUnion(order.getOrderID()))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {@Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("ChefOrdersSuccess", "DocumentSnapshot successfully updated!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("ChefOrdersError", "Error updating document", e);
                                                }
                                            });

                            // if successful, add orderId to specific client's list of orders
                            database.collection(CLIENT_COLLECTION)
                                    .document(order.getClientID())
                                    .update("orders", FieldValue.arrayUnion(order.getOrderID()))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {@Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("ClientOrdersSuccess", "DocumentSnapshot successfully updated!");
                                    }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w("ClientOrdersError", "Error updating document", e);
                                        }
                                    });

                            App.ORDER_HANDLER.handleActionSuccess(ADD_ORDER, order);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            App.ORDER_HANDLER.handleActionFailure(ADD_ORDER, "Failed to add order to database: " + e.getMessage());
                        }
                    });
        }
    }


    /**
     * Removes order from firebase
     *
     * @param orderId orderId of object to be removed
     */
    public void removeOrder(String orderId){

        if (Preconditions.isNotNull(orderId)) {

            // retrieve order object from firebase
            DocumentReference docRef = database.collection(ORDER_COLLECTION).document(orderId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            // if the order exists, get clientId and chefId
                            String clientId = (String) document.get("clientId");
                            String chefId = (String) document.get("chefId");

                            // delete orderId from specific chef's list of orders
                            database.collection(CHEF_COLLECTION)
                                            .document(chefId)
                                            .update("orders", FieldValue.arrayRemove(orderId));

                            // delete orderId from specific client's list of orders
                            database.collection(CLIENT_COLLECTION)
                                    .document(clientId)
                                    .update("orders", FieldValue.arrayRemove(orderId));

                            //finally, delete order from Orders Collection
                            docRef
                            .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            App.ORDER_HANDLER.handleActionSuccess(REMOVE_ORDER, orderId);

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            App.ORDER_HANDLER.handleActionFailure(REMOVE_ORDER, "Failed to remove order from database: " + e.getMessage());
                                        }
                                    });


                        } else {
                            Log.d("RemoveOrder", "No such document");
                        }
                    } else {
                        Log.d("RemoveOrder", "get failed with ", task.getException());
                    }
                }
            });
        }

    }


    public void getOrderById(String orderId){

        if (Preconditions.isNotNull(orderId)) {

            // retrieve order object from firebase
            DocumentReference docRef = database.collection(ORDER_COLLECTION).document(orderId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {


                        } else {
                            Log.d("RemoveOrder", "No such document");
                        }
                    } else {
                        Log.d("RemoveOrder", "get failed with ", task.getException());
                    }
                }
            });
        }

    }

    public void updateOrder(Order order){

    }
}
