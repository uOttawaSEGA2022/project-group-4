package com.example.mealer_project.ui.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.handlers.MealHandler;
import com.example.mealer_project.data.handlers.OrderHandler;
import com.example.mealer_project.data.models.Client;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.data.models.inbox.Complaint;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class OrderScreen extends UIScreen implements StatefulView {

    Meal mealData;

    // button instantiations
    private ImageButton backButton;
    private Button minusButton;
    private Button plusButton;
    private Button addOrRemoveButton;
    private Button checkoutButton;

    // counter for quantity
    int totalQuantityCounter = 0;

    // flag to keep track for whether the meal should be added or removed from cart
    private boolean addToCart = false; // false until added to cart
    // this variable is weird bc where are we gonna retrieve this data from? the cart map in the Client class??

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_screen);

        // buttons for onClick methods
        backButton = (ImageButton) findViewById(R.id.button_back);
        minusButton = (Button) findViewById(R.id.minus_button);
        plusButton = (Button) findViewById(R.id.add_button);
        addOrRemoveButton = (Button) findViewById(R.id.add_or_remove_from_cart);

        // Process: attaching listeners to buttons
        attachOnClickListeners();

        // Process: getting the meal data
        try {
            //mealData = (Meal) getIntent().getSerializableExtra(SearchScreen.SEARCH_OBJ_INTENT_KEY);
        }
        catch (Exception e) { //error-handling

            // Output
            Log.e("OrderScreen", "unable to get meal info");
            displayErrorToast("Unable to display meal!");

        }

    }

    /**
     * this helper method sets all the on click methods for the buttons of the XML screen
     */
    private void attachOnClickListeners() {

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
        /*addOrRemoveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (addToCart == false) { // clicked add to cart

                    addToCart = true; //updating flag

                    // Variable Declaration
                    Client client = (Client) App.getUser();

                    // Process: adding to the client's cart map
                    client.addToCart(mealData.getMealID(), totalQuantityCounter);

                    finish(); //finish the activity

                }
                else { // clicked remove from cart

                    addToCart = false; //updating flag

                    // Variable Declaration
                    Client client = (Client) App.getUser();

                    // Process: removing from client's cart map
                    client.removeFromCart(mealData.getMealID());

                }

                updateUI();

            }
        });*/

        // on click method for checkout button
        /*checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Variable Declaration
                Client client = (Client) App.getUser();

                // Process: calling order handler method to add entire cart to order & telling firebase to store order
                App.ORDER_HANDLER.dispatch(OrderHandler.dbOperations.ADD_ORDER, client.getCart());

                // Process: going to CheckoutScreen
                showNextScreen();

            }
        });*/

    }

    /**
     * Updates the screen with the required information
     *
     * NOTE: Use addToCart variable as the parameter for the flag and pass in totalQuantityCounter as the
     * parameter for quantity in updateOrderScreen method!
     */
    @Override
    public void updateUI() {

        // Process: calling helper method to update the screen, based on obtained meal data
        updateOrderScreen(mealData.getName(), mealData.getPrice(), mealData.getMealType(),
                mealData.getCuisineType(), mealData.getIngredients(), mealData.getAllergens(),
                mealData.getDescription(), addToCart, totalQuantityCounter);

    }

    /**
     * Takes the client to the Checkout Screen
     *
     * NOTE: Yet to create this screen
     */
    @Override
    public void showNextScreen() {
        // this should go to the checkout screen
        /*
        Intent intent = new Intent(getApplicationContext(), Checkout.class);
        startActivity(intent);
        */
    }

    @Override
    public void dbOperationSuccessHandler(Object dbOperation, Object payload) {

        if (dbOperation == OrderHandler.dbOperations.ADD_ORDER) { // adding new order completed

            // Output
            displaySuccessToast((String) payload);


            // Process: finish the activity and return
            this.setResult(Activity.RESULT_OK);

            this.finish(); //returning to search screen

        }
        //else if -> add logic for other methods if needed

    }

    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {

        if (dbOperation == OrderHandler.dbOperations.ADD_ORDER) {

            // Output: failed to add new order
            displayErrorToast("Failed to add order!");

        }
        else {

            // Output
            displayErrorToast((String) payload);

        }

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
    private void updateOrderScreen (String mealTitle, double price, String mealType, String cuisineType,
                                    String ingredients, ArrayList<String> allergens, String description,
                                    boolean flag, int quantity) {
        // sets the text for the meal name
        TextView mealNameText = (TextView) findViewById(R.id.order_meal_name);
        mealNameText.setText(mealTitle);

        // sets the text for price
        TextView priceText = (TextView) findViewById(R.id.order_price_of_meal);
        priceText.setText("$ " + String.valueOf(price));

        // sets the text for the chef's name
        TextView chefNameText = (TextView) findViewById(R.id.order_chef_name_msg);
        chefNameText.setText("PLEASE CHANGE ME TO THE CHEF'S NAME!");

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