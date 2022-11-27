package com.example.mealer_project.ui.screens;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.mealer_project.R;
import com.example.mealer_project.ui.core.UIScreen;

public class VoidChequeScreen extends UIScreen {

    private boolean pictureSubmitted = false;
    private String voidChequeValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_additional_info);

        Button submitChequeButton = (Button) findViewById(R.id.submitCheque);
        ImageButton takePictureButton = (ImageButton) findViewById(R.id.imageButton);

//        takePictureButton.setOnClickListener(new Button.OnClickListener() {
//            public void onClick(View v) {
//                takePicture(v);
//            }
//        });

        submitChequeButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //submitVoidChequeImage(); // this is temporary (need to check if image uploaded, but not implemented yet)
                if (pictureSubmitted) {
                    submitVoidChequeImage();
                } else {
                    displayErrorToast("No void cheque image submitted! Please take a picture or upload one");
                }
            }
        });

        if (ContextCompat.checkSelfPermission(VoidChequeScreen.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(VoidChequeScreen.this, new String[]{
                    Manifest.permission.CAMERA
            }, 100);
        }

        // initially false
        //pictureSubmitted = false;

    }

    ActivityResultLauncher<Intent> startImageCaptureActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Log.e("ResultCode", String.valueOf(result.getResultCode()));
            if (result.getResultCode() == RESULT_OK) {
                Intent intent = result.getData();
                if (intent != null) {
                    updateVoidChequeImage(intent);
                } else {
                    Log.e("ImageCaptureActivity", "intent null");
                    displayErrorToast("Unable to process void cheque image!");
                }
            }
        }
    });

    public void takePicture(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startImageCaptureActivity.launch(intent);
    }

    private void updateVoidChequeImage(Intent data) {
        ImageView imageView = (ImageView) findViewById(R.id.voidCheque);
        imageView.setImageBitmap((Bitmap) data.getExtras().get("data"));
        voidChequeValue = String.valueOf(data.getExtras().get("data"));
        imageView.buildDrawingCache();
        Bitmap voidChequeImg = imageView.getDrawingCache();
        Bundle extras = new Bundle();
        extras.putParcelable(SignupScreen.observedStates.VOID_CHEQUE_IMAGE.toString(), voidChequeImg);
        extras.putString("String", voidChequeValue);
        this.getIntent().putExtras(extras);
        // indicate picture has been submitted
        pictureSubmitted = true;
    }

    private void submitVoidChequeImage() {
        this.setResult(Activity.RESULT_OK);
        this.getIntent().putExtra("voidChequeValue", voidChequeValue);
        Log.e("VoidChequeString", voidChequeValue);
        this.finish();
    }
}