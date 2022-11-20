package com.example.mealer_project.data.sources;

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
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.data.models.UserRoles;
import com.example.mealer_project.data.sources.actions.AuthActions;
import com.example.mealer_project.data.sources.actions.InboxActions;
import com.example.mealer_project.data.sources.actions.MealActions;
import com.example.mealer_project.data.sources.actions.OrderActions;
import com.example.mealer_project.data.sources.actions.UserActions;
import com.example.mealer_project.ui.screens.LoginScreen;
import com.example.mealer_project.ui.screens.SignupScreen;
import com.example.mealer_project.utils.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirebaseRepository {

    public UserActions USER;
    public AuthActions AUTH;
    public InboxActions INBOX;
    public MealActions MEALS;
    public OrderActions ORDERS;

    public FirebaseRepository(FirebaseAuth mAuth) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        this.USER = new UserActions(db);
        this.AUTH = new AuthActions(mAuth, db, this);
        this.INBOX = new InboxActions(db, this);
        this.MEALS = new MealActions(db);
        this.ORDERS = new OrderActions(db);
    }



}