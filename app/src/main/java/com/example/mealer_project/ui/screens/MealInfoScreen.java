package com.example.mealer_project.ui.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mealer_project.R;
import com.example.mealer_project.ui.screens.meals.MealsListScreen;

public class MealInfoScreen extends AppCompatActivity {

    // key to pass meal's information through intent
    public final static String MEAL_DATA_ARG_KEY = "MEAL_DATA_ARG_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_info_screen);
    }

    // Go back to screen with all meals
    public void clickBack(View view) {
        Intent intent = new Intent(this, MealsListScreen.class);
        startActivity(intent);
    }

    // Go to edit meal screen
    public void clickEdit(View view) {
        Intent intent = new Intent(this, EditMealScreen.class);
        startActivity(intent);
    }
}