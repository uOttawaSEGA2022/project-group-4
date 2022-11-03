package com.example.mealer_project.ui.screens;

import android.content.Intent;
import android.os.Bundle;
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
import java.util.List;
import java.util.Map;


public class AdminScreen extends UIScreen implements StatefulView {

    int numOfButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);

        ListView complaintList = findViewById(R.id.complaintList);
        List<String> list = new ArrayList<String>();

        list.add("Complaint 1");
        list.add("Complaint 2");
        list.add("Complaint 3");
        list.add("Complaint 4");
        list.add("Complaint 5");

        //Implementation required
        /**
        AdminInbox adminInbox = null;
        try {
            adminInbox = App.getAppInstance().getAdminInbox();
            System.out.println("SIZE: " + adminInbox.complaints.size());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        for(Map.Entry<String, Complaint> complaintSet: adminInbox.complaints.entrySet()) {
            Complaint complaintInformation = complaintSet.getValue();

            //ComplaintTitle
            list.add(complaintInformation.getTitle());
        }*/

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, list);
        complaintList.setAdapter(arrayAdapter);

        complaintList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(), ComplaintScreen.class));
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
}
