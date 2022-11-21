package com.example.mealer_project.data.sources.actions;

import static com.example.mealer_project.data.sources.FirebaseCollections.CHEF_COLLECTION;
import static com.example.mealer_project.data.sources.FirebaseCollections.CLIENT_COLLECTION;
import static com.example.mealer_project.data.sources.FirebaseCollections.ORDER_COLLECTION;
import static com.example.mealer_project.data.handlers.OrderHandler.dbOperations.*;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.handlers.OrderHandler;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.utils.Preconditions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
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

            Map<String, Object> databaseOrder = new HashMap<>();

            databaseOrder.put("chefId", order.getChef().getUserId());
            databaseOrder.put("clientId", order.getClientID());

            Map<String, Integer> mealIds = new HashMap<>();

            for (Map.Entry<String, Object> entry : order.getOrderItems().entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                mealIds.put(key.getMeal().getMealID(), value);
                // ...
            }

            databaseOrder.put("mealIds", mealIds);
            databaseOrder.put("meals", order.getorderItems());
            databaseOrder.put("isPending", order.isPending());
            databaseOrder.put("isRejected", order.isRejected());
            databaseOrder.put("isCompleted", order.isCompleted());
            databaseOrder.put("date", order.getOrderDate());

            database.collection(ORDER_COLLECTION)
                    .add(databaseOrder)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            order.setOrderID(documentReference.getId());

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

                            database.collection(ORDER_COLLECTION)
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

            database.collection(ORDER_COLLECTION)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document1 : task.getResult()) {

                                    database.collection(ORDER_COLLECTION)
                                            .document(document1.getId())
                                            .collection("orders")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document2 : task.getResult()) {

                                                            if(document2.getId() == orderId){

                                                                database.collection(ORDER_COLLECTION)
                                                                        .document(document1.getId())
                                                                        .collection("orders")
                                                                        .document(document2.getId())
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
                                                                                App.ORDER_HANDLER.handleActionFailure(REMOVE_ORDER, "Failed to remove order from Firebase: " + e.getMessage());
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    } else {
                                                        Log.d("removeOrder", "Error getting orderId documents: ", task.getException());
                                                    }
                                                }
                                            });

                                }
                            } else {
                                Log.d("removeOrder", "Error getting chefId documents: ", task.getException());
                            }
                        }
                    });
        }
    }


    public void getOrderById(String orderId){

    }

    public void updateOrder(Order order){

    }
}
