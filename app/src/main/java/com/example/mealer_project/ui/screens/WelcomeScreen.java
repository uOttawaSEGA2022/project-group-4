package com.example.mealer_project.ui.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.User;
import com.google.firebase.auth.FirebaseAuth;


public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User currentUser = App.getAppInstance().getUser();
        // Change text to proper welcome message when opened
        String message = "";
        if (App.getAppInstance().isUserAuthenticated()) {
            message = "Welcome " + currentUser.getRole() + ", " + currentUser.getFirstName() + " " + currentUser.getLastName() + "!";
        }

        setContentView(R.layout.activity_welcome_screen);
        TextView editText = (TextView) findViewById(R.id.welcome_message);
        editText.setText(message, TextView.BufferType.EDITABLE);


    }

    public void clickLogout (View view) {
        Intent intent = new Intent (this, IntroScreen.class);
        startActivity(intent);
        //finish(); // change later to proper code
        FirebaseAuth.getInstance().signOut();
    }
}