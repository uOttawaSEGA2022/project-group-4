package com.example.mealer_project.ui.screens.checkout;

import static com.example.mealer_project.data.handlers.OrderHandler.dbOperations.ADD_ORDER;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.orders.OrderItem;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This screen will use the ListView to populate the screen with all items in the cart using
 * the class CheckoutAdapter for each items information.
 */
public class CheckoutScreen extends UIScreen implements StatefulView {

    // format price to two decimal places
    private static final DecimalFormat df = new DecimalFormat("0.00");

    // buttons
    private ImageButton backButton;
    private Button cancelButton;
    private Button orderButton;

    Map<OrderItem, Boolean> orderData;

    // list to store the items in the cart
    private List<OrderItem> orderItemList;

    // items adapter
    private CheckoutAdapter checkoutAdapter;

    // -------------------------------------------------------------------------------------------------------------------------------------
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

    private void attachOnClickListeners() {

        // create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Are you sure you wish to continue?");
        // set the icon for the alert dialog
        builder.setIcon(R.drawable.mealer);

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
                handleOrderButton();
                if (App.getClient() != null) {
                    App.getClient().clearCart(); //clears cart
                    orderData = App.getClient().getCart(); //sets orderData to equal the empty cart
                }
                displaySuccessToast("Order Has Been Placed!");
                showNextScreen(); //finishes action
            }
        });

        // on click method for order button (with Alert Dialog)
        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setMessage("You will be clearing your cart and returning to the previous page.");

                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                App.getClient().clearCart(); //clears cart
                                orderData = App.getClient().getCart(); //sets orderData to equal the empty cart
                                finish(); //finishes action
                                Log.e("cartClear", "Clear the list: " + orderData.size());
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
        // initialize orderItemList as empty list
        this.orderItemList = new ArrayList<>();
        // get the list view component
        ListView orderList = findViewById(R.id.items_in_cart);
        // get the adapter
        this.checkoutAdapter = new CheckoutAdapter(this, R.layout.activity_checkout_screen, this.orderItemList);
        // attach adapter to list view
        orderList.setAdapter(this.checkoutAdapter);
        // add data to the adapter
        for (OrderItem sMItemId : this.orderData.keySet()) {
            this.orderItemList.add(sMItemId);
        }
        // if we do have items in cart
        if (!this.orderItemList.isEmpty()) {
            /// make the order submission container visible
            LinearLayout orderSubmissionContainer = findViewById(R.id.orderSubmissionContainer);
            orderSubmissionContainer.setVisibility(View.VISIBLE);
            // display total cost
            TextView totalCostAmount = findViewById(R.id.totalCostAmount);
            totalCostAmount.setText(getTotalCost());
            // display credit card info
            displayCreditCardInfo();
        } else {
            // make the order submission container invisible
            LinearLayout orderSubmissionContainer = findViewById(R.id.orderSubmissionContainer);
            orderSubmissionContainer.setVisibility(View.GONE);
        }
        Log.e("cartPopulation", "Populated the list: " + this.orderItemList.size());
    }

    private void handleOrderButton() {
        App.ORDER_HANDLER.dispatch(ADD_ORDER, null, this);
    }

    private String getTotalCost() {
        if (this.orderItemList != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                df.setRoundingMode(RoundingMode.UP);
                Log.e("CHECKOUTSCREEN", (df.format(orderItemList.stream().mapToDouble(el -> el.getSearchMealItem().getMeal().getPrice() * el.getQuantity()).sum())));
                return df.format(orderItemList.stream().mapToDouble(el -> el.getSearchMealItem().getMeal().getPrice() * el.getQuantity()).sum());
            }
        }
        // if no order items
        return String.valueOf(0.00);
    }

    private void displayCreditCardInfo() {
        // make sure we have a valid client
        if (App.getClient() != null) {
            TextView clientCreditCard = findViewById(R.id.clientCreditCardInfo);
            String creditCardNumber = App.getClient().getClientCreditCard().getNumber();
            String creditCardHashed = "XXXX-XXXX-XXXX-" + creditCardNumber.substring(creditCardNumber.length() - 4);
            clientCreditCard.setText(creditCardHashed);
        }
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