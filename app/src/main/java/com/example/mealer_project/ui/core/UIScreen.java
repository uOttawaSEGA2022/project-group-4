package com.example.mealer_project.ui.core;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Superclass for most UI screens, containing methods with common implementation
 */
public class UIScreen extends AppCompatActivity {

    protected void displaySuccessToast(String message) {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void displayErrorToast(String message) {
        Toast.makeText(this.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

}
