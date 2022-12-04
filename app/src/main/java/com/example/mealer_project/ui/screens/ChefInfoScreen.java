package com.example.mealer_project.ui.screens;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Address;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;

import java.util.ArrayList;

public class ChefInfoScreen extends UIScreen implements StatefulView {

    ImageButton chefInfoBackBtn;
    Chef chefData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_info_screen);


        // buttons for onClick methods
        chefInfoBackBtn = (ImageButton) findViewById(R.id.button_back5);

        // on click method for back button
        chefInfoBackBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Try getting chef's data from previous screen
        try {
            //chefData = (Chef) getIntent().getSerializableExtra(CHEF_DATA_ARG_KEY);
            chefData = App.getChef();
            updateUI();
        } catch (Exception e) {
            Log.e("MealInfoScreen", "unable to create meal object :(");
            displayErrorToast("Unable to retrieve the meal info!");
        }

        // buttons for onClick methods
        chefInfoBackBtn = (ImageButton) findViewById(R.id.button_back5);

        // Process: calling helper method to attach listeners to all buttons
        attachOnClickListeners();
    }

    private void attachOnClickListeners(){
        // on click method for back button
        chefInfoBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            } // go to previous screen
        });
    }

    @Override
    public void updateUI() {
        try {
            updateChefInfoScreen(chefData.getFirstName()+" "+chefData.getLastName(), chefData.getEmail(), chefData.getAddress(), chefData.getChefRating(), chefData.getDescription(),
                    chefData.getNumOfMealsSold(), chefData.getIsSuspended());
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

    }

    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {

    }

    /** Updates the text on the meal info screen to its respective information
     * @param name
     * @param emailAddress
     * @param location
     * @param chefRating
     * @param description
     * @param totalMealsSold
     * @param isSuspended
     */
    public void updateChefInfoScreen(String name, String emailAddress, Address location, double chefRating,
                                     String description, int totalMealsSold, boolean isSuspended) {
        //Setting the text for the chef's information
        TextView chefNameText = (TextView) findViewById(R.id.chef_name_msg);
        chefNameText.setText(name);

        // sets the text for chef's email
        TextView emailText = (TextView) findViewById(R.id.chef_email_msg);
        emailText.setText(emailAddress);

        // sets the text for the location
        TextView locationText = (TextView) findViewById(R.id.chef_location_msg);
        locationText.setText(location.toString());

        // sets the text for chef's rating
        RatingBar ratingBar = (RatingBar) findViewById(R.id.chef_rating_msg);
        ratingBar.setRating((float) chefRating);
        ratingBar.setIsIndicator(true);

        // sets the text for the description
        TextView descriptionText = (TextView) findViewById(R.id.chef_description_msg);
        descriptionText.setText(description);

        // sets the text for cuisineType
        TextView mealsSoldText = (TextView) findViewById(R.id.chef_mealsSold_msg);
        mealsSoldText.setText(String.valueOf(totalMealsSold));

        // sets the text for chef's availability
        TextView availabilityText = (TextView) findViewById(R.id.chef_availability_msg);

        if (!isSuspended) { // Chef is currently available
            availabilityText.setText("Yes");
        } else { // Chef is currently not available
            availabilityText.setText("No");
        }

    }
}
