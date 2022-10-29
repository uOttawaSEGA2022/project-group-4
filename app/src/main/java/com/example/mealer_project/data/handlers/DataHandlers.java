package com.example.mealer_project.data.handlers;

import com.example.mealer_project.data.sources.FirebaseRepository;

public class DataHandlers {
    // instance handlers
    private final UserHandler userHandler;
    private final InboxHandler inboxHandler;
//    public MealDataHandler MealHandler;
//    public OrderDataHandler OrderHandler;

    // instantiate repository and handlers
    public DataHandlers(FirebaseRepository firebaseRepository) {
        this.userHandler = new UserHandler();
        this.inboxHandler = new InboxHandler();
    }

    public UserHandler getUserHandler() {
        return userHandler;
    }

    public InboxHandler getInboxHandler() { return inboxHandler; }

}
