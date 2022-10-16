package com.example.mealer_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class welcome_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        // Change text to proper welcome message when opened
        String role = "wowowowo";
        String name = "plssss";
        TextView editText = (TextView) findViewById(R.id.welcome_message);
        editText.setText("Welcome " + role + " " +  name + "!", TextView.BufferType.EDITABLE);
    }

    public void clickLogout (View view) {
        finish(); // change later to proper code
    }
}