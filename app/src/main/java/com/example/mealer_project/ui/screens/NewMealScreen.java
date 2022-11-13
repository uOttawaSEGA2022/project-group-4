package com.example.mealer_project.ui.screens;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.widget.SwitchCompat;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.entity_models.MealEntityModel;
import com.example.mealer_project.data.handlers.MealHandler;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
public class NewMealScreen extends UIScreen implements StatefulView {

    // Variable Declaration
    protected String allergens = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meal_screen);

        /*
        Add information to spinner for meal type
         */
        Spinner spinner = (Spinner) findViewById(R.id.meal_type);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.menu_types_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // attach onClick handlers to buttons
        attachOnClickListeners();

    }

    /**
     * this method attaches the click methods to all the buttons
     */
    private void attachOnClickListeners() {

        // Variable Declaration
        Button addMealButton = (Button) findViewById(R.id.add_meal_button);
        Button backButton = (Button) findViewById(R.id.back_button);

        // Process: setting the new onClick method for addMeal
        addMealButton.setOnClickListener(new Button.OnClickListener() {

            /**
             * this method sets the on click for the add meal button
             * @param view
             *  the current view
             */
            @Override
            public void onClick(View view) {

                onAddMeal(view); //calling helper method to add the meal

            }

        });

        // Process: setting the new onClick method for the back button
        backButton.setOnClickListener(new Button.OnClickListener() {

            /**
             * this method sets the on click for the back button
             * @param view
             *  the current view
             */
            @Override
            public void onClick(View view) {

                cancelAddingMeal(); //stopping the activity
                showNextScreen(); //returning the previous screen

            }

        });

    }

    // Helper Methods for Adding the Meal------------------------------------------------------------------------
    /**
     * this method adds the selected allergens to the allergens list
     */
    private void addAllergensToList() {

        // Variable Declaration
        CheckBox currentCheck;

        // Process: adding allergens to list
        if (findViewById(R.id.gluten_checkbox).isSelected()) { //gluten

            // Initialization
            currentCheck = (CheckBox) findViewById(R.id.gluten_checkbox);

            allergens += ", " + currentCheck.getText().toString(); //adding to allergens String

        }
        if (findViewById(R.id.peanuts_checkbox).isSelected()) { //peanuts

            // Initialization
            currentCheck = (CheckBox) findViewById(R.id.peanuts_checkbox);

            allergens += ", " + currentCheck.getText().toString(); //adding to allergens String

        }
        if (findViewById(R.id.treenuts_checkbox).isSelected()) { //treenuts

            // Initialization
            currentCheck = (CheckBox) findViewById(R.id.treenuts_checkbox);

            allergens += ", " + currentCheck.getText().toString(); //adding to allergens String

        }
        if (findViewById(R.id.celery_checkbox).isSelected()) { //celery

            // Initialization
            currentCheck = (CheckBox) findViewById(R.id.celery_checkbox);

            allergens += ", " + currentCheck.getText().toString(); //adding to allergens String

        }
        if (findViewById(R.id.mustard_checkbox).isSelected()) { //mustard

            // Initialization
            currentCheck = (CheckBox) findViewById(R.id.mustard_checkbox);

            allergens += ", " + currentCheck.getText().toString(); //adding to allergens String

        }
        if (findViewById(R.id.eggs_checkbox).isSelected()) { //eggs

            // Initialization
            currentCheck = (CheckBox) findViewById(R.id.eggs_checkbox);

            allergens += ", " + currentCheck.getText().toString(); //adding to allergens String

        }
        if (findViewById(R.id.milk_checkbox).isSelected()) { //milk

            // Initialization
            currentCheck = (CheckBox) findViewById(R.id.milk_checkbox);

            allergens += ", " + currentCheck.getText().toString(); //adding to allergens String

        }
        if (findViewById(R.id.sesame_checkbox).isSelected()) { //sesame

            // Initialization
            currentCheck = (CheckBox) findViewById(R.id.sesame_checkbox);

            allergens += ", " + currentCheck.getText().toString(); //adding to allergens String

        }
        if (findViewById(R.id.fish_checkbox).isSelected()) { //fish

            // Initialization
            currentCheck = (CheckBox) findViewById(R.id.fish_checkbox);

            allergens += ", " + currentCheck.getText().toString(); //adding to allergens String

        }
        if (findViewById(R.id.crustaceans_checkbox).isSelected()) { //crustaceans

            // Initialization
            currentCheck = (CheckBox) findViewById(R.id.crustaceans_checkbox);

            allergens += ", " + currentCheck.getText().toString(); //adding to allergens String

        }
        if (findViewById(R.id.molluscs_checkbox).isSelected()) { //molluscs

            // Initialization
            currentCheck = (CheckBox) findViewById(R.id.molluscs_checkbox);

            allergens += ", " + currentCheck.getText().toString(); //adding to allergens String

        }
        if (findViewById(R.id.soya_checkbox).isSelected()) { //soya

            // Initialization
            currentCheck = (CheckBox) findViewById(R.id.soya_checkbox);

            allergens += ", " + currentCheck.getText().toString(); //adding to allergens String

        }
        if (findViewById(R.id.sulphites_checkbox).isSelected()) { //sulphites

            // Initialization
            currentCheck = (CheckBox) findViewById(R.id.sulphites_checkbox);

            allergens += ", " + currentCheck.getText().toString(); //adding to allergens String

        }
        if (findViewById(R.id.lupin_checkbox).isSelected()) { //lupin

            // Initialization
            currentCheck = (CheckBox) findViewById(R.id.lupin_checkbox);

            allergens += ", " + currentCheck.getText().toString(); //adding to allergens String

        }

    }

    /**
     * this method creates the new meal and adds it to the chef's menu
     * @param view
     *  the current view selected
     */
    public void onAddMeal(View view) {

        addAllergensToList(); //calling helper method to add all allergens

        // Variable Declaration
        Chef chef = (Chef) App.getAppInstance().getUser();
        String chefID = App.getUserId();

        EditText mealName = (EditText) findViewById(R.id.meal_name);
        EditText cuisineType = (EditText) findViewById(R.id.cuisine_type);
        Spinner mealType = (Spinner) findViewById(R.id.meal_type);
        EditText ingredients = (EditText) findViewById(R.id.ingredients);
        EditText description = (EditText) findViewById(R.id.description);
        EditText price = (EditText) findViewById(R.id.price);
        SwitchCompat offered = (SwitchCompat) findViewById((R.id.offer_meal_switch));

        double priceValue;

        // Process: validating the price
        try {

            // Initialization
            priceValue = Double.parseDouble(price.getText().toString());

            // Variable Declaration
            MealEntityModel mealEntityModel = new MealEntityModel(mealName.getText().toString(), chefID, cuisineType.getText().toString(),
                  mealType.getSelectedItem().toString(), ingredients.getText().toString(), allergens,
                  description.getText().toString(), offered.isChecked(), priceValue);


            App.MEAL_HANDLER.dispatch(MealHandler.dbOperations.ADD_MEAL, mealEntityModel, this); //calling add meal method from mealhandler

        }
        catch(NumberFormatException e) {

            // error toast here
            displayErrorToast("Incorrect price formatting!");

        }

    }

    /**
     * this helper method cancels the add meal action and ends the activity
     */
    private void cancelAddingMeal() {
        // finish the activity and return
        this.setResult(Activity.RESULT_CANCELED);
        this.finish();
    }

    // UI Methods-----------------------------------------------------------------------------------------------
    @Override
    public void updateUI() {}

    /**
     * this method returns the chef to the main screen
     */
    @Override
    public void showNextScreen() {

        // Variable Declaration
        Intent intent = new Intent(getApplicationContext(), ChefScreen.class);

        // Process: starting new intent
        startActivity(intent);

    }

    // Firebase Methods------------------------------------------------------------------------------------------
    @Override
    public void dbOperationSuccessHandler(Object dbOperation, Object payload) {
        if (dbOperation == MealHandler.dbOperations.ADD_MEAL) {
            // adding new meal completed
            displaySuccessToast((String) payload);
            // finish the activity and return
            this.setResult(Activity.RESULT_OK);
            this.finish();

            showNextScreen(); //returning to chef's main screen
        }
    }

    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {
        if (dbOperation == MealHandler.dbOperations.ADD_MEAL) {
            // failed adding a new meal
            displayErrorToast("Failed to add meal!");
        }
    }
    
}