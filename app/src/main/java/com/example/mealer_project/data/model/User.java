package com.example.mealer_project.data.model;

/**
 * User class to instantiate a User
 * Parent class of Client, Chef and Admin
 */
public class User {
    protected String firstName;
    protected String lastName;
    protected String email;
    // storing password as string for simplicity for now
    protected String password;
    // not modularizing address (ex: city, region, zipcode), storing as single string for now
    protected String address;
    // user id will likely be a UUID (from Firebase or App logic)
    protected String userId;
    // User's role can one of the accepted values
    // below are the accepted roles (same for all user's)
    static private final String ADMIN = "ADMIN";
    static private final String CLIENT = "CLIENT";
    static private final String CHEF = "CHEF";
    // an enum to allow child classes to access possible roles
    protected enum UserRoles {
        ADMIN,
        CLIENT,
        CHEF
    }
    // data member containing user's role
    protected UserRoles role;

    /**
     * Create a User object
     * @param firstName First name of the user
     * @param lastName Last name of the user
     * @param email email of the user
     * @param password password for the user
     * @param address address of the user
     * @param role Role of the user
     */
    public User(String firstName, String lastName, String email, String password, String address, UserRoles role) {
        // instantiate User's data members
        // using setters to enable validation of incoming data
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setPassword(password);
        this.setAddress(address);
        // value of role has to be one of accepted user roles
        this.setRole(role);
        // id will be auto generated (uuid), by firebase or app logic
        this.setUserId("");
    }

    /**
     * Get user's first name
     * @return String representing user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set user's first name
     * @param firstName String representing user's first name
     */
    public void setFirstName(String firstName) {
        // validate first name
        this.firstName = firstName;
    }

    /**
     * Get user's last name
     * @return String representing user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Set user's last name
     * @param lastName String representing user's last name
     */
    public void setLastName(String lastName) {
        // validate last name
        this.lastName = lastName;
    }

    /**
     * Get user's email address
     * @return String representing user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set user's email address
     * @param email String representing user's email address
     */
    public void setEmail(String email) {
        // validate email format
        this.email = email;
    }

    /**
     * Validate user's password against the supplied password
     * @param password password to validate
     * @return True, if passwords match, else False
     */
    public Boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    /**
     * Set user's password
     * @param password String representing user's password
     */
    public void setPassword(String password) {
        // validate password
        this.password = password;
    }

    /**
     * Get user's address
     * @return String representing user's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set user's address
     * @param address String representing user's address
     */
    public void setAddress(String address) {
        // validate address
        this.address = address;
    }

    /**
     * Get user's id
     * @return String representing user's id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Set user's id
     * @param userId String representing user's id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Get user's role
     * @return String representing user's role
     */
    public String getRole() {
        return role.toString();
    }

    /**
     * Set user's role
     * @param role one of the values of UserRoles
     */
    public void setRole(UserRoles role) {
        // validate user role
        this.role = role;
    }
}
