package com.example.mealer_project.ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.mealer_project.R;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.google.firebase.auth.FirebaseAuth;

public class ClientScreen extends UIScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_screen);
    }

    public void clickLogout(View view) {
        Intent intent = new Intent(this, IntroScreen.class);
        startActivity(intent);
        //finish(); // change later to proper code
        FirebaseAuth.getInstance().signOut();
    }
}
