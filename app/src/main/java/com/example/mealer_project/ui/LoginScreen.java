package com.example.mealer_project.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mealer_project.app.App;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.example.mealer_project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        Button loginBtn = (Button) findViewById(R.id.loginSubmitBtn);
        loginBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                EditText textEmail = (EditText)findViewById(R.id.loginUsername);
                String email = textEmail.getText().toString();
                if (email.equals("")) {
                    displayErrorToast("Invalid email address! Try again!");
                    return;
                }

                EditText textPassword = (EditText)findViewById(R.id.loginPassword);
                String password = textPassword.getText().toString();
                if (password.equals("")) {
                    displayErrorToast("Must provide a value for password! Try again!");
                    return;
                }

                login(email, password);
            }
        });
    }

    private void login(String email, String password) {
        // initiate login process
        App.getUserDataHandler().logInUser(this, email, password);
    }

    public void showNextScreen() {
        Intent intent = new Intent(getApplicationContext(), WelcomeScreen.class);
        startActivity(intent);
    }

    public void userLoginFailed(String message) {
        displayErrorToast(message);
    }

    private void displayErrorToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


}