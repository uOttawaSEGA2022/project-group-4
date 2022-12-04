package com.example.mealer_project.ui.screens;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.mealer_project.MainActivity;
import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.entity_models.AddressEntityModel;
import com.example.mealer_project.data.entity_models.CreditCardEntityModel;
import com.example.mealer_project.data.entity_models.UserEntityModel;
import com.example.mealer_project.data.models.User;
import com.example.mealer_project.data.models.UserRoles;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;
import com.example.mealer_project.utils.Response;
import com.example.mealer_project.utils.Result;


public class SignupScreen extends UIScreen implements StatefulView {
    UserRoles userRole;
    EditText cvcValue;
    Button selectUserTypeClientBtn;
    Button selectUserTypeChefBtn;
    LinearLayout clientSpecificInfo;
    LinearLayout chefSpecificInfo;
    boolean clientButtonClicked;
    boolean chefButtonClicked;
    boolean userRegistrationInProgress;
    String chequeString = null;
    // define states being observed
    enum observedStates {
        VOID_CHEQUE_IMAGE
    }
    // store void cheque image
    Bitmap voidChequeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // attach onClick handlers to buttons
        attachOnClickListeners();
        // instantiate variables needed for user type selection
        clientSpecificInfo = (LinearLayout) findViewById(R.id.clientSpecificInfoContainer);
        chefSpecificInfo = (LinearLayout) findViewById(R.id.chefSpecificInfoContainer);
        clientButtonClicked = false;
        chefButtonClicked = false;
        // add input filter to CVC form field to limit number of characters
        cvcValue = findViewById(R.id.signupCreditCardCVC);
        // to keep track of when user sign up completes on the Firebase
        // it gets set to true, when first time submit button it clicked
        // methods that the asynchronous firebase code calls backs after completing registration
        // sets this to false
        setRegistrationInProgress(false);
    }

    private void attachOnClickListeners() {

        // Sign up form submit handler
        Button signupSubmitBtn = (Button) findViewById(R.id.signupFormSubmitBtn);
        signupSubmitBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (!userRegistrationInProgress) {
                    // handle submission
                    signUpSubmitButtonClickHandler();
                } else {
                    displayErrorToast("Currently processing a sign-up request. Please wait, and try again.");
                }
            }
        });

        // handle displaying of conditional information (dependent on type of user)
        this.selectUserTypeClientBtn = (Button) findViewById(R.id.userTypeClientBtn);
        selectUserTypeClientBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                clientButtonClicked = true;
                updateUI();
            }
        });
        this.selectUserTypeChefBtn = (Button) findViewById(R.id.userTypeChefBtn);
        selectUserTypeChefBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                chefButtonClicked = true;
                updateUI();
            }
        });

        Button voidChequeBtnHandler = (Button) findViewById(R.id.signupChefVoidChequeBtn);
        voidChequeBtnHandler.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                startVoidChequeActivity.launch(new Intent(getApplicationContext(), VoidChequeScreen.class));
            }
        });

        ImageButton backButton = (ImageButton) findViewById(R.id.button_back2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    ActivityResultLauncher<Intent> startVoidChequeActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //Log.e("VoidChequeActivity", String.valueOf(result.getResultCode()));
                    if (result.getResultCode() == Activity.RESULT_OK) { // operation succeed (value = -1)
                        Intent intent = result.getData();
                        Log.e("intentCheck", String.valueOf(intent));
                        if (intent != null) {
                            updateVoidChequeImage(intent);
                            chequeString = intent.getStringExtra("voidChequeValue");
                            displaySuccessToast("Void cheque uploaded!");
                        } else {
                            Log.e("VoidChequeActivity", "intent null");
                            displayErrorToast("Unable to process void cheque image!");
                        }
                    }
                }
    });

    private void updateVoidChequeImage(Intent intent) {
        Object img = intent.getExtras().getParcelable(observedStates.VOID_CHEQUE_IMAGE.toString());
        try {
            if (img != null) {
                this.voidChequeImage = (Bitmap) img;
            } else {
                throw new NullPointerException("No image in intent for the key");
            }
        } catch (Exception e) {
            Log.e("updateVoidCheque", "failed to load image for void cheque. Error: " + e.getMessage());
            displayErrorToast("failed to load image for void cheque");
        }
    }

    /**
     * Centralised method to update UI on state changes as well as handle
     * side effects (one state change, causing UI changes at multiple spots)
     */
     public void updateUI() {
        // update user type selection
        if (clientButtonClicked || chefButtonClicked) {
            if (clientButtonClicked) {
                // set client
                setUserTypeClient();
                clientButtonClicked = false;
            } else {
                // set chef
                setUserTypeChef();
                chefButtonClicked = false;
            }
        }

        // if user signup in progress, display loading spinner
         if (userRegistrationInProgress) {
             // display loading spinner
             // displayLoadingSpinner();
         } else {
             // hide loading spinner
             //hideLoadingSpinner();
         }
    };

    private void setUserTypeClient() {
        // update client & chef buttons color
        // selectUserTypeClientBtn.setBackgroundColor(90648947);
        // selectUserTypeChefBtn.setBackgroundColor( 30648947);
        userRole = UserRoles.CLIENT;
        clientSpecificInfo.setVisibility(View.VISIBLE);
        chefSpecificInfo.setVisibility(View.GONE);
    }

    private void setUserTypeChef() {
        // update client & chef buttons color
        // selectUserTypeClientBtn.setBackgroundColor(30648947);
        // selectUserTypeChefBtn.setBackgroundColor( 90648947);
        userRole = UserRoles.CHEF;
        clientSpecificInfo.setVisibility(View.GONE); // takes it out of the view
        chefSpecificInfo.setVisibility(View.VISIBLE);
    }

    // logic for handling sign up submission
    public void signUpSubmitButtonClickHandler() {

        // set sign up in progress, user can't re-submit form for processing
        setRegistrationInProgress(true);

        // check confirm password is correct
        if (!checkConfirmPasswordMatches()) {
            setRegistrationInProgress(false);
            displayErrorToast("Passwords do not match. Try again.");
            return;
        }

        // pass all sign up form details to controller
        Response response = signupFormSubmissionHandler();
        if (response.isSuccess()) {
            displaySuccessToast(response.getSuccessMessage());
        } else {
            // setting below false let's user submit signup form again
            setRegistrationInProgress(false);
            displayErrorToast(response.getErrorMessage());
        }
    }

    private Response signupFormSubmissionHandler() {
        UserEntityModel userEntityModel = getUserEntityModel();

        // registering a new Client user
        if (userRole == UserRoles.CLIENT) {

            // check CVC
            Response cvcCheck = isCVCValid();
            if (cvcCheck.isError()) {
                return cvcCheck;
            }

            // first try to create credit card entity model (to collect credit card data) [performs some validation]
            // get a Result object back as representation of results of creating credit card entity model
            Result<CreditCardEntityModel, String> creditCardEntityCreation;
            try {
                creditCardEntityCreation = getCreditCardEntityModel();
            } catch (Exception e) {
                return new Response(false, "Please complete all credit card fields.");
            }

            if (creditCardEntityCreation.isSuccess()) {
                // register the new user by passing data to UserHandler of the app instance
                Response userRegistrationResponse = App.getUserHandler().registerClient(this, userEntityModel, creditCardEntityCreation.getSuccessObject());
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
            //Log.e("voidchequetest", chequeString);
            String voidCheque = chequeString; //(String) chequeString.get("String");

            //check void cheque submission
            Response voidChequeCheck = voidChequeUploaded();
            if (voidChequeCheck.isError()){
                return voidChequeCheck;
            }

            // register the new user by passing data to UserHandler of the app instance
            Response userRegistrationResponse = App.getUserHandler().registerChef(this, userEntityModel, chefShortDescription, voidCheque);
            if (userRegistrationResponse.isSuccess()) {
                return new Response(true, userRegistrationResponse.getSuccessMessage());
            } else {
                return new Response(false, userRegistrationResponse.getErrorMessage());
            }
        } else {
            return new Response(false, "Please select either Client or Chef.");
        }
    }

    /**
     * Validate user's password against the supplied password
     * @return True, if passwords match, else False
     */
    private boolean checkConfirmPasswordMatches() {
        EditText textPassword = (EditText)findViewById(R.id.signupPassword);
        String password = textPassword.getText().toString();

        EditText textConfirmPassword = (EditText)findViewById(R.id.signupConfirmPassword);
        String confirmPassword = textConfirmPassword.getText().toString();

        return password.equals(confirmPassword);
    }

    private Response isCVCValid() {
        String cvcNum = cvcValue.getText().toString();
        int numChars = cvcNum.toCharArray().length;
        if (numChars != 3) {
            return new Response(false, "Invalid CVC value. CVC must be 3 digit number between 000 - 999");
        }

        return new Response(true);
    }

    private Response voidChequeUploaded(){
        if (chequeString == null){
            return new Response(false, "Please upload a void cheque!");
        }
        return new Response(true);
    }

    // THIS NEEDS TO CHANGE
    /**
     * Method to display next screen - Welcome Screen
     * This method also sets userRegistrationInProgress to false to indicate user sign completed
     */
    public void showNextScreen() {

        // Variable Declaration
        User currentUser = App.getAppInstance().getUser();
        Intent intent;

        setRegistrationInProgress(false);

        if (currentUser.getRole() == UserRoles.CHEF){ //chef
            intent = new Intent(getApplicationContext(), ChefScreen.class);
            startActivity(intent);
        }
        else { //client
            intent = new Intent(getApplicationContext(), ClientScreen.class);
            startActivity(intent);
        }
    }

    /**
     * Method to handle success of a DB operation
     */
    @Override
    public void dbOperationSuccessHandler(Object dbOperation, Object payload) {

    }

    /**
     * Method to handle failure of a DB operation
     */
    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {

    }

    /**
     * Method to indicate signup failure
     * @param message error message
     */
    public void userRegistrationFailed(String message) {
        setRegistrationInProgress(false);
        displayErrorToast(message);
    }

    private void setRegistrationInProgress(boolean inProgress) {
        this.userRegistrationInProgress = inProgress;
        // hide or show loading spinner
        updateUI();
    }

    /**
     * Method to create a UserEntityModel for storing and transferring unvalidated User data
     * @return UserEntityModel containing information from signup form
     */
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

    /**
     * Method to create a CreditCardEntityModel for storing and transferring unvalidated credit card data
     * @return CreditCardEntityModel containing information from signup form
     */
    public Result<CreditCardEntityModel, String> getCreditCardEntityModel() {

        CreditCardEntityModel creditCard = new CreditCardEntityModel();

        EditText textCardBrand = (EditText)findViewById(R.id.signupAssociatedBrand);
        creditCard.setBrand(textCardBrand.getText().toString());

        EditText textCardNumber = (EditText)findViewById(R.id.signupCreditCardNumber);
        creditCard.setNumber(textCardNumber.getText().toString());

        EditText textCardName = (EditText)findViewById(R.id.signupCreditCardName);
        creditCard.setName(textCardName.getText().toString());

        EditText textCardMonth = (EditText)findViewById(R.id.signupCreditCardMonth);

        creditCard.setExpiryMonth(Integer.parseInt(textCardMonth.getText().toString()));

        EditText textCardYear = (EditText)findViewById(R.id.signupCreditCardYear);

        creditCard.setExpiryYear(Integer.parseInt(textCardYear.getText().toString()));

        EditText textCardCVC = (EditText)findViewById(R.id.signupCreditCardCVC);

        creditCard.setCvc(textCardCVC.getText().toString());

        // return Result containing creditCard instance and no error object
        return new Result<>(creditCard, null);
    }

    public String getChefDescription() {
        EditText textDescription = (EditText)findViewById(R.id.signupChefShortDescription);
        return textDescription.getText().toString();
    }
}