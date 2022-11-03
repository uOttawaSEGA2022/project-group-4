package com.example.mealer_project.data.handlers;

import android.util.Log;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.entity_models.CreditCardEntityModel;
import com.example.mealer_project.data.entity_models.UserEntityModel;
import com.example.mealer_project.data.models.Address;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Client;
import com.example.mealer_project.data.models.CreditCard;
import com.example.mealer_project.data.models.UserRoles;
import com.example.mealer_project.ui.screens.LoginScreen;
import com.example.mealer_project.ui.screens.SignupScreen;
import com.example.mealer_project.utils.Response;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserHandler {

    /**
     * Register a new Client account
     * @param signupScreen instance of login screen (required in asynchronous callback function to indicate success or failure)
     * @param userData user entity model containing all unvalidated user data
     * @param creditCardData credit card entity model containing all unvalidated credit card data
     * @return a Response object containing error or success message
     */
    public Response registerClient(SignupScreen signupScreen, UserEntityModel userData, CreditCardEntityModel creditCardData) {
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
                    .registerClient(userData.getEmail(), userData.getPassword(), signupScreen, newClient);

            return new Response(true, "User signup submitted!");
        } catch (Exception e) {
            // if at any point, code throws exception (ex: unable to create instance)
            return new Response(false, e.getMessage());
        }

    }

    /**
     * Register a new Chef account
     * @param signupScreen instance of login screen (required in asynchronous callback function to indicate success or failure)
     * @param userData user entity model containing all unvalidated user data
     * @param chefShortDescription a short description provided by chef
     * @param voidCheque void cheque image provided by chef
     * @return a Response object containing error or success message
     */
    public Response registerChef(SignupScreen signupScreen, UserEntityModel userData, String chefShortDescription, String voidCheque) {
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

            // Try to instantiate two objects: Address object & the Chef object itself
            // if any of these throw exception (i.e., if unable to create instance) we handle and return response
            Chef newChef = new Chef(userData, new Address(userData.getAddress()), chefShortDescription, voidCheque);

            // if code execution reaches this points, it means all user data was valid
            // therefore, add the user to database
            // in returned Result, on success: user id is returned (from firebase) or error message
            App.getPrimaryDatabase().AUTH
                    .registerChef(userData.getEmail(), userData.getPassword(), signupScreen, newChef);

            return new Response(true, "User signup submitted");

        } catch (Exception e) {
            // if at any point, code throws exception (ex: unable to create instance)
            return new Response(false, e.getMessage());
        }
    }

    /**
     * Method to login user
     * @param loginScreen instance of login screen (required in asynchronous callback function to indicate success or failure)
     * @param email email address of the user
     * @param password password provided by the user
     */
    public void logInUser(LoginScreen loginScreen, String email, String password) {
        App.getPrimaryDatabase().AUTH.logInUser(email, password, loginScreen);
    }

    /**
     * Method to suspend chef
     * @param chef Chef involved with complaint
     * @param suspensionDate end date of suspension
     */
    public void suspendChef(Chef chef, Date suspensionDate){

        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy");
        String suspension= formatter.format(suspensionDate);

        App.getPrimaryDatabase().USER.updateChefSuspension(chef.getUserId(), true, suspension);
    }

    /**
     * Method to update chef (check if date has passed)
     * @param chef Chef involved with complaint
     */
    public void updateChef(Chef chef){

        if (new Date().after(chef.getSuspensionDate())) { // if the date has passed, we change info in firebase
            chef.setIsSuspended(false);
            chef.setSuspensionDate(null);
            App.getPrimaryDatabase().USER.updateChefSuspension(chef.getUserId(), false, null);
        }
    }
}
