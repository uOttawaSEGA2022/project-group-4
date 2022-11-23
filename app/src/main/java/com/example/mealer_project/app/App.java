package com.example.mealer_project.app;

import com.example.mealer_project.data.handlers.InboxHandler;
import com.example.mealer_project.data.handlers.MealHandler;
import com.example.mealer_project.data.handlers.OrderHandler;
import com.example.mealer_project.data.handlers.UserHandler;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Client;
import com.example.mealer_project.data.models.User;
import com.example.mealer_project.data.models.inbox.AdminInbox;
import com.example.mealer_project.data.models.orders.OrderItem;
import com.example.mealer_project.data.sources.FirebaseRepository;

import java.util.Map;

public class App {
    static AppInstance app = new AppInstance();
    static final public UserHandler USER_HANDLER = app.getAppDataHandler().getUserHandler();
    static final public InboxHandler INBOX_HANDLER = app.getAppDataHandler().getInboxHandler();
    static final public MealHandler MEAL_HANDLER = app.getAppDataHandler().getMealHandler();
    static final public OrderHandler ORDER_HANDLER = app.getAppDataHandler().getOrderHandler();
    static final public Map<OrderItem, Boolean> CART = app.getClientCart();

    public static AppInstance getAppInstance() {
        return app;
    }

    public static FirebaseRepository getPrimaryDatabase() {
        return app.getPrimaryDatabase();
    }

    public static User getUser() { return app.getUser(); }
    public static String getUserId() { return app.getUser().getUserId(); }

    public static UserHandler getUserHandler() {
        return app.getAppDataHandler().getUserHandler();
    }

    public static InboxHandler getInboxHandler() {
        return app.getAppDataHandler().getInboxHandler();
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

    /**
     * Helper method to get an instance of a logged in Client
     * @return instance of client, if authentication or user validation fails, returns null
     */
    public static Client getClient() {
        // if user is logged in and is a Client
        if (app.isUserAuthenticated() && app.getUser() instanceof Client) {
            try {
                return (Client) app.getUser();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * Helper method to get an instance of a logged in Chef
     * @return instance of chef, if authentication or user validation fails, returns null
     */
    public static Chef getChef() {
        // if user is logged in and is a Client
        if (app.isUserAuthenticated() && app.getUser() instanceof Chef) {
            try {
                return (Chef) app.getUser();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }
}
