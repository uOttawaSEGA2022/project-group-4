package com.example.mealer_project.ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.handlers.MealHandler;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.example.mealer_project.ui.screens.search.SearchScreen;

public class ClientScreen extends UIScreen implements StatefulView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_screen);

        // initiate loading of searchable meals (so we have them before client goes to search screen)
        App.MEAL_HANDLER.dispatch(MealHandler.dbOperations.ADD_MEALS_TO_SEARCH_LIST, null, this);

        attachOnClickListeners();




    }

    private void attachOnClickListeners(){

        Button searchMealsBtn = (Button) findViewById(R.id.searchMealButton);
        searchMealsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchScreen.class));
            }
        });

        Button pendingOrdersBtn = (Button) findViewById(R.id.viewPendingOrdersButton);
        pendingOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PendingOrdersScreen.class));
            }
        });

        Button completedOrdersBtn = (Button) findViewById(R.id.viewCompletedOrdersButton);
        completedOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CompletedOrdersScreen.class));
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
    public void updateUI() {}

    @Override
    public void showNextScreen() {}

    /**
     * Method to handle success of a DB operation
     */
    @Override
    public void dbOperationSuccessHandler(Object dbOperation, Object payload) {
        displaySuccessToast((String) payload);
        Log.e("clientScreen", "DB op: " + dbOperation + " success");
    }

    /**
     * Method to handle failure of a DB operation
     */
    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {
        displayErrorToast((String) payload);
        Log.e("clientScreen", "DB op: " + dbOperation + " failed: " + payload);
    }
}
