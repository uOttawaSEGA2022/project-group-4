package com.example.mealer_project.ui.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mealer_project.R;

public class MealInfoScreen extends AppCompatActivity {

    // key to pass meal's information through intent
    public final static String MEAL_DATA_ARG_KEY = "MEAL_DATA_ARG_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_info_screen);
    }
}