package com.example.mealer_project.ui.screens;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.inbox.Complaint;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;

import java.util.Calendar;

public class ComplaintScreen extends UIScreen implements StatefulView{
    private Button banButton;
    private DatePickerDialog datePickerDialog;
    DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_screen);
        banButton = findViewById(R.id.ban_chef);

        updateComplaintScreen();
    }

    @Override
    public void updateUI() {
    }


    /**
     * Updates the text on the complaint screen
     * @param title
     * @param client
     * @param chef
     * @param meal
     * @param description
     */
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
        TextView textMeal = (TextView)findViewById(R.id.mealComplaintName);
        textMeal.setText(meal);

        // sets the text for description
        TextView textDescription = (TextView)findViewById(R.id.complaintDescription);
        textDescription.setText(description);
    }

    @Override
    public void showNextScreen() {

    }

    public void openDatePicker(View view){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(ComplaintScreen.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }
}
