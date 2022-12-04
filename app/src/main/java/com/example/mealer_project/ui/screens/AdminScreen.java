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
    // DB operations performed by AdminScreen
    public enum dbOperations {
      GET_COMPLAINTS
    };

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
        // reload complaints anytime update UI is called
        displayComplaints();
    }

    @Override
    public void showNextScreen() {}

    public void clickLogout(View view) {
        // handle user logout
        App.getAppInstance().logoutUser();
        // take user back to intro screen
        Intent intent = new Intent(this, IntroScreen.class);
        startActivity(intent);
    }

    private void displayComplaints() {
        try {
            complaintsData = App.getAdminInbox().getListOfComplaints();

            Log.e("NUMBER COMPLAINTS", String.valueOf(complaintsData.size()));

            List<String> complaintTitles = new ArrayList<String>();

            for (Complaint eachComplaint: complaintsData) {
                complaintTitles.add(eachComplaint.getTitle());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.text_view, complaintTitles);
            complaintListView.setAdapter(arrayAdapter);
        } catch (Exception e) {
            Log.e("displayComplaints", "unable to display complaints: " + e.getMessage());
        }

    }

    @Override
    public void dbOperationSuccessHandler(Object dbOperation, Object payload) {
        if (dbOperation == dbOperations.GET_COMPLAINTS) {
            updateUI();
            displaySuccessToast((String) payload);
        }
    }

    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {
        displayErrorToast((String) payload);
    }

}
