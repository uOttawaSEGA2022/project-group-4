package com.example.mealer_project.data.handlers;

import android.util.Log;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.entity_models.MealEntityModel;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.utils.Preconditions;

import java.util.Map;

public class OrderHandler {

    public enum dbOperations {
        ADD_ORDER,
        REMOVE_ORDER,
        GET_ORDER,
        UPDATE_ORDER_INFO
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
                        if (Preconditions.isNotNull(payload) && payload instanceof MealEntityModel) {
                            // below code might cause exception if validation fails or instance can't be created
                            Order newOrder = new Order((OrderEntityModel) payload);
                            // if order creation was success, add the meal to database
                            App.getPrimaryDatabase().ORDERS.addOrder(newOrder);
                        } else {
                            handleActionFailure( operationType, "Invalid Order Object provided");
                        }
                        break;

                    case REMOVE_ORDER:
                        // remove order on remote database first
                        if (Preconditions.isNotNull(payload) && payload instanceof String) {
                            App.getPrimaryDatabase().ORDERS.removeOrder((String) payload);
                        } else {
                            handleActionFailure( operationType, "Invalid Order ID provided");
                        }
                        break;

                    case GET_ORDER:
                        App.getPrimaryDatabase().ORDERS.getOrders();
                        break;

//                    case UPDATE_ORDER_INFO:
//                        if (Preconditions.isNotNull(payload) && payload instanceof Meal) {
//                            App.getPrimaryDatabase().ORDERS.updateMealInfo((Meal) payload);
//                        } else {
//                            handleActionFailure( operationType, "Invalid Order instance provided");
//                        }
//                        break;

                    default:
                        Log.e("OrderHandler dispatch", "Action not implemented yet");
                }
            } catch (Exception e) {
                Log.e("OrderHandler Dispatch", "Exception: " + e.getMessage());
                uiScreen.dbOperationFailureHandler(MealHandler.dbOperations.ERROR, "Dispatch failed: " + e.getMessage());
            }

        } else {
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
                        if (Preconditions.isNotNull(payload) && payload instanceof Order) {
                            // add meal locally
                            ((Chef) App.getUser()).ORDERS.addOrder((Order) payload);
                            // let UI know about success
                            uiScreen.dbOperationSuccessHandler(operationType, "Order added successfully!");
                        } else {
                            handleActionFailure(operationType, "Invalid order object provided");
                        }
                        break;

                    case REMOVE_ORDER:
                        if (Preconditions.isNotNull(payload) && payload instanceof String) {
                            ((Chef) App.getUser()).ORDERS.removeOrder((String) payload);
                            // let UI know about success
                            uiScreen.dbOperationSuccessHandler(operationType, "Order removed successfully!");
                        } else {
                            handleActionFailure(operationType, "Invalid order ID");
                        }
                        break;

                    case UPDATE_ORDER_INFO:
                        if (Preconditions.isNotNull(payload) && payload instanceof Order) {
                            // update meal locally
                            ((Chef) App.getUser()).ORDERS.updateOrder((Order) payload);
                            uiScreen.dbOperationSuccessHandler(operationType, "updated order info!");
                        } else {
                            handleActionFailure( operationType, "Invalid order instance provided");
                        }
                        break;

                    case GET_ORDER:
                        // update the Chef's meals locally
                        if (Preconditions.isNotNull(payload) && payload instanceof Map) {
                            Orders<String, Order> orders = (Map<String, Order>) payload;
                            ((Chef) App.getUser()).MEALS.setMeals(orders);
                            uiScreen.dbOperationSuccessHandler(operationType, orders);
                        } else {
                            handleActionFailure(operationType, "Invalid payload for getOrder");
                        }
                        break;
                }
            } catch (Exception e) {
                Log.e("handleActionSuccess", "Success handler exception: " + e.getMessage());
                uiScreen.dbOperationFailureHandler(MealHandler.dbOperations.ERROR, "Failed to process request");
            }

        } else {
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

                case UPDATE_ORDER_INFO:
                    tag = "updateMealInfo";
                    userMessage = "Failed to update meal info!";
                    break;

                case GET_ORDER:
                    tag = "errorGetOrder";
                    userMessage = "Failed to get order!";
                    break;

                default:
                    Log.e("handleActionSuccess", "Action not implemented yet");
            }

            // send error
            Log.e(tag, message);
            uiScreen.dbOperationFailureHandler(operationType, userMessage);

        } else {
            Log.e("handleActionFailure", "No UI Screen initialized");
        }
    }

}
