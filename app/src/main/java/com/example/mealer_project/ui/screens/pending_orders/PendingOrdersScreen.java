package com.example.mealer_project.ui.screens.pending_orders;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.handlers.OrderHandler;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;

import java.util.ArrayList;
import java.util.List;

public class PendingOrdersScreen extends UIScreen implements StatefulView {

    // Variable Declaration
    /**
     * the map that contains the current CHEF's Orders
     */
    private List<Order> ordersData;

    /**
     * the list that will hold the orders
     */
    private List<Order> orders;

    /**
     * the array adapter for the list view of the pending orders
     */
    private PendingOrdersAdapter pendingOrdersAdapter;

    /**
     * the back button icon
     */
    ImageButton backButton;

    //----------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders_screen);
        App.getAppInstance().setPendingOrdersScreen(this);

        // Initialization
        ordersData = new ArrayList<Order>();
        backButton = findViewById(R.id.backButton);

        // Process: loading the Orders data
        loadPendingOrdersData();

        // Process: populating the Orders ListView
        populatePendingOrdersList();

        // Process: setting onClick method for back button
        backButton.setOnClickListener(v -> finish());

    }

    /**
     * this helper method retrieves the current CHEF's Orders
     */
    private void loadPendingOrdersData() {

        // Process: checking if current user is a CHEF
        if (App.getUser() instanceof Chef) { //is CHEF
            // Initialization: setting ordersData to the list
            this.ordersData = ((Chef) App.getUser()).ORDERS.getPendingOrders();
        }
        else { //not a chef -> error-handling
            Log.e("PendingOrdersScreen", "Can't show pending orders; Current logged-in user is not a CHEF");

            // Output
            displayErrorToast("No pending orders available to be displayed!");
        }

    }

    /**
     * this helper method populates the Orders list
     */
    private void populatePendingOrdersList() {

        // Variable Declaration
        this.orders = new ArrayList<Order>();
        ListView pendingOrdersList = findViewById(R.id.pendingListView);

        // Initialization: setting the adapter
        pendingOrdersAdapter = new PendingOrdersAdapter(this, R.layout.activity_pending_orders_list_item, this.orders);

        // Process: attaching the adapter to the ListView
        pendingOrdersList.setAdapter(pendingOrdersAdapter);

        // Process: looping through the map of data
        for (Order order: this.ordersData) {
            pendingOrdersAdapter.add(order); //adding the orderData to the list
        }

    }

    /**
     * this helper method populates the Orders list after a change has been made
     */
    private void repopulatePendingOrdersList() {

        pendingOrdersAdapter.clear(); //removing all previous data

        // Process: looping through the map of data
        for (Order order: this.ordersData) {
            pendingOrdersAdapter.add(order); //adding the orderData to the list
        }

    }

    @Override
    public void updateUI() {

    }

    @Override
    public void showNextScreen() {

    }

    @Override
    public void dbOperationSuccessHandler(Object dbOperation, Object payload) {

        if (dbOperation == OrderHandler.dbOperations.ADD_ORDER) {

            // Output: successfully add new order
            displayErrorToast("Successfully added order!");

        }
        else if (dbOperation == OrderHandler.dbOperations.REMOVE_ORDER) {

            // Output: successfully removed order
            displayErrorToast("Successfully removed order!");

        }
        else if (dbOperation == OrderHandler.dbOperations.UPDATE_ORDER) {

            // Output: successfully updated order
            displayErrorToast("Successfully updated order!");

        }
        else if (dbOperation == OrderHandler.dbOperations.GET_ORDER_BY_ID) {

            // Output: successfully retrieved order
            displayErrorToast("Successfully retrieved order!");

        }
        else { //other op

            // Output
            displayErrorToast((String) payload);

        }

        loadPendingOrdersData();
        repopulatePendingOrdersList();

        // Process: telling adapter that orders have been updated
        pendingOrdersAdapter.notifyDataSetChanged();

        App.getAppInstance().getOrdersInProgressScreen().updateAdapter();

    }

    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {

        if (dbOperation == OrderHandler.dbOperations.ADD_ORDER) {

            // Output: failed to add new order
            displayErrorToast("Failed to add order!");

        }
        else if (dbOperation == OrderHandler.dbOperations.REMOVE_ORDER) {

            // Output: failed to remove order
            displayErrorToast("Failed to remove order!");

        }
        else if (dbOperation == OrderHandler.dbOperations.UPDATE_ORDER) {

            // Output: failed to update order
            displayErrorToast("Failed to update order!");

        }
        else if (dbOperation == OrderHandler.dbOperations.GET_ORDER_BY_ID) {

            // Output: failed to get order
            displayErrorToast("Failed to get order!");

        }
        else { //other error

            // Output
            displayErrorToast((String) payload);

        }

    }
}