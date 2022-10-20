package com.example.mealer_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        // Getting the user's information on Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String name = user.getDisplayName(); // gives entire name of user

        // Change text to proper welcome message when opened
        String role = "wowowowo";
        String name = "plssss";
        TextView editText = (TextView) findViewById(R.id.welcome_message);
        editText.setText("Welcome " + role + " " +  name + "!", TextView.BufferType.EDITABLE);
    }

    public void clickLogout (View view) {
        Intent intent = new Intent (this, IntroScreen.class);
        startActivity(intent);
        FirebaseAuth.getInstance().signOut();
    }
}