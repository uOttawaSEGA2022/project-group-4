package com.example.mealer_project.ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.handlers.InboxHandler;
import com.example.mealer_project.data.models.User;
import com.example.mealer_project.data.models.inbox.AdminInbox;
import com.example.mealer_project.data.models.inbox.Complaint;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class AdminScreen extends UIScreen implements StatefulView {

    int numOfButtons;
    ListView complaintList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);

        App.getInboxHandler().updateAdminInbox(this);

        complaintList = findViewById(R.id.complaintList);
        complaintList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), ComplaintScreen.class));
            }
        });*/
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
            Log.e("NUMBER COMPLAINTS", String.valueOf(App.getAdminInbox().getComplaints().size()));

            List<String> complaintTitles = new ArrayList<String>();

            for (Complaint eachComplaint: App.getAdminInbox().getComplaints().values()) {
                complaintTitles.add(eachComplaint.getTitle());
            }


            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, complaintTitles);
            complaintList.setAdapter(arrayAdapter);
        } catch (Exception e) {
            Log.e("displayComplaints", "user doesn't have access");
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
