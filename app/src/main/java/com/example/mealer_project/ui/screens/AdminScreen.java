package com.example.mealer_project.ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.inbox.Complaint;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class AdminScreen extends UIScreen implements StatefulView {

    int numOfButtons;
    ListView complaintListView;
    List<Complaint> complaintsData;
    public final static String COMPLAINT_OBJ_INTENT_KEY = "complaint";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);

        App.getInboxHandler().updateAdminInbox(this);

        complaintListView = findViewById(R.id.complaintList);
        complaintListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // guard-clause
                if (complaintsData == null) {
                    Log.e("setOnItemClickListener", "complaintsData is null");
                    displayErrorToast("No complaints available");
                }

                // get the complaint
                Complaint complaint = null;

                try {

                    Log.e("fgf", "id: " + complaint);
                    Log.e("fgf", "pos: " + position);
                    Log.e("fgf", "val: " + complaintListView.getItemAtPosition(position));

                    complaint = complaintsData.get(position);

                    // complaint = complaints[pos]
                    Intent complaintScreenIntent = new Intent(getApplicationContext(), ComplaintScreen.class);
                    complaintScreenIntent.putExtra(COMPLAINT_OBJ_INTENT_KEY, complaint);
                    startActivity(complaintScreenIntent);
                } catch (Exception e) {
                    Log.e("setOnItemClickListener", "unable to create complaint object: " + e.getMessage());
                    displayErrorToast("Unable to process request!");
                }

            }
        });
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void showNextScreen() {

    }

    public void clickLogout(View view) {
        Intent intent = new Intent(this, IntroScreen.class);
        startActivity(intent);
        //finish(); // change later to proper code
        FirebaseAuth.getInstance().signOut();
    }

    private void displayComplaints() {
        try {
            complaintsData = App.getAdminInbox().getListOfComplaints();

            Log.e("NUMBER COMPLAINTS", String.valueOf(complaintsData.size()));

            List<String> complaintTitles = new ArrayList<String>();

            for (Complaint eachComplaint: complaintsData) {
                complaintTitles.add(eachComplaint.getTitle());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, complaintTitles);
            complaintListView.setAdapter(arrayAdapter);
        } catch (Exception e) {
            Log.e("displayComplaints", "unable to display complaints: " + e.getMessage());
        }

    }

    //Display complaints
    public void successLoadingAdminInbox() {
        displayComplaints();
        displaySuccessToast("Complaints loaded!");
    }

    public void failedToLoadComplaints(String s) {
        displayErrorToast(s);
    }
}
