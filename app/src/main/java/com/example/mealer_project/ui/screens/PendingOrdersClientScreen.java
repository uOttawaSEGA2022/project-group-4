package com.example.mealer_project.ui.screens;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Client;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.ui.core.UIScreen;

import java.util.ArrayList;
import java.util.List;

public class PendingOrdersClientScreen extends UIScreen {

    // Variable Declaration
    /**
     * the map that contains the current CLIENT's Orders
     */
    private List<Order> ordersData;

    /**
     * the list that will hold the orders
     */
    private List<Order> orders;

    /**
     * the array adapter for the list view of the pending orders
     */
    private PendingOrdersAdapterClient pendingOrdersAdapterClient;

    /**
     * the back button icon
     */
    ImageButton backButton;

    //----------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders_client_screen);

        // Initialization
        ordersData = new ArrayList<Order>();
        backButton = findViewById(R.id.backButton7);

        // Process: loading the Orders data
        loadPendingOrdersData();

        // Process: populating the Orders ListView
        populatePendingOrdersList();

        // Process: setting onClick method for back button
        backButton.setOnClickListener(v -> finish());

    }

    /**
     * this helper method retrieves the current CLIENT's Orders
     */
    private void loadPendingOrdersData() {

        // Process: checking if current user is a CLIENT
        if (App.getUser() instanceof Client) { //is CLIENT
            // Initialization: setting ordersData to the list
            this.ordersData = ((Client) App.getUser()).ORDERS.getPendingOrders();
        }
        else { //not a client -> error-handling
            Log.e("PendingOrders Client", "Can't show pending orders; Current logged-in user is not a CLIENT");

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
        ListView pendingOrdersList = findViewById(R.id.pendingClientsListView);

        // Initialization: setting the adapter
        pendingOrdersAdapterClient = new PendingOrdersAdapterClient(this, R.layout.activity_completed_orders_list_item, this.orders);

        // Process: attaching the adapter to the ListView
        pendingOrdersList.setAdapter(pendingOrdersAdapterClient);

        // Process: looping through the map of data
        for (Order order: this.ordersData) {
            pendingOrdersAdapterClient.add(order); //adding the orderData to the list
        }

    }
}