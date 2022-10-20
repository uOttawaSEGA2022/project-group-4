package com.example.mealer_project.data.handlers;

import com.example.mealer_project.data.sources.FirebaseRepository;

public class DataHandlers {

    private FirebaseRepository firebaseRepository;
    // instance handlers
    private UserDataHandler userDataHandler;
//    public MealDataHandler MealHandler;
//    public OrderDataHandler OrderHandler;

    // instantiate repository and handlers
    public DataHandlers(FirebaseRepository firebaseRepository) {
        this.firebaseRepository = firebaseRepository;
        this.userDataHandler = new UserDataHandler();
    }

    public UserDataHandler getUserDataHandler() {
        return userDataHandler;
    }

}
