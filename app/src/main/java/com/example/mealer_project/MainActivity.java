package com.example.mealer_project;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.example.mealer_project.ui.screens.LoginScreen;
import com.example.mealer_project.ui.screens.SignupScreen;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);
    }

    /**
     * Button click takes to sign up screen
     * @param view
     */
    public void OnSetSignUp(View view) {

        // Variable Declaration
        Intent intent = new Intent(getApplicationContext(), SignupScreen.class); //where SignUp.class is the sign up activity
        startActivity(intent);
    }

    /**
     * Button click takes to login screen
     * @param view
     */
    public void OnSetLogIn(View view) {
        // Variable Declaration
        Intent intent = new Intent(getApplicationContext(), LoginScreen.class); //where LogIn.class is the login activity
        startActivity(intent);
    }
}