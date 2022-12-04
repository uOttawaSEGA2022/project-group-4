package com.example.mealer_project.ui.screens.meals;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.mealer_project.R;
import com.example.mealer_project.ui.core.UIScreen;

public class MealsSearchScreen extends UIScreen {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_meal_screen);

        attachOnClickListeners();

    }

    private void attachOnClickListeners() {

        //Header Buttons
        ImageButton backButton = (ImageButton) findViewById(R.id.back_btn3);
        ImageButton cartButton = (ImageButton) findViewById(R.id.cartBtn);

        backButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cartButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
