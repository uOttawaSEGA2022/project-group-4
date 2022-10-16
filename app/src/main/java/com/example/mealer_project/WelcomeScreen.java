package com.example.mealer_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class WelcomeScreen extends AppCompatActivity {

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
        Intent intent = new Intent (this, IntroScreen.class);
        startActivity(intent);
        //finish(); // change later to proper code
    }
}