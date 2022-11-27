package com.example.mealer_project.ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.data.models.User;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.example.mealer_project.ui.screens.meals.MealsListScreen;

import java.util.ArrayList;
import java.util.List;

public class ChefScreen extends UIScreen implements StatefulView {

    TextView editText;

    // list containing the current chef's orders in progress
    private List<Order> orderList;

    // list that holds the orders
    private List<Order> orders;

    // array adapter for the in progress orders
    private OrdersInProgressAdapter ordersInProgressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_screen);

        editText = (TextView) findViewById(R.id.welcome_message_chef);

        attachOnClickListeners();

        User currentUser = App.getAppInstance().getUser();

        // Change text to proper welcome message when opened
        if (App.getAppInstance().isUserAuthenticated()) {
            setWelcomeMessage("Welcome " + currentUser.getFirstName() + " " + currentUser.getLastName() + ", you're logged in as a CHEF!");
        }

        // Initialization
        orderList = new ArrayList<Order>();

        // Process: loading the in progress orders
        loadOrdersInProgress();

        // Process: populate the Orders ListView
        populateOrdersInProgress();

    }

    /**
     * populates the list with all in progress meals that will be displayed
     */
    private void populateOrdersInProgress() {

        // variable declaration
        this.orders = new ArrayList<Order>();
        ListView ordersInProgressList = findViewById(R.id.ordersInProgress);

        // Initialization: setting adapter
        ordersInProgressAdapter = new OrdersInProgressAdapter(this, R.layout.orders_in_progress, this.orders);

        // Process: attach adapter to ListView
        ordersInProgressList.setAdapter(ordersInProgressAdapter);

        // Process: loop through the map of data for the orders in progress
        for (Order order: this.orderList) {
            ordersInProgressAdapter.add(order); // adds orderData to the list
        }
    }

    /**
     * retrieves the current chef's orders
     */
    private void loadOrdersInProgress() {

        // Process: Check if current user is a chef
        if (App.getUser() instanceof Chef) { // is a CHEF
            // Initialization: setting ordersData to the list
            this.orderList = ((Chef) App.getUser()).ORDERS.getAllOrders();
        }
        else { // not a chef -> error-handling
            Log.e("ChefScreen", "Current logged-in user is not a CHEF");

            // Output
            TextView noOrdersInProgress = findViewById(R.id.noOrdersInProgress);
            noOrdersInProgress.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Sets the appropriate welcome message to the user
     * @param message
     */
    private void setWelcomeMessage(String message) {
        editText.setText(message, TextView.BufferType.EDITABLE);
    }

    /**
     * OnClick method for buttons
     */
    private void attachOnClickListeners() {

        // Meal buttons
        Button menuButton = (Button) findViewById(R.id.viewMenuButton);
        Button offeredMealsButton = (Button) findViewById(R.id.viewOfferedButton);
        Button addButton = (Button) findViewById(R.id.addMealButton);
        // Order buttons
        Button viewOrder = (Button) findViewById(R.id.viewCompletedOrdersButton);
        Button viewPendingOrder = (Button) findViewById(R.id.viewPendingOrdersButton);


        menuButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MealsListScreen.class));
            }
        });

        offeredMealsButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // initialize a new intent
                Intent intent = new Intent(getApplicationContext(), MealsListScreen.class);
                // specify that we want to display offered meals
                intent.putExtra(MealsListScreen.MEALS_TYPE_ARG_KEY, MealsListScreen.MEALS_TYPE.OFFERED_MEALS.toString());
                // display the offered meals list
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NewMealScreen.class)); //show new meal screen
            }
        });

        viewOrder.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CompletedOrdersScreen.class)); //show completed orders
            }
        });



        viewPendingOrder.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PendingOrdersScreen.class)); //show pending orders
            }
        });

    }

    /**
     * When the user clicks the logout button, it will take back to the intro screen
     * @param view
     */
    public void clickLogout(View view) {
        // handle user logout
        App.getAppInstance().logoutUser();
        // take user back to intro screen
        Intent intent = new Intent(this, IntroScreen.class);
        startActivity(intent);
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void showNextScreen() {

    }

    @Override
    public void dbOperationSuccessHandler(Object dbOperation, Object payload) {

    }

    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {

    }
}