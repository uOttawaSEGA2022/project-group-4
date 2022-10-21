package com.example.mealer_project.data.models;

import com.example.mealer_project.R;
import com.example.mealer_project.data.entity_models.UserEntityModel;
import com.example.mealer_project.ui.SignupActivity;

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
        this.setUserData(firstName, lastName, email, password, address);
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
    public User(UserEntityModel userData, Address userAddress)  {
        // instantiate User's data members
        // using setters to enable validation of incoming data
        this.setUserData(userData.getFirstName(),
                userData.getLastName(),
                userData.getEmail(),
                userData.getPassword(),
                userAddress);
        // value of role has to be one of accepted user roles
        this.setRole(userData.getRole());
    }

    /**
     * this helper method calls the set methods for all the user data
     * and checks for errors, throwing an exception when one is found
     * @param firstName the first name entered by the user
     * @param lastName the last name entered by the user
     * @param email the email
     * @param password the password
     * @param address the address
     * @throws IllegalArgumentException
     */
    private void setUserData(String firstName, String lastName, String email, String password, Address address) throws IllegalArgumentException {

        // Variable Declaration
        boolean hasError = false;

        // Process: checking for errors
        if (!setFirstName(firstName)) //invalid first name
            hasError = true;
        if (!setLastName(lastName)) //invalid last name
            hasError = true;
        if (!setEmail(email)) //invalid email
            hasError = true;
        if (!setPassword(password)) //invalid password
            hasError = true;
        if (!setAddress(address)) //invalid address
            hasError = true;

        // Output
        if (hasError) //exception
            throw new IllegalArgumentException("Sign-up unsuccessful!");

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
     * @return whether the set is successful
     */
    private boolean setFirstName(String firstName) {

        if (validateName(firstName, 0)) { //valid

            this.firstName = firstName; //setting name

            return true;

        }
        else {

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
     * @return whether the set is successful
     */
    private boolean setLastName(String lastName) {

        if (validateName(lastName, 1) == true) { //valid

            this.lastName = lastName; //setting name

            return true;

        }
        else {

            return false;

        }

    }

    /**
     * this helper method checks and validates the first/last names
     * or city/country name
     *
     * @param name
     *  the name inputted
     *
     * @param index
     *  the index representing the text field being manipulated
     *
     * @return
     *  whether the name is valid
     */
    private boolean validateName(String name, int index) {

        // Process: checking name length
        if (name.length() > 0) { //at least 1 char

            // Variable Declaration
            char[] charsInName = name.toCharArray();

            // Process: validating input
            for (int i = 0; i < charsInName.length; i++) {

                // Process: checking for all letters
                if (!Character.isLetter(charsInName[i])) { //is not letter

                    if (!(charsInName[i] == 45 || charsInName[i] == 32)) {

                        //SignupActivity.getCurrentFields(index).setBackgroundResource(R.drawable.edterror);
                        SignupActivity.getCurrentFields(index).setError("Illegal characters in name"); //setting error icon & msg

                        return false;

                    }

                }

            }

            //SignupActivity.getCurrentFields(index).setBackgroundResource(R.drawable.edtnormal);
            SignupActivity.getCurrentFields(index).setError(null); //setting error icon & msg

            return true;

        }
        else { //nothing inputted

            //SignupActivity.getCurrentFields(index).setBackgroundResource(R.drawable.edterror);
            SignupActivity.getCurrentFields(index).setError("Name cannot be empty"); //setting error icon & msg

            return false;

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
     * @return whether the set is successful
     */
    public boolean setEmail(String email) {

        // Process: checking if email is valid
        if (validateEmail(email)) { //valid

            this.email = email; //setting email

            return true;

        }
        else {

            return false;

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

               // SignupActivity.getCurrentFields(2).setBackgroundResource(R.drawable.edtnormal);
                SignupActivity.getCurrentFields(2).setError(null); //setting error icon & msg

                return true;

            } else { //invalid

               // SignupActivity.getCurrentFields(2).setBackgroundResource(R.drawable.edterror);
                SignupActivity.getCurrentFields(2).setError("Invalid email address"); //setting error icon & msg

                return false;

            }

        }
        else { //empty field

            //SignupActivity.getCurrentFields(2).setBackgroundResource(R.drawable.edterror);
            SignupActivity.getCurrentFields(2).setError("Email cannot be empty"); //setting error icon & msg

            return false;

        }

    }

    /**
     * Set user's password
     * @param password String representing user's password
     * @return whether the set is successful
     */
    public boolean setPassword(String password) {

        // Process: validating password
        if (validatePassword(password)) { //valid

            //SignupActivity.getCurrentFields(3).setBackgroundResource(R.drawable.edtnormal);
            SignupActivity.getCurrentFields(3).setError(null); //setting error icon & msg

            this.password = password;

            return true;

        }
        else { //invalid

            //SignupActivity.getCurrentFields(3).setBackgroundResource(R.drawable.edterror);
            SignupActivity.getCurrentFields(3).setError("A good password should contain:\n" +
                    "at least 8 characters,\n" +
                    "at least 1 capital,\n" +
                    "at least 1 number, &\n" +
                    "at least 1 special character"); //setting error icon & msg

            return false;

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
            if (!hasLetters) {

                return false;

            }
            else if (!hasCapital) {

                return false;


            }
            else if (!hasNumber) {

                return false;

            }
            else if (!hasSpecial) {

                return false;

            }
            else { //valid

                return true;

            }

        }
        else { //not long enough

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
     * @return whether the set is successful
     */
    public boolean setAddress(Address address) {

        if (validateAddress(address)) {

            this.address = address;

            return true;

        }
        else {

            return false;

        }

    }

    private boolean validateAddress(Address address) {

        // Variable Declaration
        String streetAd = address.getStreetAddress();
        String postalCode = address.getPostalCode();
        String city = address.getCity();
        String country = address.getCountry();

        // Process: checking length of fields
        if (streetAd.length() <= 0 || postalCode.length() <= 0 || city.length() <= 0 ||
                country.length() <= 0) { //empty

            if (streetAd.length() == 0) {
               // SignupActivity.getCurrentFields(4).setBackgroundResource(R.drawable.edterror);
                SignupActivity.getCurrentFields(4).setError("Street address cannot be empty"); //setting error icon & msg
            }if (postalCode.length() == 0) {
               // SignupActivity.getCurrentFields(5).setBackgroundResource(R.drawable.edterror);
                SignupActivity.getCurrentFields(5).setError("City cannot be empty"); //setting error icon & msg
            }if (city.length() == 0){
                //SignupActivity.getCurrentFields(6).setBackgroundResource(R.drawable.edterror);
                SignupActivity.getCurrentFields(6).setError("Postal code cannot be empty"); //setting error icon & msg
            }if (country.length() == 0) {
               // SignupActivity.getCurrentFields(7).setBackgroundResource(R.drawable.edterror);
                SignupActivity.getCurrentFields(7).setError("Country cannot be empty"); //setting error icon & msg
            }
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
                    SignupActivity.getCurrentFields(4).setError("Invalid characters in street address"); //setting error icon & msg
                }
                else {
                    SignupActivity.getCurrentFields(4).setError("Must contain both street number and street name"); //setting error icon & msg
                }

                return false;

            }

            // Process: checking if postal code is valid
            if (!postalCode.matches("[A-Za-z]\\d[A-Za-z]\\d[A-Za-z]\\d")) { //invalid

                SignupActivity.getCurrentFields(6).setError("Invalid postal code"); //setting error icon & msg

                return false;

            }

            // Process: checking if city is valid
            if (!validateName(city, 5)) { //invalid

                return false;

            }

            // Process: checking if country is valid
            if (!validateName(country, 7)) { //invalid

                return false;

            }

           // SignupActivity.getCurrentFields(4).setBackgroundResource(R.drawable.edtnormal);
            SignupActivity.getCurrentFields(4).setError(null); //setting error icon & msg
            //SignupActivity.getCurrentFields(5).setBackgroundResource(R.drawable.edtnormal);
            SignupActivity.getCurrentFields(5).setError(null); //setting error icon & msg
            //SignupActivity.getCurrentFields(6).setBackgroundResource(R.drawable.edtnormal);
            SignupActivity.getCurrentFields(6).setError(null); //setting error icon & msg
            //SignupActivity.getCurrentFields(7).setBackgroundResource(R.drawable.edtnormal);
            SignupActivity.getCurrentFields(7).setError(null); //setting error icon & msg

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
