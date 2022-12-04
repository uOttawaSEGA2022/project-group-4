package com.example.mealer_project.ui.screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.handlers.OrderHandler;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.data.models.orders.ChefInfo;
import com.example.mealer_project.data.models.orders.OrderItem;
import com.example.mealer_project.ui.screens.search.SearchMealItem;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.example.mealer_project.utils.Preconditions;
import com.example.mealer_project.utils.Response;

import java.text.DecimalFormat;
import java.util.Map;

public class OrderScreen extends UIScreen implements StatefulView {

    // format price to two decimal places
    private static final DecimalFormat df = new DecimalFormat("0.00");

    // store meals and chef data
    SearchMealItem sMItem;
    Meal mealData;
    ChefInfo chefData;

    // key
    public static final String SEARCH_MEAL_ITEM_ARG_KEY = "SEARCH_MEAL_ITEM_ARG_KEY";

    // button instantiations
    private ImageButton backButton;
    private Button minusButton;
    private Button plusButton;
    private Button addOrRemoveButton;
    private EditText quantityText;

    // counter for quantity
    int totalQuantityCounter = 1;
    // flag to track if item needs to be added to the cart
    boolean addToCart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_screen);

        // buttons for onClick methods
        backButton = (ImageButton) findViewById(R.id.button_back);
        minusButton = (Button) findViewById(R.id.minus_button);
        plusButton = (Button) findViewById(R.id.add_button);
        addOrRemoveButton = (Button) findViewById(R.id.add_or_remove_from_cart);
        quantityText = (EditText) findViewById(R.id.order_quantity);

        // Process: attaching listeners to buttons
        attachOnClickListeners();

        // Get the meal and chef data through intent
        Response response = loadMealAndChefData();
        // if loading the data was successful
        if (response.isSuccess()) {
            // display the meal and chef data
            displayMealAndChefData();
            // display quantity
            updateOrderQuantity();
            // set appropriate text for the button
            setAddOrRemoveButtonText();
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
        backButton.setOnClickListener(v -> {
            finish(); // go to previous screen
        });

        // on click method for minus button
        minusButton.setOnClickListener(v -> {
            if (totalQuantityCounter != 1) {
                totalQuantityCounter--;
                updateUI();
            }
        });

        // on click method for plus button
        plusButton.setOnClickListener(v -> {
            totalQuantityCounter++;
            updateUI();
        });

        // Variable Declaration
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.mealer);
        builder.setCancelable(true);
        builder.setTitle("Please confirm your selection");

        // on click method for adding or removing the meal from the cart
        addOrRemoveButton.setOnClickListener(v -> {
            // if current quantity zero, we don't need to do anything
            if (totalQuantityCounter == 0) {
                displayErrorToast("Please select quantity!");
                return;
            }

            // ensure a valid client available at this point
            if (Preconditions.isNotNull(App.getClient())) {
                // if adding item to the cart
                if (addToCart) {
                    // Process: checking if cart is empty
                    if (!App.getClient().getCart().isEmpty()) { //not empty

                        // Variable Declaration
                        Map.Entry<OrderItem, Boolean> entry = App.getClient().getCart().entrySet().iterator().next();
                        OrderItem orderItem = entry.getKey();

                        // Process: checking if random orderItem chef is same as current chef
                        if (orderItem.getSearchMealItem().getChef().getChefName().equals(sMItem.getChef().getChefName())) { //same chef

                            // add order item to client's cart
                            App.getClient().updateOrderItem(new OrderItem(sMItem, totalQuantityCounter));
                            displaySuccessToast("Item added to cart!");

                            // finish the activity and return back
                            setResult(Activity.RESULT_OK);
                            finish();

                        }
                        else { //different chef

                            // setting alert text
                            builder.setMessage("This meal is offered by a different chef. Would you like to clear your cart and start a new order?");

                            // Process: setting actions for pos/neg buttons
                            builder.setPositiveButton("Start new order",
                                    (dialog, which) -> {

                                        App.getClient().clearCart(); //clearing cart

                                        // add order item to client's cart
                                        App.getClient().updateOrderItem(new OrderItem(sMItem, totalQuantityCounter));
                                        displaySuccessToast("Item added to cart!");

                                        // finish the activity and return back
                                        setResult(Activity.RESULT_OK);
                                        finish();

                                    });
                            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> {});

                            AlertDialog dialogue = builder.create();

                            dialogue.show();

                        }

                    }
                    else { //empty cart

                        // add order item to client's cart
                        App.getClient().updateOrderItem(new OrderItem(sMItem, totalQuantityCounter));
                        displaySuccessToast("Item added to cart!");

                        // finish the activity and return back
                        setResult(Activity.RESULT_OK);
                        finish();

                    }

                }
                // removing item from cart
                else {
                    App.getClient().updateOrderItem(new OrderItem(sMItem, 0));
                    displaySuccessToast("Item removed from cart!");
                    // finish the activity and return back
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            } else {
                displayErrorToast("Unable to update cart!");
            }

        });

    }

    // Helper Methods for Adding the Meal------------------------------------------------------------------------
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
                            // set add to cart as false, so we only allow remove from cart
                            this.addToCart = false;
                            // set quantity buttons to unclickable
                            plusButton.setClickable(false);
                            minusButton.setClickable(false);
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

    // UI Methods-----------------------------------------------------------------------------------------------
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

    /**
     * Displays meal and chef data
     */
    private void displayMealAndChefData () {
        // sets the text for the meal name
        TextView mealNameText = (TextView) findViewById(R.id.order_meal_name);
        mealNameText.setText(this.mealData.getName());

        // sets the text for price
        TextView priceText = (TextView) findViewById(R.id.order_price_of_meal);
        priceText.setText("$ ".concat(df.format(this.mealData.getPrice())));

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

        //// Chef's details

        // sets the text for the chef's name
        TextView chefNameText = (TextView) findViewById(R.id.order_chef_name_msg);
        chefNameText.setText(String.valueOf(this.chefData.getChefName()));

        // chef's address
        ((TextView) findViewById(R.id.os_chef_address)).setText(this.chefData.getChefAddress().toString());

        // chef's rating
        ((RatingBar) findViewById(R.id.os_chef_rating)).setRating((float) this.chefData.getChefRating());
    }

    private void updateOrderQuantity() {
        // sets the text (counter) for the quantity total
        quantityText.setText(String.valueOf(this.totalQuantityCounter), TextView.BufferType.EDITABLE);
    }

    private void setAddOrRemoveButtonText() {
        // sets the text for the add/remove from cart button

        // meal has not been added to cart
        if (addToCart) {
            addOrRemoveButton.setText("Add to Cart");
        }

        else {
            // meal has already been added to cart, so only action allowed is to remove from cart
            addOrRemoveButton.setText("Remove from Cart");
            // disable quantity box
            quantityText.setFocusable(false);
            quantityText.setEnabled(false);
            quantityText.setCursorVisible(false);
            quantityText.setKeyListener(null);
            quantityText.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    // Firebase Methods------------------------------------------------------------------------------------------
    @Override
    public void dbOperationSuccessHandler(Object dbOperation, Object payload) {

        if (dbOperation == OrderHandler.dbOperations.ADD_ORDER) {

            // Output: successfully add new order
            displayErrorToast("Successfully added order!");

        }
        else if (dbOperation == OrderHandler.dbOperations.REMOVE_ORDER) {

            // Output: successfully removed order
            displayErrorToast("Successfully removed order!");

        }
        else if (dbOperation == OrderHandler.dbOperations.UPDATE_ORDER) {

            // Output: successfully updated order
            displayErrorToast("Successfully updated order!");

        }
        else if (dbOperation == OrderHandler.dbOperations.GET_ORDER_BY_ID) {

            // Output: successfully retrieved order
            displayErrorToast("Successfully retrieved order!");

        }
        else { //other op

            // Output
            displayErrorToast((String) payload);

        }

    }

    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {

        if (dbOperation == OrderHandler.dbOperations.ADD_ORDER) {

            // Output: failed to add new order
            displayErrorToast("Failed to add order!");

        }
        else if (dbOperation == OrderHandler.dbOperations.REMOVE_ORDER) {

            // Output: failed to remove order
            displayErrorToast("Failed to remove order!");

        }
        else if (dbOperation == OrderHandler.dbOperations.UPDATE_ORDER) {

            // Output: failed to update order
            displayErrorToast("Failed to update order!");

        }
        else if (dbOperation == OrderHandler.dbOperations.GET_ORDER_BY_ID) {

            // Output: failed to get order
            displayErrorToast("Failed to get order!");

        }
        else { //other error

            // Output
            displayErrorToast((String) payload);

        }

    }
}