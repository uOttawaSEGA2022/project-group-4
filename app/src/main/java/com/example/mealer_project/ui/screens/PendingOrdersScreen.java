package com.example.mealer_project.ui.screens;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.ui.core.UIScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PendingOrdersScreen extends UIScreen {

    // Variable Declaration
    /**
     * the map that contains the current CHEF's Orders
     */
    private Map<String, Order> ordersData;

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

        // Initialization
        ordersData = new HashMap<String, Order>();
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
            // Initialization: setting ordersData to the map of orderIDs & Orders
            this.ordersData = ((Chef) App.getUser()).getOrders();
        }
        else { //not a chef -> error-handling
            Log.e("PendingOrdersScreen", "Can't show pending offered; Current logged-in user is not a CHEF");

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
        for (Order order: this.ordersData.values()) {
            pendingOrdersAdapter.add(order); //adding the orderData to the list
        }

    }
}