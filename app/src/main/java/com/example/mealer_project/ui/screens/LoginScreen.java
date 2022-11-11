package com.example.mealer_project.ui.screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mealer_project.app.App;
import com.example.mealer_project.R;
import com.example.mealer_project.data.models.User;
import com.example.mealer_project.data.models.UserRoles;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.example.mealer_project.utils.Preconditions;
import com.example.mealer_project.utils.Response;

public class LoginScreen extends UIScreen implements StatefulView {
    boolean loginInProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        // variable to track if login is in progress, initially set to false
        setLoginInProcess(false);

        Button loginBtn = (Button) findViewById(R.id.loginSubmitBtn);
        loginBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                // if login already in progress, don't start process again
                if (loginInProcess) {
                    displayErrorToast("Login in process already! Please wait!");
                    return;
                }


                // handle login
                Response loginResponse = loginSubmissionHandler();
                if (loginResponse.isSuccess()) {
                    displaySuccessToast(loginResponse.getSuccessMessage());
                    // set login to be in process & display loading spinner
                    setLoginInProcess(true);
                } else {
                    displayErrorToast(loginResponse.getErrorMessage());
                    setLoginInProcess(false);
                }
            }
        });
    }

    private Response loginSubmissionHandler() {

        EditText textEmail = (EditText)findViewById(R.id.loginUsername);
        String email = textEmail.getText().toString();
        if (email.equals("")) {
            return new Response(false, "Please enter your email address");
        }

        EditText textPassword = (EditText)findViewById(R.id.loginPassword);
        String password = textPassword.getText().toString();
        if (password.equals("")) {
            return new Response(false, "Please enter your password");
        }

        // initiate login process (async process, no blocking return)
        App.getUserHandler().logInUser(this, email, password);

        return new Response(true, "Login submitted, please wait");
    }

    @Override
    public void showNextScreen() {
        // this method gets called when login completed
        User currentUser = App.getAppInstance().getUser();
        setLoginInProcess(false);

        if (currentUser.getRole() == UserRoles.ADMIN){
            Intent intent = new Intent(getApplicationContext(), AdminScreen.class);
            startActivity(intent);
        }else if (currentUser.getRole() == UserRoles.CHEF){
            Intent intent = new Intent(getApplicationContext(), ChefScreen.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(), ClientScreen.class);
            startActivity(intent);
        }
    }

    @Override
    public void updateUI() {
        if (loginInProcess) {
            // display loading spinner
        } else {
            // hide loading spinner
        }
    }

    /**
     * Method that the database calls for indicating failure in user login
     * @param message error message
     */
    public void userLoginFailed(String message) {
        // login process failed
        setLoginInProcess(false);
        displayErrorToast(message);
    }

    /**
     * Method to set login process status indicator
     * @param inProcess status of login process
     */
    private void setLoginInProcess(boolean inProcess) {
        this.loginInProcess = inProcess;
        updateUI();
    }

    /**
     * Method called when a Chef who logs in is currently suspended
     * @param chefSuspensionDate date until which chef is suspended
     * @param chefName name of the Chef for displaying message
     */
    public void handleSuspendedChefLogin(String chefSuspensionDate, String chefName) {

        setLoginInProcess(false);

        // if we have a valid suspension date
        if (Preconditions.isNotEmptyString(chefSuspensionDate)){
            // pass data to Welcome screen so a message can be shown
            Intent intent = new Intent(getApplicationContext(), WelcomeScreen.class);
            // set intent data
            intent.putExtra(WelcomeScreen.CHEF_SUSPENSION_DATE_KEY, chefSuspensionDate);
            intent.putExtra(WelcomeScreen.CHEF_NAME_KEY, chefName);
            startActivity(intent);
        } else {
            Log.e("handleSuspendedChef", "Received invalid suspension date");
           displayErrorToast("Cannot process login request");
        }
    }
}