package com.example.mealer_project.data.sources.actions;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.entity_models.AddressEntityModel;
import com.example.mealer_project.data.entity_models.CreditCardEntityModel;
import com.example.mealer_project.data.entity_models.UserEntityModel;
import com.example.mealer_project.data.models.Address;
import com.example.mealer_project.data.models.Admin;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Client;
import com.example.mealer_project.data.models.CreditCard;
import com.example.mealer_project.data.models.UserRoles;
import com.example.mealer_project.ui.screens.LoginScreen;
import com.example.mealer_project.utils.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class UserActions {

    FirebaseFirestore database;

    public UserActions(FirebaseFirestore database) {
        this.database = database;
    }

    protected void getUserById(String userId, LoginScreen loginScreen) {
        // first check if Admin
        // then check if Client
        // then check if Chef
        DocumentReference userReference = database.collection("Admin").document(userId);

        userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.getData() != null){
                            Response r = makeAdminFromFirebase(document, userId);
                            if (loginScreen != null && r.isSuccess()) {
                                loginScreen.showNextScreen();
                            } else if (loginScreen != null){
                                Log.e("Login failed for admin", r.getErrorMessage());
                                loginScreen.userLoginFailed("Login failed for admin: " + r.getErrorMessage());
                            }

                            if (r.isError()) {
                                Log.e("getUserById: ", r.getErrorMessage());
                            }
                        }

                    } else {
                        // if user not in Admin collection, check Chef
                        getChefById(userId, loginScreen);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    protected void getChefById(String userId, LoginScreen loginScreen) {
        DocumentReference userReference = database.collection("Chefs").document(userId);

        userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        if (document.getData() != null){
                            Response r = makeChefFromFirebase(document);

                            if (loginScreen != null && r.isSuccess()) {
                                loginScreen.showNextScreen();
                            } else if (loginScreen != null){
                                Log.e("Login failed for chef", r.getErrorMessage());
                                loginScreen.userLoginFailed("Login failed for user: " + r.getErrorMessage());
                            }
                        }

                    } else {
                        getClientById(userId, loginScreen);
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    protected void getClientById(String userId, LoginScreen loginScreen) {
        DocumentReference userReference = database.collection("Clients").document(userId);

        userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        if (document.getData() != null){
                            Response r = makeClientFromFirebase(document);
                            if (loginScreen != null && r.isSuccess()) {
                                loginScreen.showNextScreen();
                            } else if (loginScreen != null){
                                Log.e("Login failed for client", r.getErrorMessage());
                                loginScreen.userLoginFailed("Login failed for user: " + r.getErrorMessage());
                            }

                            if (r.isError()) {
                                Log.e("getUserById: ", r.getErrorMessage());
                            }
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private Response makeAdminFromFirebase(DocumentSnapshot document, String uid){

        try{
            if (document.getData() == null) {
                throw new NullPointerException("makeAdminFromFirebase: invalid document object");
            }

            String firstName = String.valueOf(document.getData().get("firstName"));
            String lastName = String.valueOf(document.getData().get("lastName"));
            String email = String.valueOf(document.getData().get("email"));

            App.getAppInstance().setUser(new Admin(uid, firstName, lastName, email));

            return new Response(true);
        } catch (Exception e) {
            return new Response(false, "makeAdminFromFirebase: " + e.getMessage());
        }
    }

    private Response makeClientFromFirebase(DocumentSnapshot document){

        try{

            if (document.getData() == null) {
                throw new NullPointerException("makeClientFromFirebase: invalid document object");
            }

            UserEntityModel newUser = new UserEntityModel();
            AddressEntityModel newAddress = new AddressEntityModel();
            CreditCardEntityModel newCreditCard = new CreditCardEntityModel();

            newUser.setFirstName(String.valueOf(document.getData().get("firstName")));
            newUser.setLastName(String.valueOf(document.getData().get("lastName")));
            newUser.setEmail(String.valueOf(document.getData().get("email")));
            newUser.setRole(UserRoles.CLIENT);

            newAddress.setStreetAddress(String.valueOf(document.getData().get("addressStreet")));
            newAddress.setCity(String.valueOf(document.getData().get("addressCity")));
            newAddress.setCountry(String.valueOf(document.getData().get("country")));
            newAddress.setPostalCode(String.valueOf(document.getData().get("postalCode")));

            newCreditCard.setBrand(String.valueOf(document.getData().get("creditCardBrand")));
            newCreditCard.setName(String.valueOf(document.getData().get("creditCardName")));
            newCreditCard.setNumber(String.valueOf(document.getData().get("creditCardNumber")));
            newCreditCard.setExpiryMonth(Integer.parseInt(document.getData().get("creditCardExpiryMonth").toString()));
            newCreditCard.setExpiryYear(Integer.parseInt(document.getData().get("creditCardExpiryYear").toString()));
            newCreditCard.setCvc(String.valueOf(document.getData().get("creditCardCvc")));

            Address address = new Address(newAddress);
            CreditCard creditCard = new CreditCard(newCreditCard);

            Client newClient = new Client(newUser, address, creditCard);

            App.getAppInstance().setUser(newClient);

            return new Response(true);
        } catch (Exception e) {
            return new Response(false, "makeChefFromFirebase: " + e.getMessage());
        }
    }

    private Response makeChefFromFirebase(DocumentSnapshot document){

        try {

            if (document.getData() == null) {
                throw new NullPointerException("makeChefFromFirebase: invalid document object");
            }

            UserEntityModel newUser = new UserEntityModel();
            AddressEntityModel newAddress = new AddressEntityModel();

            newUser.setFirstName(String.valueOf(document.getData().get("firstName")));
            newUser.setLastName(String.valueOf(document.getData().get("lastName")));
            newUser.setEmail(String.valueOf(document.getData().get("email")));
            newUser.setRole(UserRoles.CHEF);

            newAddress.setStreetAddress(String.valueOf(document.getData().get("addressStreet")));
            newAddress.setCity(String.valueOf(document.getData().get("addressCity")));
            newAddress.setCountry(String.valueOf(document.getData().get("country")));
            newAddress.setPostalCode(String.valueOf(document.getData().get("postalCode")));

            String description = String.valueOf(document.getData().get("description"));
            String voidCheque = String.valueOf(document.getData().get("voidCheque"));

            Address address = new Address(newAddress);

            Chef newChef = new Chef(newUser, address, description, voidCheque);
            newChef.setIsSuspended((Boolean)document.getData().get("isSuspended"));
            newChef.setSuspensionDate((Date)document.getData().get("suspensionDate"));

            App.getAppInstance().setUser(newChef);

            return new Response(true);
        } catch (Exception e) {
            return new Response(false, "makeChefFromFirebase: " + e.getMessage());
        }
    }
}
