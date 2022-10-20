package com.example.mealer_project.data.models;

import com.example.mealer_project.data.entity_models.UserEntityModel;

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
    protected Address address;
    // user id will likely be a UUID (from Firebase or App logic)
    protected String userId;
    // User's role can one of the accepted values

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
    public User(String firstName, String lastName, String email, String password, Address address, UserRoles role) {
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
     * Create a User object
     * @param userData A UserEntityModel object representing non-validated user data
     * @param userAddress an Address object representing validated address data
     */
    public User(UserEntityModel userData, Address userAddress) {
        // instantiate User's data members
        // using setters to enable validation of incoming data
        this.setFirstName(userData.getFirstName());
        this.setLastName(userData.getLastName());
        this.setEmail(userData.getEmail());
        this.setPassword(userData.getPassword());
        this.setAddress(userAddress);
        // value of role has to be one of accepted user roles
        this.setRole(userData.getRole());
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

        if (validateName(firstName)) { //valid

            this.firstName = firstName; //setting name

        }
        else { //invalid

            // Output: error message
            throw new IllegalArgumentException("value of first name not valid");

        }

    }

    /**
     * this helper method checks and validates the first/last names
     *
     * @param name
     *  the name inputted
     *
     * @return
     *  whether the name is valid
     */
    private boolean validateName(String name) {

        // Process: checking name length
        if (name.length() > 0) { //at least 1 char

            // Variable Declaration
            char[] charsInName = name.toCharArray();

            // Process: validating input
            for (int i = 0; i < charsInName.length; i++) {

                // Process: checking for all letters
                if (!Character.isLetter(charsInName[i])) { //is not letter

                    return false;

                }

            }

            return true;

        }
        else { //nothing inputted

            return false;

        }

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

        if (validateName(lastName) == true) { //valid

            this.lastName = lastName; //setting name

        }
        else { //invalid

            // Output: error message
            throw new IllegalArgumentException("value of last name not valid");

        }

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

        // Process: checking if email is valid
        if (validateEmail(email)) { //valid

            this.email = email; //setting email

        }
        else {

            // Output: error msg
            throw new IllegalArgumentException("invalid email address provided");

        }
    }

    /**
     * this helper method checks and validates the email address inputted by the user
     *
     * @param email
     *  the email entered
     *
     * @return
     *  whether the email is valid
     */
   private boolean validateEmail(String email) {

        // Variable Declaration
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (email.length() > 0) { //not empty

            // Process: checking if email matches the email pattern
            if (email.matches(emailPattern)) { //success

                return true;

            } else { //invalid

                return false;

            }

        }
        else { //empty field

            return false;

        }

    }

    /**
     * Set user's password
     * @param password String representing user's password
     */
    public void setPassword(String password) {

        // Process: validating password
        if (validatePassword(password)) { //valid

            this.password = password;

        }
        else { //invalid

            // Output:
            throw new IllegalArgumentException("invalid password provided");

        }

    }

    /**
     * this helper method checks and validates the password inputted by the user
     *
     * @param password
     *  the password entered
     *
     * @return
     *  whether password is valid
     */
    private boolean validatePassword(String password) {

        /*
         * A good password should contain:
         * at least 8 characters
         * at least 1 capital
         * at least 1 number
         * at least 1 special character
         */

        // Variable Declaration
        boolean atLeast8 = false;
        boolean hasLetters = false;
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        char[] charsInPassword = password.toCharArray();

        // Process: checking for password length
        if (charsInPassword.length >= 8) { //valid

            atLeast8 = true;

            // Process: looping through password
            for (int i = 0; i < charsInPassword.length; i++) {

                // Process: checking char
                if (Character.isLetter(charsInPassword[i])) { //is letter

                    // Process: checking for uppercase
                    if (Character.isUpperCase(charsInPassword[i])) { //uppercase

                        hasCapital = true; //updating flag

                    }

                    hasLetters = true; //updating flag

                } else if (Character.isDigit(charsInPassword[i])) { //is number

                    hasNumber = true; //updating flag

                } else { //is special char

                    hasSpecial = true; //updating flag

                }

            }

            // Process: validating password
            if (hasCapital && hasLetters && hasNumber && hasSpecial) { //valid

                return true;

            }
            else { //invalid

                return false;

            }

        }
        else { //not long enough

            return false;

        }

    }

    /**
     * Validate user's password against the supplied password
     * @param password password to validate
     * @return True, if passwords match, else False
     */
    private Boolean checkMatchingPasswords(String password) {
        return this.password.equals(password);
    }

    /**
     * Get user's address
     * @return String representing user's address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Set user's address
     * @param address String representing user's address
     */
    public void setAddress(Address address) {
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
    public UserRoles getRole() {
        return role;
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
