package com.example.mealer_project.ui.screens.meals;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.ui.core.UIScreen;

import java.util.ArrayList;
import java.util.List;

public class MealsListScreen extends UIScreen {

    // list to store meals data
    private List<Meal> mealsData;
    private List<Meal> meals;
    // key to specify type of meals data being displayed
    public final static String MEALS_TYPE_ARG_KEY = "MEALS_TYPE_ARG_KEY";
    // key to provide custom list of meals through intent
    public final static String MEALS_DATA_ARG_KEY = "MEALS_DATA_ARG_KEY";
    // meals adapter
    private MealsAdapter mealsAdapter;

    // Defining an enum to describe type of meals data this view can display
    public enum MEALS_TYPE {
      OFFERED_MEALS,
      CUSTOM
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_list_screen);

        // check the meals data to be loaded
        loadMealsData();

        // initialize view data holder
        meals = new ArrayList<>();

        // populate meals listView
        populateMealsList();

        App.getAppInstance().setMealsListScreen(this);
    }

    // Go back to previous screen
    public void clickBack(View view) {
        finish();
    }

    private void loadMealsData() {
        // check if have intent data and there is a property specified for meal type
        if (getIntent() != null && getIntent().getStringExtra(MEALS_TYPE_ARG_KEY) != null) {

            String mealType = getIntent().getStringExtra(MEALS_TYPE_ARG_KEY);

            // load offered meals for a logged in Chef
            if (mealType.equals(MEALS_TYPE.OFFERED_MEALS.toString())) {
                loadLoggedInChefsOfferedMeals();
            }

            // load custom meals provided in the intent
            else if (mealType.equals(MEALS_TYPE.CUSTOM.toString())) {
                retrieveCustomMealsData();
            }

            // load all meals of logged in chef
            else {
                loadLoggedInChefsMeals();
            }
        } else {
            // if nothing provided in intent, display all meals by default if chef is logged in
            loadLoggedInChefsMeals();
        }
    }

    private void retrieveCustomMealsData() {
        // get list of meals from the intent
        try {
            // check if we have meals
            if (getIntent().getSerializableExtra(MEALS_DATA_ARG_KEY) != null) {
                // retrieve list of meals (if throws exception, means data is invalid and we handle it)
                this.mealsData = (List<Meal>) getIntent().getSerializableExtra(MEALS_DATA_ARG_KEY);
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
            this.mealsData = ((Chef) App.getUser()).MEALS.getListOfMeals();
        } else {
            Log.e("MealsListScreen", "No meals provided in intent, and no chef logged in");
            displayErrorToast("No meals available to be displayed");
        }
    }

    private void loadLoggedInChefsOfferedMeals() {
        // check if the current logged in user is a Chef
        if (App.getUser() instanceof Chef) {
            // set meals data to be the list of meals of the Chef
            this.mealsData = ((Chef) App.getUser()).MEALS.getListOfOfferedMeals();
        } else {
            Log.e("MealsListScreen", "Can't show offered meal. Current logged in user is not a Chef");
            displayErrorToast("No offered meals available to be displayed");
        }
    }

    private void populateMealsList() {
        // get the meals list
        ListView mealsList = findViewById(R.id.mlMealsList);
        // get the adapter
        mealsAdapter = new MealsAdapter(this, R.layout.activity_meals_list_item, this.meals);
        // Attach the adapter to the meals listView
        mealsList.setAdapter(mealsAdapter);
        // add data to the adapter
        for (Meal meal: this.mealsData) {
            mealsAdapter.add(meal);
        }
    }

    public void notifyDataChanged() {
        // reload meals data
        loadLoggedInChefsMeals();
        this.meals = this.mealsData;
        Log.e("MealsUp", "Meals data updated");
        this.mealsAdapter.notifyDataSetChanged();
    }
}