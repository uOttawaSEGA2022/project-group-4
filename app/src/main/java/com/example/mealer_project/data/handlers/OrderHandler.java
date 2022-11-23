package com.example.mealer_project.data.handlers;

import android.util.Log;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.entity_models.MealEntityModel;
import com.example.mealer_project.data.entity_models.OrderEntityModel;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Client;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.data.models.Orders;
import com.example.mealer_project.data.models.User;
import com.example.mealer_project.data.models.UserRoles;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.data.models.orders.MealInfo;
import com.example.mealer_project.data.models.orders.OrderItem;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.utils.Preconditions;
import com.example.mealer_project.utils.Utilities;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderHandler {

    public enum dbOperations {
        ADD_ORDER,
        REMOVE_ORDER,
        GET_ORDER_BY_ID,
        UPDATE_ORDER,
        ERROR
    };

    private StatefulView uiScreen;

    /**
     * Using the Dispatch-Action Pattern to handle actions dispatched to Meal Handler
     * @param operationType one of the specified DB operations handled by MealHandler
     * @param payload an input data for the handler's operation
     * @param uiScreen instance of the view which needs to know of the operation's success or failure
     */
    public void dispatch(OrderHandler.dbOperations operationType, Object payload, StatefulView uiScreen) {

        // guard-clause
        if (Preconditions.isNotNull(uiScreen)) {

            // set the ui screen, so it can be interacted with later on
            this.uiScreen = uiScreen;

            try {
                switch (operationType) {

                    case ADD_ORDER:
                        addNewOrder();
                        break;

                    case REMOVE_ORDER:
                        if (Preconditions.isNotNull(payload) && payload instanceof String) {
                            // Process: removing order from Firebase
                            App.getPrimaryDatabase().ORDERS.removeOrder((String) payload);
                        }
                        else {
                            handleActionFailure( operationType, "Invalid Order ID provided");
                        }
                        break;

                    case GET_ORDER_BY_ID:
                        if (Preconditions.isNotNull(payload) && payload instanceof String)
                            // Process: getting Order from Firebase, given the orderID
                            App.getPrimaryDatabase().ORDERS.getOrderById((String) payload);
                        break;

                    case UPDATE_ORDER:
                        if (Preconditions.isNotNull(payload) && payload instanceof Order) {
                            // Process: updating order in Firebase
                            App.getPrimaryDatabase().ORDERS.updateOrder((Order) payload);
                        }
                        else {
                            handleActionFailure( operationType, "Invalid Order object provided");
                        }
                        break;

                    default:
                        Log.e("OrderHandler dispatch", "Action not implemented yet");
                }
            }
            catch (Exception e) { //op. failed
                Log.e("OrderHandler Dispatch", "Exception: " + e.getMessage());
                uiScreen.dbOperationFailureHandler(OrderHandler.dbOperations.ERROR, "Dispatch failed: " + e.getMessage());
            }

        }
        else { //UI Screen doesn't exist
            Log.e("OrderHandler Dispatch", "Invalid instance provided for uiScreen");
        }

    }

    /**
     * Method which is called AFTER a successful database operation to make updates locally
     * @param operationType type of database operation which was successful
     * @param payload data for making changes locally
     */
    public void handleActionSuccess(OrderHandler.dbOperations operationType, Object payload) {
        // ensure we have a valid uiScreen to inform of success
        if(Preconditions.isNotNull(uiScreen)) {

            try {
                switch (operationType) {

                    case ADD_ORDER:
                        if (Preconditions.isNotNull(payload) && payload instanceof Order && App.getUser().getRole() != UserRoles.ADMIN) {

                            // Process: checking for chef or client
                            if (App.getUser().getRole() == UserRoles.CLIENT) { //client
                                // add order to client's list of orders
                                if (App.getClient() != null) {
                                    App.getClient().addOrder((Order) payload);
                                }

                                // LOCALLY: adding order id to array in client
                                // ((Client) App.getUser()).ORDER_IDS.add(((Order) payload).getOrderID());
                            }
                            else { //chef
                                // add order to chef's list of orders
                                if (App.getChef() != null) {
                                    App.getChef().addOrder((Order) payload);
                                }

                                // LOCALLY: adding order id to array in chef
                                ((Chef) App.getUser()).ORDER_IDS.add(((Order) payload).getOrderID());
                            }

                            // let UI know about success
                            uiScreen.dbOperationSuccessHandler(operationType, "Order added successfully!");

                        }
                        else { //the ADMIN is trying to add an order, or the Order is invalid
                            handleActionFailure(operationType, "Invalid order object provided, or ADMIN attempting to add order");
                        }
                        break;

                    case REMOVE_ORDER:
                        if (Preconditions.isNotNull(payload) && payload instanceof String && App.getUser().getRole() == UserRoles.CHEF) {
                            // LOCALLY: removing order id from array in chef
                            ((Chef) App.getUser()).ORDER_IDS.remove((String) payload);

                            // let UI know about success
                            uiScreen.dbOperationSuccessHandler(operationType, "Order removed successfully!");
                        }
                        else { //someone other than a CHEF is removing the order, OR the orderID is invalid
                            handleActionFailure(operationType, "Invalid order ID, or CLIENT/ADMIN attempting to remove order");
                        }
                        break;

                    /*case UPDATE_ORDER:
                        if (Preconditions.isNotNull(payload) && payload instanceof Order  && App.getUser().getRole() == UserRoles.CHEF) {
                            // LOCALLY: nothing should change

                            // let UI know about success
                            uiScreen.dbOperationSuccessHandler(operationType, "Updated order info successfully!");
                        } else {
                            handleActionFailure( operationType, "Invalid order instance provided");
                        }
                        break;*/

                    /*case GET_ORDER_BY_ID:
                        if (Preconditions.isNotNull(payload) && payload instanceof Map) {
                            //do smth here

                            uiScreen.dbOperationSuccessHandler(operationType, "");
                        } else {
                            handleActionFailure(operationType, "Invalid payload for getOrder");
                        }
                        break;*/

                }
            }
            catch (Exception e) { //error-handling
                Log.e("handleActionSuccess", "Success handler exception: " + e.getMessage());
                uiScreen.dbOperationFailureHandler(OrderHandler.dbOperations.ERROR, "Failed to process request");
            }

        }
        else { //UI Screen doesn't exist
            Log.e("handleActionSuccess", "No UI Screen initialized");
        }
    }

    /**
     * Method which is called AFTER a failure in a database operation to inform UI
     * @param operationType type of database operation which failed
     * @param message a descriptive error message for the developers and analyst (not for client or chef)
     */
    public void handleActionFailure(OrderHandler.dbOperations operationType, String message) {
        // ensure we have a valid uiScreen to inform of failure
        if(Preconditions.isNotNull(uiScreen)) {

            String tag = "handleActionFailure";
            String userMessage = "Failed to process request";

            switch (operationType) {

                case ADD_ORDER:
                    tag = "addOrder";
                    userMessage= "Failed to add order!";
                    break;

                case REMOVE_ORDER:
                    tag = "errorRemovingOrder";
                    userMessage = "Failed to remove order!";
                    break;

                case UPDATE_ORDER:
                    tag = "updateOrder";
                    userMessage = "Failed to update order info!";
                    break;

                case GET_ORDER_BY_ID:
                    tag = "errorGetOrder";
                    userMessage = "Failed to get order!";
                    break;

                default:
                    Log.e("handleActionSuccess", "Action not implemented yet");

            }

            // send error
            Log.e(tag, message);
            uiScreen.dbOperationFailureHandler(operationType, userMessage);

        }
        else { //UI Screen doesn't exist
            Log.e("handleActionFailure", "No UI Screen initialized");
        }
    }

    private void addNewOrder() {
        // ensure valid client
        if (App.getClient() != null) {
            // get the clients cart
            Map<OrderItem, Boolean> cart = App.getClient().getCart();
            // ensure cart isn't empty or uninitialized
            if (Preconditions.isNotNull(cart) && !cart.isEmpty()) {
                // create a new Order
                Order order = new Order();
                // set info of client making order
                order.setClient(App.getClient());
                // set today's date
                order.setDate(Utilities.getTodaysDate());
                // flag to set chef info
                boolean isChefInfoAdded = false;
                // iterate through cart and add all meals
                for (OrderItem orderItem : cart.keySet()) {
                    // if chef info not added, add it
                    if (!isChefInfoAdded) {
                        // set chef info
                        order.setChefInfo(orderItem.getSearchMealItem().getChef());
                        isChefInfoAdded = true;
                    }

                    // Add mealInfo and quantity to hashmap in order
                    order.addMealQuantity(orderItem.getSearchMealItem().getMeal(),orderItem.getQuantity());
                }
                // once order has all mealIds in it, we add the order remotely
                App.getPrimaryDatabase().ORDERS.addOrder(order);
            } else {
                handleActionFailure(dbOperations.ADD_ORDER, "Cart is empty or uninitialized!");
            }
        }
    }

}
