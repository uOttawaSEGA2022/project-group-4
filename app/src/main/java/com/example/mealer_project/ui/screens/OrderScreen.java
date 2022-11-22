package com.example.mealer_project.ui.screens;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.handlers.OrderHandler;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Client;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.data.models.orders.ChefInfo;
import com.example.mealer_project.data.models.orders.OrderItem;
import com.example.mealer_project.data.models.orders.SearchMealItem;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.example.mealer_project.utils.Preconditions;
import com.example.mealer_project.utils.Response;

public class OrderScreen extends UIScreen implements StatefulView {

    // store meals and chef data
    SearchMealItem sMItem;
    Meal mealData;
    ChefInfo chefData;

    public static final String SEARCH_MEAL_ITEM_ARG_KEY = "SEARCH_MEAL_ITEM_ARG_KEY";

    // button instantiations
    private ImageButton backButton;
    private Button minusButton;
    private Button plusButton;
    private Button addOrRemoveButton;
    private Button checkoutButton;

    // counter for quantity
    int totalQuantityCounter = 0;
    // flag to track if item needs to be added to the cart
    boolean addToCart = false;

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

        // Get the meal and chef data through intent
        Response response = loadMealAndChefData();
        // if loading the data was successful
        if (response.isSuccess()) {
            // display the meal and chef data
            displayMealAndChefData();
            // display quantity and the set appropriate button text
            updateOrderQuantity();
        } else {
            // display error message if unable to load meals data
            displayErrorToast(response.getErrorMessage());
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
        addOrRemoveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ensure a valid client available at this point
                if (Preconditions.isNotNull(App.getClient())) {
                    // if adding item to the cart
                    if (addToCart) {
                        // add order item to client's cart
                        App.getClient().updateOrderItem(new OrderItem(sMItem, totalQuantityCounter));
                        displaySuccessToast("item added to cart!");
                    }
                    // removing item from cart
                    else {
                        App.getClient().updateOrderItem(new OrderItem(sMItem, 0));
                        displaySuccessToast("item removed from cart!");
                    }
                } else {
                    displayErrorToast("Unable to update cart!");
                }
                // finish the activity and return back
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

    }

    private Response loadMealAndChefData() {
        try {
            // we get the data from the SearchMealItem passed from the search screen
            // ensure we have intent
            if (getIntent() != null) {
                // ensure we have the data we're looking for in the intent
                if (getIntent().getSerializableExtra(SEARCH_MEAL_ITEM_ARG_KEY) != null) {
                    // get the search meal item
                    this.sMItem = (SearchMealItem) getIntent().getSerializableExtra(SEARCH_MEAL_ITEM_ARG_KEY);
                    // get the meal and chef data
                    this.mealData = this.sMItem.getMeal();
                    this.chefData = this.sMItem.getChef();

                    // check if meal is present in cart already, so we can display present quantity
                    if (App.getClient() != null) {
                        // get the order item for the meal (if present)
                        OrderItem orderItem = App.getClient().getOrderItem(mealData.getMealID());
                        if (orderItem != null) {
                            // update the meal's order quantity
                            this.totalQuantityCounter = orderItem.getQuantity();
                            // display the order quantity
                            updateUI();
                        }
                    }
                    // return response so UI can be updated
                    return new Response(true);
                }
            }
        }
        catch (Exception e) { //error-handling
            // Output
            Log.e("OrderScreen", "unable to get meal info");
            displayErrorToast("Unable to display meal!");
        }

        // if we don't have valid data in intent
        return new Response(false, "No valid data available to display");
    }

    /**
     * Updates the screen with the required information
     */
    @Override
    public void updateUI() {

        // update the quantity displayed
        // as a side effect: if quantity is zero, Add to Cart button is shown, else, Remove from Cart
        updateOrderQuantity();

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
        else if (dbOperation == OrderHandler.dbOperations.REMOVE_ORDER) {

            // Output: failed to add new order
            displayErrorToast("Failed to remove order!");

        }
        else if (dbOperation == OrderHandler.dbOperations.UPDATE_ORDER) {

            // Output: failed to add new order
            displayErrorToast("Failed to update order!");

        }
        else if (dbOperation == OrderHandler.dbOperations.GET_ORDER_BY_ID) {

            // Output: failed to add new order
            displayErrorToast("Failed to get order!");

        }
        else { //other error

            // Output
            displayErrorToast((String) payload);

        }

    }

    /**
     * Displays meal and chef data
     */
    private void displayMealAndChefData () {
        // sets the text for the meal name
        TextView mealNameText = (TextView) findViewById(R.id.order_meal_name);
        mealNameText.setText(this.mealData.getName());

        // sets the text for price
        TextView priceText = (TextView) findViewById(R.id.order_price_of_meal);
        priceText.setText("$ ".concat(String.valueOf(this.mealData.getPrice())));

        // sets the text for the chef's name
        TextView chefNameText = (TextView) findViewById(R.id.order_chef_name_msg);
        chefNameText.setText(String.valueOf(this.chefData.getChefName()));

        // sets the text for the meal type
        TextView mealTypeText = (TextView) findViewById(R.id.order_msg_type);
        mealTypeText.setText(this.mealData.getMealType());

        // sets the text for cuisine type
        TextView cuisineText = (TextView) findViewById(R.id.order_msg_cuisine);
        cuisineText.setText(this.mealData.getCuisineType());

        // sets the text for the ingredients
        TextView ingredientsText = (TextView) findViewById(R.id.order_msg_ingredients);
        ingredientsText.setText(this.mealData.getIngredients());

        // sets the text for allergens
        TextView allergensText = (TextView) findViewById(R.id.order_msg_allergens);
        String allergensString = String.join(", ", this.mealData.getAllergens()); // convert arraylist to string

        if (allergensString.length() == 0) {
            allergensText.setText("N/A");
        } else {
            allergensText.setText(allergensString);
        }

        // sets the text for description
        TextView descriptionText = (TextView) findViewById(R.id.order_msg_description);
        descriptionText.setText(this.mealData.getDescription());
    }

    private void updateOrderQuantity() {
        // sets the text (counter) for the quantity total
        TextView quantityText = (EditText) findViewById(R.id.order_quantity);
        quantityText.setText(this.totalQuantityCounter);


        // sets the text for the add/remove from cart button
        // Message depends on state of totalQuantityCounter
        // If quantity is 0, then we show add to cart, else we show remove from cart
        Button addOrRemovingButton = (Button)findViewById(R.id.add_or_remove_from_cart);

        if (this.totalQuantityCounter == 0) { // meal has not been added to cart
            this.addToCart = true;
            addOrRemovingButton.setText("Add to Cart");
        }

        else {
            // meal has already been added to cart, so only action allowed is to remove from cart
            addToCart = false;
            addOrRemovingButton.setText("Remove from Cart");
            // disable quantity box
            quantityText.setFocusable(false);
            quantityText.setEnabled(false);
            quantityText.setCursorVisible(false);
            quantityText.setKeyListener(null);
            quantityText.setBackgroundColor(Color.TRANSPARENT);
        }


    }
}