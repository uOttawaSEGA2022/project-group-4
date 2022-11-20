package com.example.mealer_project.data.sources.actions;

import com.example.mealer_project.utils.Preconditions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class OrderActions {

    FirebaseFirestore database;

    public OrderActions(FirebaseFirestore database) {
        this.database = database;
    }


    public void addOrder(Order order){

        if (Preconditions.isNotNull(order)) {

            Map<String, Object> databaseOrder = new HashMap<>();

            databaseOrder.put("orderID", order.getID());
            databaseOrder.put("chefID", order.getChefID());
            databaseOrder.put("clientID", order.getClientID());
            databaseOrder.put("listOfMeals", order.getListOfMeals());
            databaseOrder.put("pending", order.pending());
            databaseOrder.put("completed", order.getIsCompleted());
            databaseOrder.put("date", order.getDate());
    }
}
