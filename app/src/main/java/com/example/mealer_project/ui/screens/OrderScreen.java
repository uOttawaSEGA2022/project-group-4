package com.example.mealer_project.ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;

import java.util.ArrayList;

public class OrderScreen extends UIScreen implements StatefulView {

    // button instantiations
    ImageButton backButton;
    Button minusButton;
    Button plusButton;
    Button addOrRemoveButton;
    Button checkoutButton;

    // counter for quantity
    int totalQuantityCounter;

    // flag to keep track for whether the meal should be added or removed from cart
    boolean addToCart = false; // false until added to cart

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_screen);

        // buttons for onClick methods
        backButton = (ImageButton) findViewById(R.id.button_back);
        minusButton = (Button) findViewById(R.id.minus_button);
        plusButton = (Button) findViewById(R.id.add_button);
        addOrRemoveButton = (Button) findViewById(R.id.add_or_remove_from_cart);
        checkoutButton = (Button) findViewById(R.id.checkout_button);

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
                totalQuantityCounter--;
                updateUI();
            }
        });

        // on click method for plus button
        plusButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalQuantityCounter++;
                updateUI();
            }
        });

        // on click method for adding or removing the meal from the cart
        addOrRemoveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addToCart == false) { // clicked add to cart
                    addToCart = true;
                } else { // clicked remove from cart
                    addToCart = false;
                }
                updateUI();
            }
        });

        // on click method for checkout button
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextScreen();
            }
        });
    }

    /**
     * Updates the screen with the required information
     *
     * NOTE: Use addToCart variable as the parameter for the flag and pass in totalQuantityCounter as the
     * parameter for quantity in updateOrderScreen method!
     */
    @Override
    public void updateUI() {
        // get the information from the previous screen to fill out the meal's information
        // call the method updateOrderScreen
    }

    /**
     * Takes the client to the Checkout Screen
     *
     * NOTE: Yet to create this screen
     */
    @Override
    public void showNextScreen() {
        /*
        Intent intent = new Intent(getApplicationContext(), Checkout.class);
        startActivity(intent);
        */
    }

    @Override
    public void dbOperationSuccessHandler(Object dbOperation, Object payload) {

    }

    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {

    }

    /**
     * Updates the text on the screen to its respective information
     * @param mealTitle
     * @param price
     * @param mealType
     * @param cuisineType
     * @param ingredients
     * @param allergens
     * @param description
     * @param flag
     * @param quantity
     */
    public void updateOrderScreen (String mealTitle, double price, String mealType, String cuisineType,
                                    String ingredients, ArrayList<String> allergens, String description,
                                    boolean flag, int quantity) {
        // sets the text for the meal name
        TextView mealNameText = (TextView) findViewById(R.id.order_meal_name);
        mealNameText.setText(mealTitle);

        // sets the text for price
        TextView priceText = (TextView) findViewById(R.id.order_price_of_meal);
        priceText.setText("$ " + String.valueOf(price));

        // sets the text for the meal type
        TextView mealTypeText = (TextView) findViewById(R.id.order_msg_type);
        mealTypeText.setText(mealType);

        // sets the text for cuisine type
        TextView cuisineText = (TextView) findViewById(R.id.order_msg_cuisine);
        cuisineText.setText(cuisineType);

        // sets the text for the ingredients
        TextView ingredientsText = (TextView) findViewById(R.id.order_msg_ingredients);
        ingredientsText.setText(ingredients);

        // sets the text for allergens
        TextView allergensText = (TextView) findViewById(R.id.order_msg_allergens);
        String allergensString = String.join(", ", allergens); // convert arraylist to string

        if (allergensString.length() == 0) {
            allergensText.setText("N/A");
        } else {
            allergensText.setText(allergensString);
        }

        // sets the text for description
        TextView descriptionText = (TextView) findViewById(R.id.order_msg_description);
        descriptionText.setText(description);

        /* sets the text for the add/remove from cart button
        Message depends on state of flag - true if they have not added the
        meal to the cart yet, false if otherwise */
        Button addOrRemovingButton = (Button)findViewById(R.id.add_or_remove_from_cart);

        if (flag == false) { // meal has not been added to cart
            addOrRemovingButton.setText("Add to Cart");
        } else { // meal has already been added to cart (flag = true)
            addOrRemovingButton.setText("Remove from Cart");
        }

        // sets the text (counter) for the quantity total
        TextView quantityText = (TextView) findViewById(R.id.order_quantity);
        quantityText.setText(quantity);

    }
}