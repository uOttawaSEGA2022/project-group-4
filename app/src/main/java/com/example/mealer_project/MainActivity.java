package com.example.mealer_project;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.example.mealer_project.ui.signup.SignupActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);
    }

    public void OnSetSignUp(View view) {

        // Variable Declaration
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class); //where SignUp.class is the sign up activity
        startActivityForResult (intent,0); //fix this after
    }

    public void OnSetLogIn(View view) {

        // Variable Declaration
        Intent intent = new Intent(getApplicationContext(), Login_Screen.class); //where LogIn.class is the login activity
        startActivityForResult (intent,0); //fix this after
    }
}