package com.example.mealer_project.ui.screens.checkout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.mealer_project.R;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;

/**
 * This screen will use the ListView to populate the screen with all items in the cart using
 * the class CheckoutAdapter for each items information.
 */
public class CheckoutScreen extends UIScreen implements StatefulView {

    // buttons
    private ImageButton backButton;
    private Button minusButton;
    private Button plusButton;
    private Button cancelButton;
    private Button orderButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_screen);

        // buttons for onClick
        backButton = (ImageButton) findViewById(R.id.button_back3);
        minusButton = (Button) findViewById(R.id.decrease_quantity);
        plusButton = (Button) findViewById(R.id.increase_quantity);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        orderButton = (Button) findViewById(R.id.order_button);

        // Process: calling helper method to attach listeners to all buttons
        attachOnClickListeners();

    }

    private void attachOnClickListeners(){

        // create alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);

        /* onClick logic for checkout button
        you add all the cart order items to the order and call order handler */

        // on click method for back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            } // go to previous screen
        });

        // on click method for minus button
        minusButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                // update quantity on screen and update price of item

            }
        });

        // on click method for plus button
        plusButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                // update quantity on screen and update price of item

            }
        });

        // on click method for order button
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // on click method for order button
        cancelButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setTitle("Are you sure you wish to continue?");
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
        ListView itemsInCart = findViewById(R.id.items_in_cart);
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
