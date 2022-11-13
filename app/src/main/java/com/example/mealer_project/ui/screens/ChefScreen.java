package com.example.mealer_project.ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentManager;

import com.example.mealer_project.R;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.google.firebase.auth.FirebaseAuth;

public class ChefScreen extends UIScreen implements StatefulView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_screen);

        attachOnClickListeners();

    }

    private void attachOnClickListeners() {

        // Variable Declaration
        Button menuButton = (Button) findViewById(R.id.menuButton);
        Button viewOrder = (Button) findViewById(R.id.viewOrdersButton);
        Button addButton = (Button) findViewById(R.id.addButton);

        menuButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewChef, MenuFragment.class, null) //goes to desired fragment. Different from intent()
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });

        viewOrder.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewChef, OrderFragment.class, null) //goes to desired fragment. Different from intent()
                        .setReorderingAllowed(true)
                        .addToBackStack("name")
                        .commit();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                showNextScreen(); //show new meal screen

            }

        });

    }

    public void clickLogout(View view) {
        Intent intent = new Intent(this, IntroScreen.class);
        startActivity(intent);
        //finish(); // change later to proper code
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void updateUI() {

    }

    /**
     * this method goes to the add new meal screen
     */
    @Override
    public void showNextScreen() {

        Intent intent = new Intent(getApplicationContext(), NewMealScreen.class);
        startActivity(intent);

    }

    @Override
    public void dbOperationSuccessHandler(Object dbOperation, Object payload) {

    }

    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {

    }
}
