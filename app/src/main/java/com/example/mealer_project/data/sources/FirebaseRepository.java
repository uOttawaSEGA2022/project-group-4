package com.example.mealer_project.data.sources;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Client;
import com.example.mealer_project.data.models.User;
import com.example.mealer_project.data.models.UserRoles;
import com.example.mealer_project.ui.LoginScreen;
import com.example.mealer_project.ui.SignupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseRepository {

    final private FirebaseAuth mAuth;
    public AuthActions AUTH;
    FirebaseFirestore database;

    public FirebaseRepository(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
        this.AUTH = new AuthActions();
    }


    public class AuthActions {

        public void registerClient(String email, String password, SignupActivity signupActivity, Client newUser) {
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
                                    databaseUser.put("creditCard", newUser.getClientCreditCard());


                                    database.collection("Clients").document(newUser.getUserId())
                                            .set(databaseUser)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // print success statement
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // print failure statement
                                                }
                                            });

                                    signupActivity.showNextScreen();

                                } else {
                                    signupActivity.userRegistrationFailed("User registration returned no user info");
                                }
                            } else {
                                if (task.getException() != null) {
                                    signupActivity.userRegistrationFailed(task.getException().toString());
                                } else {
                                    signupActivity.userRegistrationFailed("createUserWithEmailAndPassword failed: for unknown reasons");
                                }

                            }
                        }
                    });
        }

        public void registerChef(String email, String password, SignupActivity signupActivity, Chef newUser) {
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
                                    databaseUser.put("voidCheque", newUser.getVoidCheque());
                                    databaseUser.put("description", newUser.getDescription());

                                    database.collection("Chefs").document(newUser.getUserId())
                                            .set(databaseUser)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // print success statement
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // print failure statement
                                                }
                                            });

                                    signupActivity.showNextScreen();

                                } else {
                                    signupActivity.userRegistrationFailed("User registration returned no user info");
                                }
                            } else {
                                if (task.getException() != null) {
                                    signupActivity.userRegistrationFailed(task.getException().toString());
                                } else {
                                    signupActivity.userRegistrationFailed("createUserWithEmailAndPassword failed: for unknown reasons");
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
                            App.getAppInstance().setCurrentUserName(user.getDisplayName());
                        }
                        loginScreen.showNextScreen();
                    } else {
                        // If sign in fails, display a message to the user.
                        loginScreen.userLoginFailed("Login failed for user: " + task.getException());
                    }
                }
            });
        }
    }


    private User getUserById(String userId){

        database = FirebaseFirestore.getInstance();

        DocumentReference userReference = database.collection("Clients").document(userId);

        userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        String firstName = String.valueOf(document.getData().get("firstName"));




//                        Client newClient = new Client();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        return null;
    }



}