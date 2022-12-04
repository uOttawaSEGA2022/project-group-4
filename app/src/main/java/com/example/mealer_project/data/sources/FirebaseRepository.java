package com.example.mealer_project.data.sources;

import com.example.mealer_project.data.sources.actions.AuthActions;
import com.example.mealer_project.data.sources.actions.InboxActions;
import com.example.mealer_project.data.sources.actions.MealActions;
import com.example.mealer_project.data.sources.actions.OrderActions;
import com.example.mealer_project.data.sources.actions.UserActions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

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