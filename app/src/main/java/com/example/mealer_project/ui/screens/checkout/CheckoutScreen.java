package com.example.mealer_project.ui.screens.checkout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.orders.OrderItem;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.example.mealer_project.ui.screens.search.SearchMealItem;
import com.example.mealer_project.ui.screens.search.SearchMealItemsAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This screen will use the ListView to populate the screen with all items in the cart using
 * the class CheckoutAdapter for each items information.
 */
public class CheckoutScreen extends UIScreen implements StatefulView {

    // buttons
    private ImageButton backButton;
    private Button cancelButton;
    private Button orderButton;

    private CheckoutAdapter ordersAdapter;
    Map<OrderItem, Boolean> orderData;

    // list to store the items in the cart
    private List<OrderItem> orderItemList;

    // key to specify the item being displayed
    public final static String CHECKOUT_TYPE_ARG_KEY = "CHECKOUT_TYPE_ARG_KEY";

    //
    public final static String CHECKOUT_DATA_ARG_KEY = "CHECKOUT_DATA_ARG_KEY";

    // items adapter
    private CheckoutAdapter checkoutAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_screen);

        loadCartData();

        populateCartWithItems();

        // buttons for onClick
        backButton = (ImageButton) findViewById(R.id.button_back3);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        orderButton = (Button) findViewById(R.id.order_button);

        // Process: calling helper method to attach listeners to all buttons
        attachOnClickListeners();

    }

    private void loadCartData() {
        if (App.getClient() != null) {
            // get search meal items from app's current instance
            this.orderData = App.getClient().getCart();
        }
        Log.e("cartLoaded", "Loaded meals: " + this.orderData.size());
    }

    private void attachOnClickListeners(){

        // create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Are you sure you wish to continue?");

        /* onClick logic for checkout button
        you add all the cart order items to the order and call order handler */

        // on click method for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            } // go to previous screen
        });

        // on click method for order button
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // on click method for order button (with Alert Dialog)
        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setMessage("You will be clearing your cart and returning to the main page.");

                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // CLEAR CART
                                // finish(); <- NOTE: not sure if this will work with the alert dialog, so you may have to call the class then finish!
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    private void populateCartWithItems() {
        // initialize smItems as empty list
        this.orderItemList = new ArrayList<>();
        // get the list view component
        ListView orderList = findViewById(R.id.items_in_cart);
        // get the adapter
        this.ordersAdapter = new CheckoutAdapter(this, R.layout.activity_checkout_screen, this.orderItemList);
        // attach adapter to list view
        orderList.setAdapter(this.ordersAdapter);
        // add data to the adapter
        for (OrderItem sMItemId: this.orderData.keySet()) {
            this.orderItemList.add(sMItemId);
        }
        Log.e("cartPopulation", "Populated the list: " + this.orderItemList.size());

    }

    @Override
    public void updateUI() {
        // you update the UI when the client decides to remove an order item from the cart
        // so first you'd call order handler,
        // then you'd update the UI if the removal is successful
    }

    @Override
    public void showNextScreen() {
        // this should be called when the final submit button is clicked
        // this should finish the activity

        this.finish(); //returning to client's home screen
    }

    @Override
    public void dbOperationSuccessHandler(Object dbOperation, Object payload) {

    }

    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {

    }

}
