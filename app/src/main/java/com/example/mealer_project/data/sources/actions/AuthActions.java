package com.example.mealer_project.data.sources.actions;

import androidx.annotation.NonNull;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.handlers.UserHandler;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Client;
import com.example.mealer_project.data.models.User;
import com.example.mealer_project.data.sources.FirebaseRepository;
import com.example.mealer_project.ui.screens.LoginScreen;
import com.example.mealer_project.ui.screens.SignupScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthActions {
    final private FirebaseAuth mAuth;
    FirebaseFirestore database;
    FirebaseRepository firebaseRepository;

    public AuthActions(FirebaseAuth mAuth, FirebaseFirestore database, FirebaseRepository firebaseRepository) {
        this.mAuth = mAuth;
        this.database = database;
        this.firebaseRepository = firebaseRepository;
    }

    public void registerClient(String email, String password, SignupScreen signupScreen, Client newUser) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                newUser.setUserId(user.getUid());
                                App.getAppInstance().setUser(newUser);

                                // therefore, add the user to database
                                database = FirebaseFirestore.getInstance();

                                Map<String, Object> databaseUser = new HashMap<>();
                                databaseUser.put("firstName", newUser.getFirstName());
                                databaseUser.put("lastName", newUser.getLastName());
                                databaseUser.put("email", newUser.getEmail());
                                databaseUser.put("addressStreet", newUser.getAddress().getStreetAddress());
                                databaseUser.put("addressCity", newUser.getAddress().getCity());
                                databaseUser.put("country", newUser.getAddress().getCountry());
                                databaseUser.put("postalCode", newUser.getAddress().getPostalCode());
                                databaseUser.put("creditCardBrand", newUser.getClientCreditCard().getBrand());
                                databaseUser.put("creditCardName", newUser.getClientCreditCard().getName());
                                databaseUser.put("creditCardNumber", newUser.getClientCreditCard().getNumber());
                                databaseUser.put("creditCardExpiryMonth", newUser.getClientCreditCard().getExpiryMonth());
                                databaseUser.put("creditCardExpiryYear", newUser.getClientCreditCard().getExpiryYear());
                                databaseUser.put("creditCardCvc", newUser.getClientCreditCard().getCvc());


                                database.collection("Clients").document(newUser.getUserId())
                                        .set(databaseUser)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // let signup activity display next screen now
                                                signupScreen.showNextScreen();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                signupScreen.userRegistrationFailed("Unable to add Client's data to the Client collection: " + e.getMessage());
                                            }
                                        });
                            } else {
                                signupScreen.userRegistrationFailed("User registration returned no user info");
                            }
                        } else {
                            if (task.getException() != null) {
                                signupScreen.userRegistrationFailed(task.getException().toString());
                            } else {
                                signupScreen.userRegistrationFailed("createUserWithEmailAndPassword failed: for unknown reasons");
                            }

                        }
                    }
                });
    }

    public void registerChef(String email, String password, SignupScreen signupScreen, Chef newUser) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                newUser.setUserId(user.getUid());
                                App.getAppInstance().setUser(newUser);

                                // therefore, add the user to database
                                database = FirebaseFirestore.getInstance();

                                Map<String, Object> databaseUser = new HashMap<>();
                                databaseUser.put("firstName", newUser.getFirstName());
                                databaseUser.put("lastName", newUser.getLastName());
                                databaseUser.put("email", newUser.getEmail());
                                databaseUser.put("isSuspended", newUser.getIsSuspended());
                                databaseUser.put("suspensionDate", newUser.getSuspensionDate());
                                databaseUser.put("addressStreet", newUser.getAddress().getStreetAddress());
                                databaseUser.put("addressCity", newUser.getAddress().getCity());
                                databaseUser.put("country", newUser.getAddress().getCountry());
                                databaseUser.put("postalCode", newUser.getAddress().getPostalCode());
                                databaseUser.put("voidCheque", newUser.getVoidCheque());
                                databaseUser.put("description", newUser.getDescription());

                                database.collection("Chefs").document(newUser.getUserId())
                                        .set(databaseUser)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // let signup activity display next screen now
                                                signupScreen.showNextScreen();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                signupScreen.userRegistrationFailed("Unable to add Chef's data to the Chef collection: " + e.getMessage());
                                            }
                                        });
                            } else {
                                signupScreen.userRegistrationFailed("User registration returned no user info");
                            }
                        } else {
                            if (task.getException() != null) {
                                signupScreen.userRegistrationFailed(task.getException().toString());
                            } else {
                                signupScreen.userRegistrationFailed("createUserWithEmailAndPassword failed: for unknown reasons");
                            }

                        }
                    }
                });
    }


    public void logInUser(String email, String password, LoginScreen loginScreen) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        firebaseRepository.USER.getUserById(user.getUid(), loginScreen);
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    loginScreen.dbOperationFailureHandler(UserHandler.dbOperations.USER_LOG_IN, "Incorrect login information: please try again.");
                }
            }
        });
    }

    public void logOutUser() {
        // log out the current user
        mAuth.signOut();
    }
}
