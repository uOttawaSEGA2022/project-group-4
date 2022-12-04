package com.example.mealer_project.ui.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.handlers.OrderHandler;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.data.models.User;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.example.mealer_project.ui.screens.completed_orders.CompletedOrdersScreen;
import com.example.mealer_project.ui.screens.meals.MealsListScreen;
import com.example.mealer_project.ui.screens.pending_orders.PendingOrdersScreen;

import java.util.ArrayList;
import java.util.List;

public class ChefScreen extends UIScreen implements StatefulView {
    // Variable Declaration
    TextView editText;

    /**
     * the map that contains the current CHEF's Orders
     */
    private List<Order> orderData;

    /**
     * the list that will hold the orders
     */
    private List<Order> orders;

    /**
     * the array adapter for the list view of the orders in progress
     */
    private OrdersInProgressAdapter ordersInProgressAdapter;

    //----------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_screen);
        App.getAppInstance().setOrdersInProgressScreen(this);

        // HEADER
        editText = (TextView) findViewById(R.id.welcome_message_chef);
        User currentUser = App.getAppInstance().getUser();
        if (App.getAppInstance().isUserAuthenticated()) { // Change text to proper welcome message when opened
            setWelcomeMessage("Welcome " + currentUser.getFirstName() + " " + currentUser.getLastName() + ", you're logged in as a CHEF!");
        }

        // Initialization
        attachOnClickListeners();

        // Process: loading the in progress orders
        loadOrdersInProgress();

        // Process: populate the Orders ListView
        populateOrdersInProgress();

    }

    /**
     * retrieves the current chef's orders in progress
     */
    private void loadOrdersInProgress() {

        // Process: Check if current user is a chef
        if (App.getUser() instanceof Chef) { // is a CHEF
            // Initialization: setting ordersData to the list
            this.orderData = ((Chef) App.getUser()).ORDERS.getOrdersInProgress();
        } else { // not a chef -> error-handling
            Log.e("ChefScreen", "Current logged-in user is not a CHEF");
        }
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

        // Process: if theres no meals, display no meals in progress text
        if (orderData.size() == 0) {
            TextView noMealsInProgress = (TextView) findViewById(R.id.noOrdersInProgress);
            noMealsInProgress.setVisibility(View.VISIBLE);
        }

        // Process: loop through the map of data for the orders in progress
        for (Order order : this.orderData) {
            ordersInProgressAdapter.add(order); // adds orderData to the list
        }
    }

    /**
     * this helper method populates the Orders list after a change has been made
     */
    private void repopulatePendingOrdersList() {

        ordersInProgressAdapter.clear(); //removing all previous data

        // Process: if theres no meals, display no meals in progress text
        if (orderData.size() == 0) {
            TextView noMealsInProgress = (TextView) findViewById(R.id.noOrdersInProgress);
            noMealsInProgress.setVisibility(View.VISIBLE);
        } else {
            TextView noMealsInProgress = (TextView) findViewById(R.id.noOrdersInProgress);
            noMealsInProgress.setVisibility(View.INVISIBLE);
        }

        // Process: looping through the map of data
        for (Order order : this.orderData) {
            ordersInProgressAdapter.add(order); //adding the orderData to the list
        }

    }

    public OrdersInProgressAdapter getOrdersInProgressAdapter() {
        return ordersInProgressAdapter;
    }

    public void updateAdapter() {
        loadOrdersInProgress();
        repopulatePendingOrdersList();

        ordersInProgressAdapter.notifyDataSetChanged();
    }

    /**
     * Sets the appropriate welcome message to the user
     *
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

        //Profile Button
        Button profileButton = (Button) findViewById(R.id.chefProfile);

        // Order buttons
        Button viewOrder = (Button) findViewById(R.id.viewCompletedOrdersButton);
        Button viewPendingOrder = (Button) findViewById(R.id.viewPendingOrdersButton);
        // View profile button
        ImageView viewChefProfile = (ImageView) findViewById(R.id.mealer_logo);

        // create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("NOTICE");
        // set the icon for the alert dialog
        builder.setIcon(R.drawable.mealer);

        menuButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if chef has no meals
                if (App.getChef().MEALS.getMenu().size() == 0 || App.getChef().MEALS.getMenu() == null) {

                    builder.setMessage("You have no meals!");
                    builder.setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else
                    // chef has meals
                    startActivity(new Intent(getApplicationContext(), MealsListScreen.class));
            }
        });

        offeredMealsButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if chef has no offered meals
                if (App.getChef().MEALS.getOfferedMeals().size() == 0 || App.getChef().MEALS.getOfferedMeals() == null) {
                    builder.setMessage("You have no offered meals!");
                    builder.setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    // initialize a new intent
                    Intent intent = new Intent(getApplicationContext(), MealsListScreen.class);
                    // specify that we want to display offered meals
                    intent.putExtra(MealsListScreen.MEALS_TYPE_ARG_KEY, MealsListScreen.MEALS_TYPE.OFFERED_MEALS.toString());
                    // display the offered meals list
                    startActivity(intent);
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NewMealScreen.class)); //show new meal screen
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChefInfoScreen.class)); //Show Chef's Profile
            }
        });

        viewOrder.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((Chef) App.getUser()).ORDERS.getCompletedOrders().size() != 0)
                    startActivity(new Intent(getApplicationContext(), CompletedOrdersScreen.class)); //show completed orders
                else {
                    builder.setMessage("You have no completed orders!");
                    builder.setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        viewPendingOrder.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((Chef) App.getUser()).ORDERS.getPendingOrders().size() != 0)
                    startActivity(new Intent(getApplicationContext(), PendingOrdersScreen.class)); //show pending orders
                else {
                    builder.setMessage("You have no pending orders!");
                    builder.setPositiveButton("Okay",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        viewChefProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ChefInfoScreen.class));
            }
        });
    }

    /**
     * When the user clicks the logout button, it will take back to the intro screen
     *
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
        if (dbOperation == OrderHandler.dbOperations.UPDATE_ORDER) {

            // Output: successfully completed an order
            displayErrorToast("Successfully completed order!");

        } else { // other op
            displayErrorToast((String) payload);
        }
        updateAdapter();
    }

    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {
        if (dbOperation == OrderHandler.dbOperations.UPDATE_ORDER) {

            // Output: failed to update order
            displayErrorToast("Failed to update order!");

        }
    }
}