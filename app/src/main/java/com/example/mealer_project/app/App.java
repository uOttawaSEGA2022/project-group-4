package com.example.mealer_project.app;

import com.example.mealer_project.data.handlers.DataHandlers;
import com.example.mealer_project.data.handlers.UserHandler;
import com.example.mealer_project.data.models.UserRoles;
import com.example.mealer_project.data.models.inbox.AdminInbox;
import com.example.mealer_project.data.sources.FirebaseRepository;

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

    public static UserHandler getUserDataHandler() {
        return app.getAppDataHandler().getUserDataHandler();
    }

    /**
     * Checks if there is a valid user and the user is Admin
     * @return true if current user is admin, else false
     */
    public static boolean userIsAdmin() {
        return app.userIsAdmin();
    }

    /**
     * @return Returns AdminInbox if current user is admin
     * @throws IllegalAccessException if user is not admin but tries to access AdminInbox
     */
    public static AdminInbox getAdminInbox() throws IllegalAccessException {
        return app.getAdminInbox();
    }

    /**
     * Sets app's admin inbox
     * @param adminInbox AdminInbox instance
     */
    public static void setAdminInbox(AdminInbox adminInbox) throws NullPointerException {
        app.setAdminInbox(adminInbox);
    }
}
