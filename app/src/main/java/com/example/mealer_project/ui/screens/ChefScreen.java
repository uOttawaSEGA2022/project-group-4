package com.example.mealer_project.ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.User;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.example.mealer_project.ui.screens.meals.MealsListScreen;
import com.google.firebase.auth.FirebaseAuth;

public class ChefScreen extends UIScreen implements StatefulView {

    TextView editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_screen);

        editText = (TextView) findViewById(R.id.welcome_message_chef);

        attachOnClickListeners();

        User currentUser = App.getAppInstance().getUser();

        // Change text to proper welcome message when opened
        if (App.getAppInstance().isUserAuthenticated()) {
            setWelcomeMessage("Welcome " + currentUser.getFirstName() + " " + currentUser.getLastName() + ", you're logged in as a CHEF!");
        }

    }

    private void setWelcomeMessage(String message) {
        editText.setText(message, TextView.BufferType.EDITABLE);
    }

    private void attachOnClickListeners() {

        // Meal buttons
        Button menuButton = (Button) findViewById(R.id.viewMenuButton);
        Button offeredMealsButton = (Button) findViewById(R.id.viewOfferedButton);
        Button addButton = (Button) findViewById(R.id.addMealButton);
        // Order buttons
        Button viewOrder = (Button) findViewById(R.id.viewOrdersButton);
        Button viewPendingOrder = (Button) findViewById(R.id.viewPendingOrdersButton);


        menuButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MealsListScreen.class));
            }
        });

        offeredMealsButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // initialize a new intent
                Intent intent = new Intent(getApplicationContext(), MealsListScreen.class);
                // specify that we want to display offered meals
                intent.putExtra(MealsListScreen.MEALS_TYPE_ARG_KEY, MealsListScreen.MEALS_TYPE.OFFERED_MEALS.toString());
                // display the offered meals list
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NewMealScreen.class)); //show new meal screen
            }
        });

        viewOrder.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CompletedOrdersScreen.class)); //show completed orders
            }
        });



        viewPendingOrder.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PendingOrdersScreen.class)); //show pending orders
            }
        });

    }

    public void clickLogout(View view) {
        // handle user logout
        App.getAppInstance().logoutUser();
        // take user back to intro screen
        Intent intent = new Intent(this, IntroScreen.class);
        startActivity(intent);
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void showNextScreen() {

    }

    @Override
    public void dbOperationSuccessHandler(Object dbOperation, Object payload) {

    }

    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {

    }
}