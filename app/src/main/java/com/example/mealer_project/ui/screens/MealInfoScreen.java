package com.example.mealer_project.ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.example.mealer_project.ui.screens.meals.MealsAdapter;
import com.example.mealer_project.ui.screens.meals.MealsListScreen;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class MealInfoScreen extends UIScreen implements StatefulView {

    // button variables
    private Button editButton;
    private Button offeringButton;
    private Button removeButton;

    // the meal's information
    Meal mealData;
    double priceData;
    ArrayList<String> allergensData;

    // key to pass meal's information through intent
    public final static String MEAL_DATA_ARG_KEY = "MEAL_DATA_ARG_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_info_screen);

        editButton = (Button) findViewById(R.id.edit_btn);
        offeringButton = (Button) findViewById(R.id.offering_btn);
        removeButton = (Button) findViewById(R.id.remove_btn);

        // get meal data
        try {
            mealData = (Meal) getIntent().getSerializableExtra(MealsAdapter.MEALS_DATA_ARG_KEY);
            updateUI();
        } catch (Exception e) {
            Log.e("MealInfoScreen", "unable to create meal object :(");
            displayErrorToast("Unable to retrieve the meal info!");
        }
    }


    // Go back to previous screen
    public void clickBack(View view) {
        finish();
    }

    /**
    CHANGE TO ON CLICK LATER
     */
    // Go to edit meal screen
    public void clickEdit(View view) {
        Intent intent = new Intent(this, EditMealScreen.class);
        startActivity(intent);
    }


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

    }

    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {

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