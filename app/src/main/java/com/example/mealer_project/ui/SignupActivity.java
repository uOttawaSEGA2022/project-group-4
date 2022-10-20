package com.example.mealer_project.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.entity_models.AddressEntityModel;
import com.example.mealer_project.data.entity_models.CreditCardEntityModel;
import com.example.mealer_project.data.entity_models.UserEntityModel;
import com.example.mealer_project.data.models.UserRoles;
import com.example.mealer_project.utils.Response;
import com.example.mealer_project.utils.Result;


public class SignupActivity extends AppCompatActivity {
    LinearLayout clientSpecificInfo;
    LinearLayout chefSpecificInfo;
    UserRoles userRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // instantiate signup handler
//        signupHandler = new SignupHandler();

        // attach onClick handlers to buttons
        attachOnClickListeners();
        // instantiate linear layouts need for user type selection
        clientSpecificInfo = (LinearLayout) findViewById(R.id.clientSpecificInfoContainer);
        chefSpecificInfo = (LinearLayout) findViewById(R.id.chefSpecificInfoContainer);
    }

    private void attachOnClickListeners() {

        // Sign up form submit handler
        Button signupSubmitBtn = (Button) findViewById(R.id.signupFormSubmitBtn);
        signupSubmitBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                signupFormSubmissionHandler(v);
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
                Intent intent = new Intent(getApplicationContext(), ChefAdditionalInfo.class); //where SignUp.class is the sign up activity
                startActivityForResult (intent,0);
            }
        });
    }

    private void signupFormSubmissionHandler(View v) {

        // check confirm password is correct
        if (!checkConfirmPasswordMatches()) {
            displayErrorToast("Passwords do not match. Try again");
            return;
        }

        // pass all sign up form details to controller
        Response response = signUpSubmitButtonClickHandler();
        if (response.isSuccess()) {
            displaySuccessToast(response.getSuccessMessage());
        } else {
            displayErrorToast(response.getErrorMessage());
        }

    }

    private void setUserTypeClient() {
        userRole = UserRoles.CLIENT;
        clientSpecificInfo.setVisibility(View.VISIBLE);
        chefSpecificInfo.setVisibility(View.GONE);
    }

    private void setUserTypeChef() {
        userRole = UserRoles.CHEF;
        clientSpecificInfo.setVisibility(View.GONE); // takes it out of the view
        chefSpecificInfo.setVisibility(View.VISIBLE);
    }

    private boolean checkConfirmPasswordMatches() {
        EditText textPassword = (EditText)findViewById(R.id.signupPassword);
        String password = textPassword.getText().toString();

        EditText textConfirmPassword = (EditText)findViewById(R.id.signupConfirmPassword);
        String confirmPassword = textConfirmPassword.getText().toString();

        return password.equals(confirmPassword);
    }

    // logic for handling sign up submission
    public Response signUpSubmitButtonClickHandler() {

        UserEntityModel userEntityModel = getUserEntityModel();

        // registering a new Client user
        if (userRole == UserRoles.CLIENT) {
            // first try to create credit card entity model (to collect credit card data) [performs some validation]
            // get a Result object back as representation of results of creating credit card entity model
            Result<CreditCardEntityModel, String> creditCardEntityCreation;
            try {
                creditCardEntityCreation = getCreditCardEntityModel();
            } catch (Exception e) {
                return new Response(false, "Form submitted with invalid or no Credit Card data, please enter correct details and try again!" + e.getMessage());
            }

            if (creditCardEntityCreation.isSuccess()) {
                // register the new user by passing data to UserDataHandler of the app instance
                Response userRegistrationResponse = App.getUserDataHandler().registerClient(this, userEntityModel, creditCardEntityCreation.getSuccessObject());
                if (userRegistrationResponse.isSuccess()) {


                    return new Response(true, userRegistrationResponse.getSuccessMessage());
                } else {
                    return new Response(false, userRegistrationResponse.getErrorMessage());
                }

            } else {
                // if failed to create credit card entity due to data validation error
                return new Response(false, creditCardEntityCreation.getErrorObject());
            }
        } else if (userRole == UserRoles.CHEF){
            // registering a new chef
            // get short description
            EditText chefShortDesc = (EditText)findViewById(R.id.signupChefShortDescription);
            String chefShortDescription = chefShortDesc.getText().toString();

            // TO-DO: to be implemented. Temporarily empty string
            String voidCheque = "";

            // register the new user by passing data to UserDataHandler of the app instance
            Response userRegistrationResponse = App.getUserDataHandler().registerChef(this, userEntityModel, chefShortDescription, voidCheque);
            if (userRegistrationResponse.isSuccess()) {
                return new Response(true, userRegistrationResponse.getSuccessMessage());
            } else {
                return new Response(false, userRegistrationResponse.getErrorMessage());
            }
        } else {
            return new Response(false, "Invalid user role!");
        }

    }

    public void showNextScreen() {
        Intent intent = new Intent(getApplicationContext(), WelcomeScreen.class); //where SignUp.class is the sign up activity
        startActivity(intent);
    }
    public void userRegistrationFailed(String message) { displayErrorToast(message);
    }

    private UserEntityModel getUserEntityModel () {
        UserEntityModel user = new UserEntityModel();

        AddressEntityModel userAddress = new AddressEntityModel();

        // Get text from text fields
        EditText textFirst = (EditText)findViewById(R.id.signupFirstName);
        user.setFirstName(textFirst.getText().toString());

        EditText textLast = (EditText)findViewById(R.id.signupLastName);
        user.setLastName(textLast.getText().toString());

        EditText textEmail = (EditText)findViewById(R.id.signupEmailAddress);
        user.setEmail(textEmail.getText().toString());

        EditText textPassword = (EditText)findViewById(R.id.signupPassword);
        user.setPassword(textPassword.getText().toString());

        EditText textAddress = (EditText)findViewById(R.id.signupStreetAddress);
        userAddress.setStreetAddress(textAddress.getText().toString());

        EditText textCity = (EditText)findViewById(R.id.signupCity);
        userAddress.setCity(textCity.getText().toString());

        EditText textPostalCode = (EditText)findViewById(R.id.signupPincode);
        userAddress.setPostalCode(textPostalCode.getText().toString());

        EditText textCountry = (EditText)findViewById(R.id.signupCountry);
        userAddress.setCountry(textCountry.getText().toString());

        user.setAddress(userAddress);

        return user;
    }




    public Result<CreditCardEntityModel, String> getCreditCardEntityModel() {

        CreditCardEntityModel creditCard = new CreditCardEntityModel();

        EditText textCardBrand = (EditText)findViewById(R.id.signupAssociatedBrand);
        creditCard.setBrand(textCardBrand.getText().toString());

        EditText textCardNumber = (EditText)findViewById(R.id.signupCreditCardNumber);
        creditCard.setNumber(textCardNumber.getText().toString());

        EditText textCardName = (EditText)findViewById(R.id.signupCreditCardName);
        creditCard.setName(textCardName.getText().toString());

        // holder for validating integer data going forward
        int val;

        EditText textCardMonth = (EditText)findViewById(R.id.signupCreditCardMonth);
        // try parsing the user input to integer
        try {
            val = Integer.parseInt(textCardMonth.getText().toString());
        } catch (Exception e) {
            // return an appropriate error if fails
            return new Result<>(null, "Invalid Expiry Month value: " + e.getMessage());
        }
        // if parsing successful, add info to creditCard instance
        creditCard.setExpiryMonth(val);

        EditText textCardYear = (EditText)findViewById(R.id.signupCreditCardYear);
        try {
            val = Integer.parseInt(textCardYear.getText().toString());
        } catch (Exception e) {
            return new Result<>(null, "Invalid Expiry Month value: " + e.getMessage());
        }
        creditCard.setExpiryMonth(val);

        EditText textCardCVC = (EditText)findViewById(R.id.signupCreditCardCVC);
        try {
            val = Integer.parseInt(textCardCVC.getText().toString());
        } catch (Exception e) {
            return new Result<>(null, "Invalid CVC value: " + e.getMessage());
        }
        creditCard.setCvc(val);

        // return Result containing creditCard instance and no error object
        return new Result<>(creditCard, null);
    }

    private void displaySuccessToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void displayErrorToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    public void goBack(View view){
        Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
        startActivityForResult (intent,0);
    }

    public String getChefDescription() {
        EditText textDescription = (EditText)findViewById(R.id.signupChefShortDescription);
        String description = textDescription.getText().toString();
        return description;
    }
}