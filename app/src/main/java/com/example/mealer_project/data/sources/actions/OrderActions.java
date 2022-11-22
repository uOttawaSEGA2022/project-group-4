package com.example.mealer_project.data.sources.actions;

import static com.example.mealer_project.data.sources.FirebaseCollections.CHEF_COLLECTION;
import static com.example.mealer_project.data.sources.FirebaseCollections.CHEF_MEALS_COLLECTION;
import static com.example.mealer_project.data.sources.FirebaseCollections.CHEF_ORDERS_COLLECTION;
import static com.example.mealer_project.data.sources.FirebaseCollections.CLIENT_COLLECTION;
import static com.example.mealer_project.data.sources.FirebaseCollections.CLIENT_ORDERS_COLLECTION;
import static com.example.mealer_project.data.sources.FirebaseCollections.MEALS_COLLECTION;
import static com.example.mealer_project.data.sources.FirebaseCollections.MEALS_COLLECTION_CHEF_KEY;
import static com.example.mealer_project.data.sources.FirebaseCollections.ORDER_COLLECTION;
import static com.example.mealer_project.data.handlers.OrderHandler.dbOperations.*;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.data.models.orders.ChefInfo;
import com.example.mealer_project.data.models.orders.ClientInfo;
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

import java.util.Date;
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

            databaseOrder.put("clientInfo", order.getClientInfo());
            databaseOrder.put("chefInfo", order.getChefInfo());
            databaseOrder.put("date", order.getOrderDate());
            databaseOrder.put("isPending", order.getIsPending());
            databaseOrder.put("isRejected", order.getIsRejected());
            databaseOrder.put("isCompleted", order.getIsCompleted());

            Map <String, Integer> mealsQuantity = new HashMap<>();

            for (Map.Entry<Meal, Integer> entry : order.getMealsQuantity().entrySet()) {
                Meal key = entry.getKey();
                Integer value = entry.getValue();
                mealsQuantity.put(key.getMealID(), value);
            }

            databaseOrder.put("mealsQuantity", mealsQuantity);

            // Add order to Orders Collection
            database
                    .collection(ORDER_COLLECTION)
                    .add(databaseOrder)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            // if successful, update orderId
                            order.setOrderID(documentReference.getId());

                            // if successful, add orderId to specific chef's list of orders
                            database.collection(CHEF_COLLECTION)
                                            .document(order.getChefInfo().getChefId())
                                            .update(CHEF_ORDERS_COLLECTION, FieldValue.arrayUnion(order.getOrderID()))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
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
                                    .document(order.getClientInfo().getClientId())
                                    .update(CLIENT_ORDERS_COLLECTION, FieldValue.arrayUnion(order.getOrderID()))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
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
                                            .update(CHEF_ORDERS_COLLECTION, FieldValue.arrayRemove(orderId));

                            // delete orderId from specific client's list of orders
                            database.collection(CLIENT_COLLECTION)
                                    .document(clientId)
                                    .update(CLIENT_ORDERS_COLLECTION, FieldValue.arrayRemove(orderId));

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
                            App.ORDER_HANDLER.handleActionFailure(REMOVE_ORDER, "No such document");
                        }
                    } else {
                        App.ORDER_HANDLER.handleActionFailure(REMOVE_ORDER, "get failed with " + task.getException());
                    }
                }
            });
        }

    }


    public void getOrderById(String orderId){

        if (Preconditions.isNotNull(orderId)) {

            // retrieve order object from firebase
            database
                    .collection(ORDER_COLLECTION)
                    .document(orderId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {

                                    // create a new Order
                                    Order order = new Order();
                                    order.setOrderID(document.getId());
                                    order.setClientInfo((ClientInfo) document.getData().get("clientInfo"));
                                    order.setChefInfo((ChefInfo) document.getData().get("chefInfo"));
                                    order.setIsRejected((Boolean) document.getData().get("isRejected"));
                                    order.setIsPending((Boolean) document.getData().get("isPending"));
                                    order.setIsCompleted((Boolean) document.getData().get("isCompleted"));
                                    // have to handle date parsing
                                    try {
                                        order.setDate((Date) document.getData().get("date"));
                                    } catch (Exception e) {
                                        // TODO: handle date parsing failure
                                        order.setDate(new Date());
                                    }

                                    Map<String, Integer> mealsQuantity = (Map<String, Integer>) document.getData().get("mealsQuantity");
                                    for (Map.Entry<String, Integer> entry : mealsQuantity.entrySet()) {
                                        String key = entry.getKey();
                                        Integer value = entry.getValue();

                                        database.collection(MEALS_COLLECTION)
                                                .document(order.getChefInfo().getChefId())
                                                .collection(CHEF_MEALS_COLLECTION)
                                                .document(key)
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            if (document.exists() && document.getData() != null) {
                                                                try  {
                                                                    Meal meal = App.getPrimaryDatabase().MEALS.makeMealFromFirebase(document);
                                                                    // add the meal to order
                                                                    order.addMealQuantity(meal, value);

                                                                } catch (Exception e) {
                                                                    App.ORDER_HANDLER.handleActionFailure(GET_ORDER_BY_ID, "Error making a meal from data retrieved");
                                                                }
                                                            } else {
                                                                App.ORDER_HANDLER.handleActionFailure(GET_ORDER_BY_ID, "Error getting the meal for given id");
                                                            }
                                                        } else {
                                                            App.ORDER_HANDLER.handleActionFailure(GET_ORDER_BY_ID, "Error getting chef's meals");
                                                        }
                                                    }
                                                });

                                    }
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

    public void getOrderMeals(Order order) {



        // get id of any invalid meal present in the order instance
        String mealId = order.getIdOfInvalidMeal();

        // meal id is only null when we have all meals in the order object
        if (mealId == null) {
            // return the order instance to handler now
            App.ORDER_HANDLER.handleActionSuccess(GET_ORDER_BY_ID, order);
        }
        // we still have to get data for a meal
        else {
            database.collection(MEALS_COLLECTION)   // top-level meals collection
                    .whereEqualTo(MEALS_COLLECTION_CHEF_KEY, order.getChefInfo().getChefId()) // get meals document of the chef
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
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        DocumentSnapshot document = task.getResult();
                                                        if (document.exists() && document.getData() != null) {
                                                            try  {
                                                                Meal meal = App.getPrimaryDatabase().MEALS.makeMealFromFirebase(document);
                                                                // set the meal id
                                                                meal.setMealID(document.getId());
                                                                // add the meal to order
                                                                order.addMeal(meal.getMealID(), meal);
                                                                // make a recursive call to getOrderMeals to check if more meals need to be loaded
                                                                getOrderMeals(order);
                                                            } catch (Exception e) {
                                                                App.ORDER_HANDLER.handleActionFailure(GET_ORDER_BY_ID, "Error making a meal from data retrieved");
                                                            }
                                                        } else {
                                                            App.ORDER_HANDLER.handleActionFailure(GET_ORDER_BY_ID, "Error getting the meal for given id");
                                                        }
                                                    } else {
                                                        App.ORDER_HANDLER.handleActionFailure(GET_ORDER_BY_ID, "Error getting chef's meals");
                                                    }
                                                }
                                            });
                                }
                            } else {
                                App.ORDER_HANDLER.handleActionFailure(GET_ORDER_BY_ID, "Error getting chef's meals");
                            }
                        }
                    });
        }
    }

    public void updateOrder(Order order){


    }

}
