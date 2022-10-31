package com.example.mealer_project.ui.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.User;
import com.example.mealer_project.data.models.UserRoles;
import com.google.firebase.auth.FirebaseAuth;

import java.time.Instant;
import java.util.Date;


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

        // checks if User is Chef and is suspended
        if (currentUser.getRole() == UserRoles.CHEF) { //chef
            Chef chefUser = (Chef) currentUser; //casting to chef

            if (chefUser.getIsSuspended()) { //suspended
                showSuspensionMessage(chefUser.getSuspensionDate());
            } else { //show normal chef screen
                //implement this
            }

        }

    }

    public void clickLogout(View view) {
        Intent intent = new Intent(this, IntroScreen.class);
        startActivity(intent);
        //finish(); // change later to proper code
        FirebaseAuth.getInstance().signOut();
    }

    /**
     * this method shows the suspension message to the user
     * @param suspensionDate
     *  the end date of the suspension
     */
    private void showSuspensionMessage(Date suspensionDate) {

        // Variable declaration
        final long HUNDRED_YEARS = 3155692597470L;
        TextView editText = (TextView) findViewById(R.id.suspensionMsg);

        // Process: checking for finite or infinite ban
        if (suspensionDate.getTime() - new Date().getTime() > HUNDRED_YEARS) { //infinite

            // setting the text
            editText.setText("Your account has been permanently suspended.");
            editText.setVisibility(View.VISIBLE); //visible

        }
        else { //finite time

            // setting the text
            editText.setText("Your account has been temporarily suspended until " + suspensionDate.toString());
            editText.setVisibility(View.VISIBLE); //visible

        }

    }

}