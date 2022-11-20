package com.example.mealer_project.app;

import android.util.Log;

import com.example.mealer_project.data.handlers.DataHandlers;
import com.example.mealer_project.data.models.User;
import com.example.mealer_project.data.models.UserRoles;
import com.example.mealer_project.data.models.inbox.AdminInbox;
import com.example.mealer_project.data.sources.FirebaseRepository;
import com.example.mealer_project.ui.screens.meals.MealsListScreen;
import com.example.mealer_project.utils.Preconditions;
import com.google.firebase.auth.FirebaseAuth;

public class AppInstance {
    private FirebaseRepository primaryDatabase;
    private DataHandlers appDataHandlers;
    private User user;
    private AdminInbox adminInbox;
    private MealsListScreen mealsListScreen;

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

    public boolean userIsAdmin() {
        return (user != null && user.getRole() == UserRoles.ADMIN);
    }

    /**
     * Sets app's admin inbox
     * @param adminInbox AdminInbox instance
     */
    public void setAdminInbox(AdminInbox adminInbox) throws NullPointerException {
        if (Preconditions.isNotNull(adminInbox)) {
            this.adminInbox = adminInbox;
        } else {
            Log.e("setAdminInbox", "App instance received a null object for adminInbox");
            throw new NullPointerException("Unable to create admin inbox");
        }
    }

    /**
     * @return Returns AdminInbox if current user is admin
     * @throws IllegalAccessException if user is not admin but tries to access AdminInbox
     */
    public AdminInbox getAdminInbox() throws IllegalAccessException {
        if (userIsAdmin()) {
            return this.adminInbox;
        } else {
            Log.e("getAdminInbox", "Either user is null or user role is not admin");
            throw new IllegalAccessException("User does not have access to admin inbox");
        }
    }

    public MealsListScreen getMealsListScreen() {
        return mealsListScreen;
    }

    public void setMealsListScreen(MealsListScreen mealsListScreen) {
        this.mealsListScreen = mealsListScreen;
    }

}
