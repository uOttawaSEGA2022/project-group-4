package com.example.mealer_project.ui.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mealer_project.R;
import com.example.mealer_project.data.models.inbox.Complaint;

import java.util.Date;

public class MakeComplaint extends AppCompatActivity {
    Complaint complaint;
    EditText titleText;
    EditText descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_complaint);

        Button submitComplaint = findViewById(R.id.send_complaint_button);
        ImageButton backButton = findViewById(R.id.backButtonComplaint);
        titleText = (EditText) findViewById(R.id.title_of_complaint);
        descriptionText = (EditText) findViewById(R.id.complaint_writer);

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


    }

    private void sendComplaint(){
        Date date = null;
        String title = titleText.getText().toString();
        String description = descriptionText.getText().toString();


        //complaint = new Complaint("id", title, description, "clientID", "ChefID", date);
    }
}