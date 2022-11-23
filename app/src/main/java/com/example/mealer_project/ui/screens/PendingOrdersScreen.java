package com.example.mealer_project.ui.screens;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.ui.core.UIScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PendingOrdersScreen extends UIScreen {

    private Map<String, Order> ordersData;
    private List<Order> orders;

    private PendingOrdersAdapter pendingOrdersAdapter;

    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders_screen);

        // check the meals data to be loaded
        loadPendingOrdersData();

        // Initialization
        ordersData = new HashMap<String, Order>();
        backButton = (ImageButton) findViewById(R.id.backButton);

        // Process: setting onClick method for back button
        backButton.setOnClickListener(v -> finish());

        // populate orders listView
        populatePendingOrdersList();

        //App.getAppInstance().setPendingOrdersScreen(this);

    }

    // Go back to previous screen
    public void clickBack(View view) {
        finish();
    }

    private void loadPendingOrdersData() {

        // check if the current logged in user is a Chef
        if (App.getUser() instanceof Chef) {
            // set meals data to be the list of meals of the Chef
            this.ordersData = ((Chef) App.getUser()).getOrders();
        }
        else {
            Log.e("PendingOrdersScreen", "Can't show pending offered; Current logged-in user is not a CHEF");
            displayErrorToast("No pending orders available to be displayed!");
        }

    }

    private void populatePendingOrdersList() {

        // Variable Declaration
        this.orders = new ArrayList<Order>();

        // get the meals list
        ListView pendingOrdersList = findViewById(R.id.pendingListView);
        // get the adapter
        pendingOrdersAdapter = new PendingOrdersAdapter(this, R.layout.activity_pending_orders_list_item, this.orders);

        // Attach the adapter to the pending orders listView
        pendingOrdersList.setAdapter(pendingOrdersAdapter);

        // add data to the adapter
        for (Order order: this.ordersData.values()) {
            pendingOrdersAdapter.add(order);
        }
    }
}