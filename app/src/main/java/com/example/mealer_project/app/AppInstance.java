package com.example.mealer_project.app;

import com.example.mealer_project.data.handlers.DataHandlers;
import com.example.mealer_project.data.models.User;
import com.example.mealer_project.data.sources.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;

public class AppInstance {
    private FirebaseRepository primaryDatabase;
    private DataHandlers appDataHandlers;
    private User user;
    private String currentUserName; // need it only because we don't yet have persistent storage for users

    public AppInstance() {
        this.initializeApp();
    }

    public void initializeApp() {
        // set firebase to be the primary database
        primaryDatabase = new FirebaseRepository(FirebaseAuth.getInstance());
        appDataHandlers = new DataHandlers(primaryDatabase);
    }

    public boolean isUserAuthenticated() {
        return this.user != null;
    }

    public DataHandlers getAppDataHandler() {
        return this.appDataHandlers;
    }

    public FirebaseRepository getPrimaryDatabase() {
        return this.primaryDatabase;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }
}
