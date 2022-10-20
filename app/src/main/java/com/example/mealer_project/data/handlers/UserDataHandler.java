package com.example.mealer_project.data.handlers;

import androidx.annotation.NonNull;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.entity_models.CreditCardEntityModel;
import com.example.mealer_project.data.entity_models.UserEntityModel;
import com.example.mealer_project.data.models.Address;
import com.example.mealer_project.data.models.Client;
import com.example.mealer_project.data.models.CreditCard;
import com.example.mealer_project.data.models.User;
import com.example.mealer_project.data.models.UserRoles;
import com.example.mealer_project.ui.LoginScreen;
import com.example.mealer_project.ui.SignupActivity;
import com.example.mealer_project.utils.Response;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserDataHandler {
    String firstName;
    String lastName;
    String email;
    String addressStreet;
    String addressCity;
    String postalCode;
    String country;

    public UserDataHandler() {
    }

    public Response registerClient(SignupActivity signupActivity, UserEntityModel userData, CreditCardEntityModel creditCardData) {
        // guard clause
        if (userData == null || creditCardData == null) {
            return new Response(false, "registerUser failed: No User and Credit Card Info Provided");
        }

        // set the appropriate role for the user
        userData.setRole(UserRoles.CLIENT);

        try {

            // Try to instantiate three objects: Address object, CreditCard object, and finally, Client object
            // if any of these throw exception (i.e., if unable to create instance) we handle and return response
            User newClient = new Client(userData, new Address(userData.getAddress()), new CreditCard(creditCardData));

            // if code execution reaches this points, it means all user data was valid
            // therefore, add the user to database
            // in returned Result, on success: user id is returned (from firebase) or error message
            App.getPrimaryDatabase().AUTH
                    .registerUser(userData.getEmail(), userData.getPassword(), signupActivity, newClient);



            // if user was authenticated successfully
            if (App.getAppInstance().isUserAuthenticated()) {
                App.getAppInstance().setUser(newClient);
            }

            return new Response(true, "User login submitted");

        } catch (Exception e) {
            // if at any point, code throws exception (ex: unable to create instance)
            return new Response(false, "UserDataHandler: " + e.getMessage());
        }
    }



    public void logInUser(LoginScreen loginScreen, String email, String password) {
        App.getPrimaryDatabase().AUTH.logInUser(email, password, loginScreen);
    }
}
