package com.example.mealer_project.ui.screens;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.mealer_project.R;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;

public class CheckoutScreen extends UIScreen implements StatefulView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_screen);

        // Process: calling helper method to attach listeners to all buttons
        //attachOnClickListeners();

    }

    private void attachOnClickListeners(){

        /* onClick logic for checkout button
        you add all the cart order items to the order and call order handler */

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
