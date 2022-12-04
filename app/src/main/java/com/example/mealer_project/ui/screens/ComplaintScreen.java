package com.example.mealer_project.ui.screens;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.handlers.UserHandler;
import com.example.mealer_project.data.models.inbox.Complaint;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.example.mealer_project.utils.Preconditions;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ComplaintScreen extends UIScreen implements StatefulView{
    private Button dismissButton;
    private Button banTempButton;
    private Button banPermButton;
    private ImageButton backButton;
    private DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener dateSetListener;
    Complaint complaintData;
    String[] clientAndChefNames;
    public final static String INFINITE_SUSPENSION_DATE = "01/01/9999";
    // store chef's suspension date if chef already suspended
    private String chefSuspensionDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_screen);
        dismissButton = (Button) findViewById(R.id.dismiss);
        banTempButton = (Button) findViewById(R.id.ban_chef);
        banPermButton = (Button) findViewById(R.id.ban_permament);
        backButton = (ImageButton) findViewById(R.id.cs_button_back);

        // get the complaint data
        try {
            complaintData = (Complaint) getIntent().getSerializableExtra(AdminScreen.COMPLAINT_OBJ_INTENT_KEY);
        } catch (Exception e) {
            Log.e("ComplaintScreen", "unable to create complaint object");
            displayErrorToast("Unable to display complaint!");
        }

        // initialize array to hold client and chef names
        clientAndChefNames = new String[2];

        // get client and chef user names
        getClientAndChefNames();

        banTempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(ComplaintScreen.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        banPermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suspendChef(INFINITE_SUSPENSION_DATE);
            }});

        dateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String suspendString = Integer.toString(month + 1) + "/" + Integer.toString(dayOfMonth) + "/" + Integer.toString(year);
                Date suspendDate = null;
                try {
                    suspendDate = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).parse(suspendString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                // suspend the chef
                suspendChef(suspendString);
            }
        };

        /**
         * On-Click Listener for Dismiss Button in Complaint Screen
         * When clicked, the complaint will be dismissed, and user will be redirected back to admin screen
         */
        dismissButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                App.getInboxHandler().removeComplaint(complaintData.getId()); //Remove complaint from primary database of Admin Inbox
                showNextScreen(); //Redirect to Admin Screen
            }
        });

        /**
         * Back button
         */
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // go to previous screen
                finish();
            }
        });
    }

    @Override
    public void updateUI() {
        // if we have now received both client and chef names
        if (
                Preconditions.isNotEmptyString(clientAndChefNames[0]) &&
                Preconditions.isNotEmptyString(clientAndChefNames[1])
        ) {
            try {
                updateComplaintScreen(complaintData.getTitle(), clientAndChefNames[0], clientAndChefNames[1], complaintData.getOrderId(), complaintData.getDescription());
            }catch (Exception e) {
                Log.e("ComplaintScreen", "unable to create complaint object");
                displayErrorToast("Unable to display complaint!");
            }
        }
    }

    /**
     * Updates the text on the complaint screen
     * @param title
     * @param client
     * @param chef
     * @param meal
     * @param description
     */
    @SuppressLint("ResourceAsColor")
    public void updateComplaintScreen(String title, String client, String chef, String meal, String description) {

        // sets the complaint header title
        TextView textTitle = (TextView)findViewById(R.id.complaintHeader);
        textTitle.setText(title);

        // sets the text for client name
        TextView textClient = (TextView)findViewById(R.id.clientComplaintName);
        textClient.setText(client);

        // sets the text for chef name
        TextView textChef = (TextView)findViewById(R.id.chefComplaintName);
        textChef.setText(chef);

        // sets the text for meal
        // TextView textMeal = (TextView)findViewById(R.id.mealComplaintName);
        // textMeal.setText(meal);

        // sets the text for description
        TextView textDescription = (TextView)findViewById(R.id.complaintDescription);
        textDescription.setText(description);

        // if chef is suspended display suspension date
        displayChefSuspensionDate();
    }

    // if chef is suspended, then show Admin the chef's suspension date
    private void displayChefSuspensionDate() {
        // if there is chef suspension date
        TextView csChefSuspension = (TextView) findViewById(R.id.csChefSuspensionInfo);
        if (this.chefSuspensionDate != null && !this.chefSuspensionDate.isEmpty() && !this.chefSuspensionDate.equalsIgnoreCase("null")) {
            String chefSuspended = "Chef is already suspended until: " + this.chefSuspensionDate;
            csChefSuspension.setText(chefSuspended);
            csChefSuspension.setVisibility(View.VISIBLE);
        } else {
            csChefSuspension.setVisibility(View.GONE);
        }
    }

    /**
     * This method creates a new activity with the updated admin screen & inbox
     */
    @Override
    public void showNextScreen() {
        startActivity(new Intent(getApplicationContext(), AdminScreen.class));
    }

    private void getClientAndChefNames() {

        // we need complaint data to retrieve chef and client names by ids
        if (complaintData == null) {
            Log.e("getClientAndChefNames", "complaintData is null");
            displayErrorToast("not a valid complaint data");
        }

        // send the request to get id
        App.getUserHandler().getClientAndChefNamesByIds(complaintData.getClientId(), complaintData.getChefId(), this);
    }

    /**
     * Method to handle success of a DB operation
     */
    @Override
    public void dbOperationSuccessHandler(Object dbOperation, Object payload) {
        if (dbOperation == UserHandler.dbOperations.GET_CLIENT_AND_CHEF_NAMES) {
            handleNamesRetrievalSuccess((String) payload);
        }

        if (dbOperation == UserHandler.dbOperations.GET_CHEF_SUSPENSION_DATE) {
            this.chefSuspensionDate = (String) payload;
        }
    };

    /**
     * Method to handle failure of a DB operation
     */
    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {
        if (dbOperation == UserHandler.dbOperations.GET_CLIENT_AND_CHEF_NAMES) {
            displayErrorToast((String) payload);
        }
    };

    public void handleNamesRetrievalSuccess(String name) {
        if (clientAndChefNames == null) {
            Log.e("handleNames", "clientAndChefNames is null");
            displayErrorToast("Unable to process request");
        }

        // if names array is empty, set client name
        if (!Preconditions.isNotEmptyString(clientAndChefNames[0])) {
            clientAndChefNames[0] = name;
        } else {
            // set chef's name
            clientAndChefNames[1] = name;
        }
    }

    private void suspendChef(String suspensionDate) {
        // suspend the chef
        App.getUserHandler().suspendChef(complaintData.getChefId(), suspensionDate);
        // remove the complaint
        App.getInboxHandler().removeComplaint(complaintData.getId());
        // take back to admin screen
        showNextScreen();
    }
}
