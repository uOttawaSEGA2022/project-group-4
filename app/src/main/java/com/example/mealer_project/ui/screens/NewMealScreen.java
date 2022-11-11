package com.example.mealer_project.ui.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.app.AppInstance;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Meal;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;

import java.util.List;

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
    }

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
        String chefID = chef.getUserId();

        EditText mealName = (EditText) findViewById(R.id.meal_name);
        EditText cuisineType = (EditText) findViewById(R.id.cuisine_type);
        //EditText mealType = (EditText) findViewById(R.id.meal_type);
        EditText ingredients = (EditText) findViewById(R.id.ingredients);
        EditText description = (EditText) findViewById(R.id.description);
        EditText price = (EditText) findViewById(R.id.price);

        double priceValue;

        // Process: validating the price
        try {

            // Initialization
            priceValue = Double.parseDouble(price.getText().toString());

            //Meal meal = new Meal(mealName.getText().toString(), chefID, cuisineType.getText().toString(),
              //    mealType.getText().toString(), ingredients.getText().toString(), allergens,
                //  description.getText().toString(), offered, Double.parseDouble(price.getText().toString()));

            // Process: adding meal to chef's menu
            //chef.addMeal(meal);

        }
        catch(NumberFormatException e) {

            // error toast here

        }

    }

    @Override
    public void updateUI() {

    }

    @Override
    public void showNextScreen() {

    }
    
}