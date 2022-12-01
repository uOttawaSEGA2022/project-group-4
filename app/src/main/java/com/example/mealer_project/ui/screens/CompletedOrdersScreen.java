package com.example.mealer_project.ui.screens;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Client;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.ui.core.UIScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompletedOrdersScreen extends UIScreen {

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
     * the array adapter for the list view of the Chef completed orders
     */
    private CompletedOrdersAdapter completedOrdersAdapter;

    /**
     * the array adapter for the list view of the Client completed orders
     */
    private CompletedOrdersAdapterClient completedOrdersAdapterClient;

    /**
     * the back button icon
     */
    ImageButton backButton;

    //----------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_orders_screen);

        // Initialization
        ordersData = new ArrayList<Order>();
        backButton = findViewById(R.id.backButton);

        // Process: loading the Orders data
        loadCompletedOrdersData();

        // Process: populating the Orders ListView
        populateCompletedOrdersList();

        // Process: setting onClick method for back button
        backButton.setOnClickListener(v -> finish());

    }

    /**
     * this helper method retrieves the current CHEF's Orders
     */
    private void loadCompletedOrdersData() {

        // Process: checking if current user is a CHEF
        if (App.getUser() instanceof Chef ) { //is CHEF
            // Initialization: setting ordersData to the list of completed orders
            this.ordersData = ((Chef) App.getUser()).ORDERS.getCompletedOrders();
        }
        // Process: checking if current user is a CLIENT
        else if (App.getUser() instanceof Client) { //is CLIENT
            // Initialization: setting ordersData to the list of completed orders
            this.ordersData = ((Client) App.getUser()).ORDERS.getCompletedOrders();
        }
        else { //if not chef//client -> error-handling
            Log.e("CompletedOrdersScreen", "Can't show completed orders; Current logged-in user is not a CHEF or CLIENT");

            // Output
            displayErrorToast("No completed orders available to be displayed!");
        }

    }

    /**
     * this helper method populates the Orders list
     */
    private void populateCompletedOrdersList() {

        // Variable Declaration
        this.orders = new ArrayList<Order>();
        ListView completedOrdersList = findViewById(R.id.completedListView);

        // Process: checking for chef or client
        if (App.getUser() instanceof Client) { //client

            // Initialization: setting the adapter
            completedOrdersAdapterClient = new CompletedOrdersAdapterClient(this, R.layout.activity_completed_orders_client_list_item, this.orders);

            // Process: attaching the adapter to the ListView
            completedOrdersList.setAdapter(completedOrdersAdapterClient);

            // Process: looping through the map of data
            for (Order order: this.ordersData) {
                completedOrdersAdapterClient.add(order); //adding the orderData to the list
            }

        }
        else if (App.getUser() instanceof Chef) { //chef

            // Initialization: setting the adapter
            completedOrdersAdapter = new CompletedOrdersAdapter(this, R.layout.activity_completed_orders_list_item, this.orders);

            // Process: attaching the adapter to the ListView
            completedOrdersList.setAdapter(completedOrdersAdapter);

            // Process: looping through the map of data
            for (Order order: this.ordersData) {
                completedOrdersAdapter.add(order); //adding the orderData to the list
            }

        }

    }
}