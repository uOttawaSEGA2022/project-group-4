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
    boolean userRegistrationInProgress;

    static EditText[] currentFields;
    static EditText[] currentFieldsCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // attach onClick handlers to buttons
        attachOnClickListeners();
        // instantiate linear layouts need for user type selection
        clientSpecificInfo = (LinearLayout) findViewById(R.id.clientSpecificInfoContainer);
        chefSpecificInfo = (LinearLayout) findViewById(R.id.chefSpecificInfoContainer);
        // to keep track of when user sign up completes on the Firebase
        // it gets set to true, when first time submit button it clicked
        // methods that the asynchronous firebase code calls backs after completing registration
        // sets this to false
        userRegistrationInProgress = false;

        currentFields = new EditText[]{(EditText) findViewById(R.id.signupFirstName),
                (EditText) findViewById(R.id.signupLastName),
                (EditText) findViewById(R.id.signupEmailAddress),
                (EditText) findViewById(R.id.signupPassword),
                (EditText) findViewById(R.id.signupStreetAddress),
                (EditText) findViewById(R.id.signupCity),
                (EditText) findViewById(R.id.signupPincode),
                (EditText) findViewById(R.id.signupCountry),
                (EditText)findViewById(R.id.signupAssociatedBrand),
                (EditText)findViewById(R.id.signupCreditCardNumber),
                (EditText)findViewById(R.id.signupCreditCardName),
                (EditText)findViewById(R.id.signupCreditCardMonth),
                (EditText)findViewById(R.id.signupCreditCardYear),
                (EditText)findViewById(R.id.signupCreditCardCVC)};

        currentFieldsCredit = new EditText[]{(EditText)findViewById(R.id.signupAssociatedBrand),
                (EditText)findViewById(R.id.signupCreditCardNumber),
                (EditText)findViewById(R.id.signupCreditCardName),
                (EditText)findViewById(R.id.signupCreditCardMonth),
                (EditText)findViewById(R.id.signupCreditCardYear),
                (EditText)findViewById(R.id.signupCreditCardCVC)};

    }

    private void attachOnClickListeners() {

        // Sign up form submit handler
        Button signupSubmitBtn = (Button) findViewById(R.id.signupFormSubmitBtn);
        signupSubmitBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                if (!userRegistrationInProgress) {
                    signupFormSubmissionHandler(v);
                } else {
                    displayErrorToast("Currently processing a sign-up request. Please wait, and try again.");
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
                Intent intent = new Intent(getApplicationContext(), ChefAdditionalInfo.class); //where SignUp.class is the sign up activity
                startActivityForResult (intent,0);
            }
        });
    }

    private void signupFormSubmissionHandler(View v) {

        // check confirm password is correct
        if (!checkConfirmPasswordMatches()) {
            EditText textPassword = (EditText)findViewById(R.id.signupPassword);
            textPassword.setBackgroundResource(R.drawable.edterror);
            textPassword.setError("Password doesn't match");

            EditText textConfirmPassword = (EditText)findViewById(R.id.signupConfirmPassword);
            textConfirmPassword.setBackgroundResource(R.drawable.edterror);
            textConfirmPassword.setError("Password doesn't match");

            displayErrorToast("Passwords do not match. Try again.");
            return;
        }

        EditText textPassword = (EditText)findViewById(R.id.signupPassword);
        textPassword.setBackgroundResource(R.drawable.edtnormal);
        textPassword.setError(null);

        EditText textConfirmPassword = (EditText)findViewById(R.id.signupConfirmPassword);
        textConfirmPassword.setBackgroundResource(R.drawable.edtnormal);
        textConfirmPassword.setError(null);

        userRegistrationInProgress = true;

        // pass all sign up form details to controller
        Response response = signUpSubmitButtonClickHandler();
        if (response.isSuccess()) {
            displaySuccessToast(response.getSuccessMessage());
        } else {
            userRegistrationInProgress = false;
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

                userRegistrationInProgress = false;
                return new Response(false, "Please complete all credit card fields.");

            }

            if (creditCardEntityCreation.isSuccess()) {
                // register the new user by passing data to UserDataHandler of the app instance
                Response userRegistrationResponse = App.getUserDataHandler().registerClient(this, userEntityModel, creditCardEntityCreation.getSuccessObject());
                if (userRegistrationResponse.isSuccess()) {
                    return new Response(true, userRegistrationResponse.getSuccessMessage());
                } else {
                    userRegistrationInProgress = false;
                    return new Response(false, userRegistrationResponse.getErrorMessage());
                }

            } else {
                userRegistrationInProgress = false;
                // if failed to create credit card entity due to data validation error
                return new Response(false, creditCardEntityCreation.getErrorObject());
            }
        } else if (userRole == UserRoles.CHEF) {
            // registering a new chef
            // get short description
            EditText chefShortDesc = (EditText) findViewById(R.id.signupChefShortDescription);
            String chefShortDescription = chefShortDesc.getText().toString();

            if (chefShortDescription == "") {
                chefShortDesc.setBackgroundResource(R.drawable.edterror);
                chefShortDesc.setError("Description cannot be empty");
            } else{
                chefShortDesc.setBackgroundResource(R.drawable.edtnormal);
                chefShortDesc.setError(null);
            }

            // TO-DO: to be implemented. Temporarily empty string
            String voidCheque = "";

            // register the new user by passing data to UserDataHandler of the app instance
            Response userRegistrationResponse = App.getUserDataHandler().registerChef(this, userEntityModel, chefShortDescription, voidCheque);
            if (userRegistrationResponse.isSuccess()) {
                return new Response(true, userRegistrationResponse.getSuccessMessage());
            } else {
                userRegistrationInProgress = false;
                return new Response(false, userRegistrationResponse.getErrorMessage());
            }
        } else {

            userRegistrationInProgress = false;
            return new Response(false, "Please select either Client or Chef.");
        }

    }

    public void showNextScreen() {
        userRegistrationInProgress = false;
        Intent intent = new Intent(getApplicationContext(), WelcomeScreen.class); //where SignUp.class is the sign up activity
        startActivity(intent);
    }
    public void userRegistrationFailed(String message) {
        userRegistrationInProgress = false;
        displayErrorToast(message);
    }

    private UserEntityModel getUserEntityModel () {
        UserEntityModel user = new UserEntityModel();

        AddressEntityModel userAddress = new AddressEntityModel();

        // Getting text from text fields
        EditText textFirst = (EditText)findViewById(R.id.signupFirstName);
        EditText textLast = (EditText)findViewById(R.id.signupLastName);

        EditText textEmail = (EditText)findViewById(R.id.signupEmailAddress);

        EditText textPassword = (EditText)findViewById(R.id.signupPassword);

        EditText textAddress = (EditText)findViewById(R.id.signupStreetAddress);
        EditText textCity = (EditText)findViewById(R.id.signupCity);
        EditText textPostalCode = (EditText)findViewById(R.id.signupPincode);
        EditText textCountry = (EditText)findViewById(R.id.signupCountry);

        // Setting current text fields
        setCurrentFields(textFirst, textLast, textEmail, textPassword, textAddress, textCity,
                textPostalCode, textCountry);

        // Setting user data
        user.setFirstName(textFirst.getText().toString());
        user.setLastName(textLast.getText().toString());

        user.setEmail(textEmail.getText().toString());

        user.setPassword(textPassword.getText().toString());

        userAddress.setStreetAddress(textAddress.getText().toString());
        userAddress.setCity(textCity.getText().toString());
        userAddress.setPostalCode(textPostalCode.getText().toString());
        userAddress.setCountry(textCountry.getText().toString());
        user.setAddress(userAddress);

        return user;
    }

    /**
     * this helper method saves and sets the current text fields on the sign-up screen
     * @param textFirst EditText that contains the first name
     * @param textLast EditText that contains the last name
     * @param textEmail EditText that contains the email
     * @param textPassword EditText that contains the password
     * @param textAddress EditText that contains the street address
     * @param textCity EditText that contains the city
     * @param textPostalCode EditText that contains the postal code
     * @param textCountry EditText that contains the country
     */
    private void setCurrentFields(EditText textFirst, EditText textLast, EditText textEmail,
                                  EditText textPassword, EditText textAddress, EditText textCity,
                                  EditText textPostalCode, EditText textCountry) {

        currentFields[0] = textFirst;
        currentFields[1] = textLast;
        currentFields[2] = textEmail;
        currentFields[3] = textPassword;
        currentFields[4] = textAddress;
        currentFields[5] = textCity;
        currentFields[6] = textPostalCode;
        currentFields[7] = textCountry;

    }

    /**
     * this helper method returns the current text field
     *
     * it is called in the <code>User</code> class during validation
     *
     * @return the current EditText field at the specified index in the currentFields array
     */
    public static EditText getCurrentFields(int index) {

        return currentFields[index];

    }

    /**
     * this helper method saves and sets the current text fields on the sign-up screen for the credit card info
     * @param textCardBrand EditText that contains the credit card brand
     * @param textCardNumber EditText that contains the credit card number
     * @param textCardName EditText that contains the card holder name
     * @param textCardMonth EditText that contains the expiration month
     * @param textCardYear EditText that contains the expiration year
     * @param textCardCVC EditText that contains the credit card CVC security code
     */
    private void setCurrentFieldsCredit(EditText textCardBrand, EditText textCardNumber,
                                        EditText textCardName, EditText textCardMonth,
                                        EditText textCardYear, EditText textCardCVC) {

        currentFieldsCredit[0] = textCardBrand;
        currentFieldsCredit[1] = textCardNumber;
        currentFieldsCredit[2] = textCardName;
        currentFieldsCredit[3] = textCardMonth;
        currentFieldsCredit[4] = textCardYear;
        currentFieldsCredit[5] = textCardCVC;

    }

    /**
     * this helper method returns the current text field
     *
     * it is called in the <code>CreditCard</code> class during validation
     *
     * @return the current EditText field at the specified index in the currentFieldsCredit array
     */
    public static EditText getCurrentFieldsCredit(int index) {

        return currentFieldsCredit[index];

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
        String number;

        EditText textCardMonth = (EditText)findViewById(R.id.signupCreditCardMonth);

        creditCard.setExpiryMonth(Integer.parseInt(textCardMonth.getText().toString()));

        EditText textCardYear = (EditText)findViewById(R.id.signupCreditCardYear);

        creditCard.setExpiryYear(Integer.parseInt(textCardYear.getText().toString()));

        EditText textCardCVC = (EditText)findViewById(R.id.signupCreditCardCVC);

        creditCard.setCvc(textCardCVC.toString());

        setCurrentFieldsCredit(textCardBrand, textCardNumber, textCardName, textCardMonth, textCardYear,
                textCardCVC);

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