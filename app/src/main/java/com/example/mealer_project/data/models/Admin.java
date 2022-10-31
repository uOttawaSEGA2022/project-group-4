package com.example.mealer_project.data.models;

/**
 * This class instantiates a single instance of Admin for Mealer App
 * Design Pattern: Singleton pattern -> https://www.geeksforgeeks.org/singleton-design-pattern/
 */
public class Admin extends User {
    private static volatile Admin mealerAdmin = null;
    /**
     * Create a single instance of admin
     * @param firstName First name of the admin
     * @param lastName Last name of the admin
     * @param email email of the admin
     * @param password password for the admin
     * @param address address of the admin
     * @param role Role of the admin
     */
    private Admin(String firstName, String lastName, String email, String password, Address address, UserRoles role) {
        // instantiate Admins data members
        super(firstName, lastName, email, password, address, role);
    }

    /**
     * Constructor to instantiate a new Admin
     * @param userId userid of the admin
     * @param firstName First name of the admin
     * @param lastName Last name of the admin
     * @param email email of the admin
     */
    public Admin(String userId, String firstName, String lastName, String email) {
        super(userId, firstName, lastName, email, UserRoles.ADMIN);
    }

    public static Admin getInstance(String firstName, String lastName, String email, String password, Address address, UserRoles role) {
        if(mealerAdmin == null) {

            //To make thread safe
            synchronized (Admin.class) {
                // check again as multiple threads
                // can reach above step
                if(mealerAdmin == null)
                    mealerAdmin = new Admin(firstName, lastName, email, password, address, role);
            }
        }
        return mealerAdmin;
    }

    /**
     * Method for Admin to suspend Cook either temporarily or indefinitely
     */
    public void suspend(String chefId) {

        //Information about Cook's suspension & changes to Database
    }

    /**
     * Method for Admin to dismiss a complaint from Client
     */
    public void deleteComplaint() {
        //Information about Client's dismissed complaint & changes to Database
    }
}
