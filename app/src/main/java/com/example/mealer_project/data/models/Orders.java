package com.example.mealer_project.data.models;

import androidx.annotation.NonNull;

import com.example.mealer_project.utils.Preconditions;
import com.example.mealer_project.utils.Response;
import com.example.mealer_project.utils.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orders implements Comparator<Order> {
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
     * Method to retrieve a list containing all pending Orders by the Chef
     * @return a List containing Order objects
     */
    public List<Order> getPendingOrders() {

        // Variable Declaration
        ArrayList<Order> pendingList = new ArrayList<Order>();

        // Process: looping through orders
        for (Order order : this.orders.values()) {

            // Process: checking if order is pending to be accepted OR accepted but not completed (not completed and not rejected)
            if (order.getIsPending() || (!order.getIsPending() && !order.getIsCompleted() && !order.getIsRejected())) {

                pendingList.add(order); //adding to list

            }

        }

        // Process: sorting the list by date placed
        Collections.sort(pendingList, this);

        // Output
        return pendingList;

    };

    /**
     * Method to retrieve a list containing all Orders in progress by the Chef
     * @return a List containing Order objects
     */
    public List<Order> getOrdersInProgress() {
        // Variable Declaration
        ArrayList<Order> ordersInProgress = new ArrayList<Order>();

        // Process: looping through orders
        for (Order order : this.orders.values()) {

            // Process: checking if completed
            if (!order.getIsCompleted() && !order.getIsRejected() && !order.getIsPending()) {

                ordersInProgress.add(order); //adding to list
            }
        }

        // Process: sorting the list by date placed
        Collections.sort(ordersInProgress, this);

        // Output
        return ordersInProgress;
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

        // Process: sorting the list by date placed
        Collections.sort(completedList, this);

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
    public void updateOrder(Order order) {

        if (Preconditions.isNotNull(order)){

            Order order1 = this.orders.get(order.getOrderID());
            order1.setIsCompleted(order.getIsCompleted());
            order1.setIsRejected(order.getIsRejected());
            order1.setIsPending(order.getIsPending());

        }
    }

    /**
     * this method compares the orders by the dates they were placed
     * @param order1 the first order
     * @param order2 the order the first is being compared to
     * @return 0 if same date; -1 is order2 was placed first; 1 if order1 was placed first
     */
    @Override
    public int compare(Order order1, Order order2) {
        // Output
        return order2.getOrderDate().compareTo(order1.getOrderDate());
    }

}
