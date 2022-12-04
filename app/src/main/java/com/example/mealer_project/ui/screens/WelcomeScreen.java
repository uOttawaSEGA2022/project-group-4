package com.example.mealer_project.ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.User;
import com.example.mealer_project.ui.core.UIScreen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class WelcomeScreen extends UIScreen {

    TextView editText;
    public final static int INFINITE_YEAR = 9999;
    public final static String CHEF_SUSPENSION_DATE_KEY = "CHEF_SUSPENSION_DATE_KEY";
    public final static String CHEF_NAME_KEY = "CHEF_NAME_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome_screen);
        editText = (TextView) findViewById(R.id.welcome_message_chef);

        // First handle a suspended chef logging in, since in that case no retrieval of additional info is required
        // if we have a value for chef suspension date to be displayed - means Chef is suspended
        if (getIntent() != null & getIntent().getExtras() != null && getIntent().getExtras().get(CHEF_SUSPENSION_DATE_KEY) != null) {
            setWelcomeMessage("Welcome Chef " + getIntent().getExtras().get(CHEF_NAME_KEY)  + "!");
            showSuspensionMessage(String.valueOf(getIntent().getExtras().get(CHEF_SUSPENSION_DATE_KEY)));

            // no need to do anything else
            return;
        }

        User currentUser = App.getAppInstance().getUser();

        // Change text to proper welcome message when opened
        if (App.getAppInstance().isUserAuthenticated()) {
            setWelcomeMessage("Welcome " + currentUser.getRole() + ", " + currentUser.getFirstName() + " " + currentUser.getLastName() + "!");
        }

        // uncomment when testing orders
        testOrder();

    }

    /**
     * Sets the welcome message in the header
     * @param message
     */
    private void setWelcomeMessage(String message) {
        editText.setText(message, TextView.BufferType.EDITABLE);
    }

    /**
     * Handles the logout action
     * @param view
     */
    public void clickLogout(View view) {
        // handle user logout
        App.getAppInstance().logoutUser();
        // take user back to intro screen
        Intent intent = new Intent(this, IntroScreen.class);
        startActivity(intent);
    }

    /**
     * this method shows the suspension message to the user
     * @param suspensionDateValue String value representing Chef's suspension date
     *  the end date of the suspension
     */
    private void showSuspensionMessage(String suspensionDateValue) {

        // try to parse suspension date
        try {
            Date suspensionDate = new SimpleDateFormat("MM/dd/yyyy").parse(suspensionDateValue);
            // Variable declaration
            TextView editText = (TextView) findViewById(R.id.suspensionMsg);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(suspensionDate);

            // Process: checking for finite or infinite ban
            if (calendar.get(Calendar.YEAR) == INFINITE_YEAR) { //infinite
                // setting the text
                editText.setText(R.string.chef_permanent_ban_message);
                editText.setVisibility(View.VISIBLE); //visible

            }
            else { //finite time

                App.getUserHandler().updateChefSuspension((Chef) App.getAppInstance().getUser());

                if (((Chef) App.getAppInstance().getUser()).getIsSuspended()) { //suspended

                    // var declaration: date formatter
                    SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.US);

                    // setting the text
                    editText.setText(String.format("%s%s.", getString(R.string.chef_temp_ban_message) + " ", formatter.format(((Chef) App.getAppInstance().getUser()).getSuspensionDate())));
                    editText.setVisibility(View.VISIBLE); //visible

                }
                else { //no longer suspended

                    // returning to chef homescreen
                    Intent intent = new Intent(getApplicationContext(), ChefScreen.class);
                    startActivity(intent);

                }

            }
        } catch (ParseException e) {
            displayErrorToast("Unable to obtain valid suspension date for Chef");
            Log.e("showSuspensionMessage", "Unable to parse chef suspension date: " + e.getMessage());
        }
    }

    /**
     * Method used when testing orders
     */
    private void testOrder() {
        // if current user is client
        if (App.getClient() != null) {
            Button testOrderBtn = (Button) findViewById(R.id.testOrderBtn);
            testOrderBtn.setVisibility(View.VISIBLE);

            // test order
            testOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), TestOrdersActivity.class));
                }
            });
        }
    }

}