package com.example.mealer_project.ui.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mealer_project.R;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.data.models.inbox.Complaint;
import com.example.mealer_project.data.models.meals.Meal;

import java.util.Date;

public class MakeComplaint extends AppCompatActivity {
    Order orderData;
    Complaint complaint;
    EditText titleText;
    EditText descriptionText;
    TextView clientName;
    TextView chefName;
    TextView dateOfOrder;
    Button submitComplaint;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_complaint);

        //Variables for displaying order information
        clientName = findViewById(R.id.client_name);
        chefName = findViewById(R.id.chef_name);

        //Variables for the user writing complaints
        titleText = (EditText) findViewById(R.id.title_of_complaint);
        descriptionText = (EditText) findViewById(R.id.complaint_writer);

        //Buttons
        submitComplaint = findViewById(R.id.send_complaint_button);
        backButton = findViewById(R.id.backButtonComplaint);

        submitComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleText.getText().toString().length() > 0 && descriptionText.getText().toString().length() > 0){
                    sendComplaint();
                }else {
                    Toast.makeText(getApplicationContext(), "Please fill in description and title!", Toast.LENGTH_LONG).show();
                }
            }
        });

        backButton.setOnClickListener(v -> finish());

        try {
            orderData = (Order) getIntent().getSerializableExtra("Test");
            setComplaintInformation(orderData.getClientInfo().getClientName(), orderData.getChefInfo().getChefName(), orderData.getOrderDate());
        } catch (Exception e) {
            Log.e("MakeComplaint", "unable to create order object :(");
            Toast.makeText(getApplicationContext(), "Unable to retrieve the meal info!", Toast.LENGTH_LONG).show();
        }


    }

    //Creates a complaint
    private void sendComplaint(){
        Date date = null;
        String title = titleText.getText().toString();
        String description = descriptionText.getText().toString();


        //complaint = new Complaint("id", title, description, "clientID", "ChefID", date);
    }

    //sets the values of the order information
    private void setComplaintInformation(String client, String chef, Date date){
        clientName.setText(client);
        chefName.setText(chef);
        dateOfOrder.setText(String.valueOf(date));
    }
}