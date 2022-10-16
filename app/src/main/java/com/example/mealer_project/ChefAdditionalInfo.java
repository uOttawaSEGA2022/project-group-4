package com.example.mealer_project;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


public class ChefAdditionalInfo extends AppCompatActivity {

    ImageView imageView;
    //Button variables used for taking picture of cheque and going to next screen
    ImageButton imageButton;
    Button submitChequeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_additional_info);

        //set the values for the buttons
        imageView = findViewById(R.id.voidCheque);
        imageButton = findViewById(R.id.imageButton);
        submitChequeButton = findViewById(R.id.submitChequeButton);

        //Request for camera permission
        if (ContextCompat.checkSelfPermission(ChefAdditionalInfo.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ChefAdditionalInfo.this, new String[]{
                    Manifest.permission.CAMERA
            },100);
        }

        //Declare the action of what the take picture button will do
        imageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 100);
            }
        });

        //Declares the action to get to welcome screen
        submitChequeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChefAdditionalInfo.this, WelcomeScreen.class));
            }
        });
    }

    //Allows the image to appear on the app
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }
}