package com.example.mealer_project.data.models;

import com.example.mealer_project.data.entity_models.UserEntityModel;

/**
 * User class to instantiate a User
 * Parent class of Client, Chef and Admin
 */
public class User {
    static protected String errorMsg = "";
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
    public User(String firstName, String lastName, String email, String password, Address address, UserRoles role)  throws IllegalArgumentException {
        // instantiate User's data members
        // using setters to enable validation of incoming data
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        if (password != null) {
            this.setPassword(password);
        }
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
    public User(UserEntityModel userData, Address userAddress)  throws IllegalArgumentException {
        // instantiate User's data members
        // using setters to enable validation of incoming data
        this.setFirstName(userData.getFirstName());
        this.setLastName(userData.getLastName());
        this.setEmail(userData.getEmail());
        this.setPassword(userData.getPassword());
        this.setAddress(userAddress);
        // value of role has to be one of accepted user roles
        this.setRole(userData.getRole());
        this.setUserId(userData.getUserId());
    }

    /**
     * Constructor for creating an instance of Admin (which only requires certain attributes)
     * @param userId user id of admin
     * @param firstName First name of the admin
     * @param lastName Last name of the admin
     * @param email email of the admin
     * @param role role of the user
     */
    protected User(String userId,String firstName, String lastName, String email, UserRoles role) {
        if (role != UserRoles.ADMIN) {
            throw new IllegalArgumentException("Invalid role. This constructor only supports users of type Admin");
        }

        // validate data and set values
        setUserId(userId);
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setRole(role);
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
            throw new IllegalArgumentException("Invalid first name");

        }

    }

    /**
     * this helper method checks and validates the first/last names
     * or city/country name
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

                    if (!(charsInName[i] == 45 || charsInName[i] == 32)) {

                        return false;

                    }

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
            throw new IllegalArgumentException("Invalid last name");

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
            throw new IllegalArgumentException("Invalid email address");

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
            return email.matches(emailPattern);

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

        if (password!= null){
            // Process: validating password
            if (validatePassword(password)) { //valid

                this.password = password;

            }
            else { //invalid

            // Output:
            throw new IllegalArgumentException(errorMsg);
            
            }
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
        boolean hasLetters = false;
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        char[] charsInPassword = password.toCharArray();

        // Process: checking for password length (has at least 8 letters)
        if (charsInPassword.length >= 8) { //valid

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
            if (!hasLetters) {

                errorMsg = "Password must contain at least 1 letter.";

                return false;

            }
            else if (!hasCapital) {

                errorMsg = "Password must contain at least 1 capital letter.";

                return false;


            }
            else if (!hasNumber) {

                errorMsg = "Password must contain at least 1 number.";

                return false;

            }
            else if (!hasSpecial) {

                errorMsg = "Password must contain at least 1 special character.";

                return false;

            }
            else { //valid

                return true;

            }

        }
        else { //not long enough

            errorMsg = "Password must be at least 8 characters long.";

            return false;

        }

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
     * @throws IllegalArgumentException
     */
    public void setAddress(Address address) throws IllegalArgumentException {

        if (validateAddress(address)) {

            this.address = address;

        }
        else {

            throw new IllegalArgumentException(errorMsg);

        }
    }

    private boolean validateAddress(Address address) {

        // Variable Declaration
        String streetAd = address.getStreetAddress();
        String postalCode = address.getPostalCode();
        String city = address.getCity();
        String country = address.getCountry();

        // Process: checking length of fields
        if (streetAd.length() == 0) {
            errorMsg = "Street address cannot be empty";
            return false;
        }
        else if (postalCode.length() == 0) {
            errorMsg = "Postal code cannot be empty";
            return false;
        }
        else if (city.length() == 0) {
            errorMsg = "City cannot be empty";
            return false;
        }
        else if (country.length() == 0) {
            errorMsg = "Country cannot be empty";
            return false;
        }
        else { //not empty

            // variable declaration for street address
            char[] charsInStreet = streetAd.toCharArray();
            boolean hasLetters = false;
            boolean hasNumber = false;
            boolean hasInvalidSpecial = false;

            // Process: looping through street address
            for (int i = 0; i < charsInStreet.length; i++) {

                // Process: checking char
                if (Character.isLetter(charsInStreet[i])) { //is letter

                    hasLetters = true; //updating flag

                }
                else if (Character.isDigit(charsInStreet[i])) { //is number

                    hasNumber = true; //updating flag

                }
                else { //is special char

                    if (charsInStreet[i] == 32 || charsInStreet[i] == 45 ||
                            charsInStreet[i] == 46) { //space, hyphen, or period

                        errorMsg = "Invalid characters in street address";
                        hasInvalidSpecial = false; //updating flag

                    }
                    else { //invalid special char

                        hasInvalidSpecial = true; //updating flag

                    }

                }

            }

            // Process: checking if street address is valid
            if (!(hasLetters == true && hasNumber == true && hasInvalidSpecial == false)) { //invalid

                // Process: checking for incomplete address or special chars
                if (hasInvalidSpecial) { //wrong chars
                    errorMsg = "Invalid characters in street address";
                }
                else {
                    errorMsg = "Invalid street address";
                }
                return false;

            }

            // Process: checking if postal code is valid
            if (!postalCode.matches("[A-Za-z]\\d[A-Za-z]\\d[A-Za-z]\\d")) { //invalid
                errorMsg = "Invalid postal code";
                return false;

            }

            // Process: checking if city is valid
            if (!validateName(city)) { //invalid
                errorMsg = "Invalid city";
                return false;

            }

            // Process: checking if country is valid
            if (!validateName(country)) { //invalid
                errorMsg = "Invalid country";
                return false;

            }

            return true;

        }

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
