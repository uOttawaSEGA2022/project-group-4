package com.example.mealer_project.data.handlers;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.entity_models.CreditCardEntityModel;
import com.example.mealer_project.data.entity_models.UserEntityModel;
import com.example.mealer_project.data.models.Address;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Client;
import com.example.mealer_project.data.models.CreditCard;
import com.example.mealer_project.data.models.UserRoles;
import com.example.mealer_project.ui.LoginScreen;
import com.example.mealer_project.ui.SignupActivity;
import com.example.mealer_project.utils.Response;

public class UserDataHandler {

    public UserDataHandler() {
    }

    public Response registerClient(SignupActivity signupActivity, UserEntityModel userData, CreditCardEntityModel creditCardData) {
        // guard clause
        if (userData == null) {
            return new Response(false, "Please complete all fields.");
        }
        else if (creditCardData == null) { //no description
            return new Response(false, "Please complete all credit card fields.");
        }

        // set the appropriate role for the user
        userData.setRole(UserRoles.CLIENT);

        try {
            // Try to instantiate three objects: Address object, CreditCard object, and finally, Client object
            // if any of these throw exception (i.e., if unable to create instance) we handle and return response
            Client newClient = new Client(userData, new Address(userData.getAddress()), new CreditCard(creditCardData));

            // if code execution reaches this points, it means all user data was valid
            // therefore, add the user to database
            // in returned Result, on success: user id is returned (from firebase) or error message
            App.getPrimaryDatabase().AUTH
                    .registerClient(userData.getEmail(), userData.getPassword(), signupActivity, newClient);

            return new Response(true, "User login submitted!");
        } catch (Exception e) {
            // if at any point, code throws exception (ex: unable to create instance)
            return new Response(false, e.getMessage());
        }

    }

    public Response registerChef(SignupActivity signupActivity, UserEntityModel userData, String chefShortDescription, String voidCheque) {
        // guard clause
        if (userData == null) {
            return new Response(false, "Please complete all fields.");
        }
        else if (chefShortDescription != null && chefShortDescription.equals("")) { //no description
            return new Response(false, "Please provide a short description of yourself.");
        }

        // set the appropriate role for the user
        userData.setRole(UserRoles.CHEF);

        try {

            // Try to instantiate three objects: Address object, CreditCard object, and finally, Client object
            // if any of these throw exception (i.e., if unable to create instance) we handle and return response
            Chef newChef = new Chef(userData, userData.getRole(), new Address(userData.getAddress()), chefShortDescription, voidCheque);

            // if code execution reaches this points, it means all user data was valid
            // therefore, add the user to database
            // in returned Result, on success: user id is returned (from firebase) or error message
            App.getPrimaryDatabase().AUTH
                    .registerChef(userData.getEmail(), userData.getPassword(), signupActivity, newChef);

            return new Response(true, "User login submitted");

        } catch (Exception e) {
            // if at any point, code throws exception (ex: unable to create instance)
            return new Response(false, e.getMessage());
        }
    }


    public void logInUser(LoginScreen loginScreen, String email, String password) {
        App.getPrimaryDatabase().AUTH.logInUser(email, password, loginScreen);
    }
}
