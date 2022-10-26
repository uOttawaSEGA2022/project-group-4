package com.example.mealer_project.data.handlers;

import com.example.mealer_project.data.sources.FirebaseRepository;

public class DataHandlers {

    private FirebaseRepository firebaseRepository;
    // instance handlers
    private UserHandler userHandler;
//    public MealDataHandler MealHandler;
//    public OrderDataHandler OrderHandler;

    // instantiate repository and handlers
    public DataHandlers(FirebaseRepository firebaseRepository) {
        this.firebaseRepository = firebaseRepository;
        this.userHandler = new UserHandler();
    }

    public UserHandler getUserDataHandler() {
        return userHandler;
    }

}
