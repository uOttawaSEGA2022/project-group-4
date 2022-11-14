package com.example.mealer_project.ui.screens.meals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.ui.core.UIScreen;

import java.util.Collection;
import java.util.List;

public class MealsListScreen extends UIScreen {

    // list to store meals data
    private List<Meal> mealsData;
    // specify key to use for providing meals data through intent
    public final static String MEALS_DATA_ARG_KEY = "MEALS_DATA_ARG_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_list_screen);

        // get list of meals from the intent
        try {
            // check if something was indeed provided
            if (getIntent() != null) {
                // check if we have meals
                if (getIntent().getSerializableExtra(MEALS_DATA_ARG_KEY) != null) {
                    // retrieve list of meals (if throws exception, means data is invalid and we handle it)
                    mealsData = (List<Meal>) getIntent().getSerializableExtra(MEALS_DATA_ARG_KEY);
                } else {
                    loadLoggedInChefsMeals();
                }
            } else {
                loadLoggedInChefsMeals();
            }
        } catch (Exception e) {
            Log.e("MealsListScreen", "An exception occurred: " + e.getMessage());
            displayErrorToast("Unable to load meals");
        }
    }

    private void loadLoggedInChefsMeals() {
        // if no list of meals was provided, just display all meals of the Chef if a chef is logged in
        if (App.getUser() instanceof Chef) {
            // set meals data to be the list of meals of the Chef
            mealsData = ((Chef) App.getUser()).MEALS.getListOfMeals();
        } else {
            Log.e("MealsListScreen", "No meals provided in intent, and no chef logged in");
            displayErrorToast("No meals available to be displayed");
        }
    }

    private void populateMealsList() {
        // get the meals list
        ListView mealsList = findViewById(R.id.mlMealsList);
    }
}