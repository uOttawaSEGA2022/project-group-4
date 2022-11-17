package com.example.mealer_project.ui.screens;

import static com.example.mealer_project.ui.screens.meals.MealsListScreen.MEALS_DATA_ARG_KEY;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.handlers.MealHandler;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.example.mealer_project.ui.screens.meals.MealsAdapter;
import com.example.mealer_project.ui.screens.meals.MealsListScreen;

import java.util.ArrayList;

public class MealInfoScreen extends UIScreen implements StatefulView {

    // create a new object of type meal that contains the respective meal's information/data
    Meal mealData;

    Button offeringButton;

    // key to pass meal's information through intent
    public final static String MEAL_DATA_ARG_KEY = "MEAL_DATA_ARG_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_info_screen);

        // buttons for onClick methods
        offeringButton = (Button) findViewById(R.id.offering_btn);
        Button removeButton = (Button) findViewById(R.id.remove_btn);

        // get meal data
        try {
            mealData = (Meal) getIntent().getSerializableExtra(MealsAdapter.MEALS_DATA_ARG_KEY);
            updateUI();
        } catch (Exception e) {
            Log.e("MealInfoScreen", "unable to create meal object :(");
            displayErrorToast("Unable to retrieve the meal info!");
        }

        // alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // confirm their click to change offering
        builder.setCancelable(true);
        builder.setTitle("Please confirm your selection");
        // change text
        if (mealData.isOffered()) { // currently being offered
            builder.setMessage("You will be unoffering this meal now!");
        } else { // currently is not offered
            builder.setMessage("You will be offering this meal now!");
        }

        // on click method for changing the offering value of the meal
        offeringButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                offeredButtonClickHandler();
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

        // on click method for removing a meal (only if it is not currently offered)
        removeButton.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mealData.isOffered()) { // currently being offered
                    displayErrorToast("CANNOT REMOVE AN OFFERED MEAL");
                } else { // currently is not offered
                    // change text
                    builder.setMessage("Are you sure you want to remove this meal? \nThis cannot be changed");

                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    removeButtonClickHandler();
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
            }
        });
    }

    /**
     * Confirms the action of the user to unoffer/offer the meal from the Action Dialog Pop-up
     */
    private void offeredButtonClickHandler() {
        if (mealData.isOffered()) { // currently offered
            Log.e("Meal ID", "" + mealData.getMealID());
            App.MEAL_HANDLER.dispatch(MealHandler.dbOperations.REMOVE_MEAL_FROM_OFFERED_LIST, mealData.getMealID(), this);
            mealData.setOffered(false);
            updateUI();
        } else { // currently not offered
            App.MEAL_HANDLER.dispatch(MealHandler.dbOperations.ADD_MEAL_TO_OFFERED_LIST, mealData.getMealID(), this); // is now offering
            mealData.setOffered(true);
            updateUI();
        }
    }

    private void removeButtonClickHandler() {
        App.MEAL_HANDLER.dispatch(MealHandler.dbOperations.REMOVE_MEAL, mealData.getMealID(), this);
    }


    // Go back to previous screen
    public void clickBack(View view) {
        finish();
    }
    
    //toggles remove button's visibility on if the meal is offered or not
    public void showRemoveButton(){
        View button = findViewById(R.id.remove_btn);
        if (mealData.isOffered()) {
            button.setVisibility(View.GONE);
        }else{
            button.setVisibility(View.VISIBLE);
        }
    }

    // updates the screen with the information according to the meal that was
    // clicked on the previous screen
    @Override
    public void updateUI() {
        try {
            updateMealInfoScreen(mealData.getName(), mealData.getPrice(), mealData.getMealType(), mealData.getCuisineType(),
                    mealData.getIngredients(), mealData.getAllergens(), mealData.getDescription(), mealData.isOffered());
        } catch (Exception e) {
            Log.e("MealInfoScreen", "unable to create meal object :(");
            displayErrorToast("Unable to retrieve the meal info!");
        }
    }

    @Override
    public void showNextScreen() {

    }

    @Override
    public void dbOperationSuccessHandler(Object dbOperation, Object payload) {
        // if any DB operation is initiated on a meal, and it's a success, we update meals list to show current changes
        App.getAppInstance().getMealsListScreen().notifyDataChanged();
        // display success message
        displaySuccessToast((String) payload);

        // if operation was to delete the meal, close the meal info screen
        if (dbOperation == MealHandler.dbOperations.REMOVE_MEAL) {
            finish();
        }
    }

    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {
        // display error message
        displayErrorToast((String) payload);
    }

    /**
     * Updates the text on the meal info screen to its respective information
     * @param mealTitle
     * @param price
     * @param mealType
     * @param cuisineType
     * @param ingredients
     * @param allergens
     * @param description
     */
    public void updateMealInfoScreen(String mealTitle, double price, String mealType, String cuisineType,
                                     String ingredients, ArrayList<String> allergens, String description, boolean offered) {
        // sets the text for the meal name
        TextView mealNameText = (TextView) findViewById(R.id.title_of_meal);
        mealNameText.setText(mealTitle);

        // sets the text for price
        TextView priceText = (TextView) findViewById(R.id.price_of_meal);
        priceText.setText("$ " + String.valueOf(price));

        // sets the text for the meal type
        TextView mealTypeText = (TextView) findViewById(R.id.msg_type);
        mealTypeText.setText(mealType);

        // sets the text for cuisine type
        TextView cuisineText = (TextView) findViewById(R.id.msg_cuisine);
        cuisineText.setText(cuisineType);

        // sets the text for the ingredients
        TextView ingredientsText = (TextView) findViewById(R.id.msg_ingredients);
        ingredientsText.setText(ingredients);

        // sets the text for allergens
        TextView allergensText = (TextView) findViewById(R.id.msg_allergens);
        String allergensString = String.join(", ", allergens); // convert arraylist to string

        if (allergensString.length() == 0) {
            allergensText.setText("N/A");
        } else {
            allergensText.setText(allergensString);
        }

        // sets the text for description
        TextView descriptionText = (TextView) findViewById(R.id.msg_description);
        descriptionText.setText(description);

        // set the text for offering button
        Button offeringButton = (Button)findViewById(R.id.offering_btn);

        if (offered) { // meal is currently being offered
            offeringButton.setText("Unoffer Meal");
        } else { // meal is currently not being offered
            offeringButton.setText("Offer Meal");
        }

    }
}