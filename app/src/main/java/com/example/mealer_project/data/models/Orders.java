package com.example.mealer_project.data.models;

import androidx.annotation.NonNull;

import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.utils.Preconditions;
import com.example.mealer_project.utils.Response;
import com.example.mealer_project.utils.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orders {
    private Map<String, Order> orders;

    public Orders(){
        this.orders = new HashMap<>();
    }

    public void setOrders(@NonNull Map<String, Order> ordersData) {
        this.orders = ordersData;
    }

    public Response addOrder(@NonNull Order newOrder) {
        // guard-clause
        // meal must have a valid id which will be used as a key
        if (Preconditions.isNotEmptyString(newOrder.getOrderID())) {
            // check if meal already exists
            if (this.orders.get(newOrder.getOrderID()) != null) {
                return new Response(false, "Meal with same ID already exists! Use updateMeal to update an existing meal");
            }
            // add the new meal
            this.orders.put(newOrder.getOrderID(), newOrder);
            // return success
            return new Response(true);
        } else {
            return new Response(false, "Meal does not have a valid ID");
        }
    }

    public Result<Order, String> getOrder(@NonNull String orderID) {
        // guard-clause
        if (Preconditions.isNotEmptyString(orderID)) {
            // check if meal exists
            if (this.orders.get(orderID) != null) {
                return new Result<>(this.orders.get(orderID), null);
            } else {
                return new Result<>(null, "Could not find any meal for the provided meal ID");
            }
        } else {
            return new Result<>(null, "Invalid meal ID provided");
        }
    }

    /**
     * Method to retrieve a list containing all Orders by the Chef
     * @return a List containing Order objects
     */
    public List<Order> getAllOrders() {
        return new ArrayList<Order>(this.orders.values());
    };

    /**
     * Method to retrieve a list containing all pending Orders by the Chef
     * @return a List containing Order objects
     */
    public List<Order> getPendingOrders() {

        // Variable Declaration
        ArrayList<Order> pendingList = new ArrayList<Order>();

        // Process: looping through orders
        for (Order order : this.orders.values()) {

            // Process: checking if pending
            if (order.getIsPending()) {

                pendingList.add(order); //adding to list

            }

        }

        // Output
        return pendingList;

    };

    /**
     * Method to retrieve a list containing all completed Orders by the Chef
     * @return a List containing Order objects
     */
    public List<Order> getCompletedOrders() {
        // Variable Declaration
        ArrayList<Order> completedList = new ArrayList<Order>();

        // Process: looping through orders
        for (Order order : this.orders.values()) {

            // Process: checking if completed
            if (order.getIsCompleted()) {

                completedList.add(order); //adding to list

            }

        }

        // Output
        return completedList;
    };

    public Response removeOrder(@NonNull String orderId) {
        // guard-clause
        if (Preconditions.isNotEmptyString(orderId)) {
            // check if meal exists
            if (this.orders.get(orderId) != null) {
                // remove the meal
                this.orders.remove(orderId);
                // return operation success
                return new Response(true);
            } else {
                return new Response(false, "Could not find any meal for the provided order ID");
            }
        } else {
            return new Response(false, "Invalid order ID provided");
        }
    }

    // update pending status and completed status from chef
    public void updateOrder(String orderId, boolean isPending, boolean isRejected, boolean isCompleted) {

        if (Preconditions.isNotNullString(order.getOrderID())){

            this.orders.get(order.getOrderID()).setIsPending(isPending);
            this.orders.get(order.getOrderID()).setIsRejected(isRejected);
            this.orders.get(order.getOrderID()).setIsCompleted(isCompleted);
        }
    }
}
