package com.example.mealer_project.ui.screens;

import android.os.Bundle;

import com.example.mealer_project.R;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;

public class ChefScreen extends UIScreen implements StatefulView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_screen);
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void showNextScreen() {

    }
}
