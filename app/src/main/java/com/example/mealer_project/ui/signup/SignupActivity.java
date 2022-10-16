package com.example.mealer_project.ui.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mealer_project.R;


public class SignupActivity extends AppCompatActivity {

    SignupController signupController;
    LinearLayout clientSpecificInfo;
    LinearLayout chefSpecificInfo;
    static private final String CLIENT = "CLIENT";
    static private final String CHEF = "CHEF";
    enum USER_TYPE {
            CLIENT,
            CHEF
    }
    USER_TYPE userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // create the controller instance
        signupController = new SignupController();
        // attach onClick handlers to buttons
        attachOnClickListeners();
        // instantiate linear layouts need for user type selection
        clientSpecificInfo = (LinearLayout) findViewById(R.id.clientSpecificInfoContainer);
        chefSpecificInfo = (LinearLayout) findViewById(R.id.chefSpecificInfoContainer);
    }

    private void attachOnClickListeners() {

        // Sign up form submit handler
        Button buttonOne = (Button) findViewById(R.id.signupFormSubmitBtn);
        buttonOne.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // pass all sign up form details to controller
                boolean signUpSuccess = signupController.signUpSubmitButtonClickHandler(v);
                // if sign up successful, go to Welcome screen
                if (signUpSuccess) {
                    setContentView(R.layout.activity_welcome_screen);
                } else {
                    // show error
                    System.out.println("######### Sign up failed ######### ");
                }
            }
        });

        // handle displaying of conditional information (dependent on type of user)
        Button selectUserTypeClientBtn = (Button) findViewById(R.id.userTypeClientBtn);
        selectUserTypeClientBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                setUserTypeClient();
            }
        });
        Button selectUserTypeChefBtn = (Button) findViewById(R.id.userTypeChefBtn);
        selectUserTypeChefBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                setUserTypeChef();
            }
        });

        Button voidChequeBtnHandler = (Button) findViewById(R.id.signupChefVoidChequeBtn);
        voidChequeBtnHandler.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                setContentView(R.layout.activity_chef_additional_info);
            }
        });
    }

    private void setUserTypeClient() {
        this.userRole = USER_TYPE.CLIENT;
        clientSpecificInfo.setVisibility(View.VISIBLE);
        chefSpecificInfo.setVisibility(View.GONE);
    }

    private void setUserTypeChef() {
        this.userRole = USER_TYPE.CHEF;
        clientSpecificInfo.setVisibility(View.GONE); // takes it out of the view
        chefSpecificInfo.setVisibility(View.VISIBLE);
    }




}