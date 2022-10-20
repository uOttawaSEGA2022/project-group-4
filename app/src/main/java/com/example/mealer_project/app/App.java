package com.example.mealer_project.app;

import com.example.mealer_project.data.handlers.DataHandlers;
import com.example.mealer_project.data.handlers.UserDataHandler;
import com.example.mealer_project.data.sources.FirebaseRepository;
import com.google.firebase.auth.FirebaseAuth;

public class App {
    static AppInstance app = new AppInstance();

    public static AppInstance getAppInstance() {
        return app;
    }

    public static DataHandlers getAppDataHandler() {
        return app.getAppDataHandler();
    }

    public static FirebaseRepository getPrimaryDatabase() {
        return app.getPrimaryDatabase();
    }

    public static UserDataHandler getUserDataHandler() {
        return app.getAppDataHandler().getUserDataHandler();
    }

}
