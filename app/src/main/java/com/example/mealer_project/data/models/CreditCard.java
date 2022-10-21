package com.example.mealer_project.data.models;

import com.example.mealer_project.R;
import com.example.mealer_project.data.entity_models.CreditCardEntityModel;
import com.example.mealer_project.ui.SignupActivity;

import java.util.Calendar;
import java.util.Date;

/**
 * Class to store information of a client's credit card
 */
public class CreditCard {
    private String clientId;
    private String brand;
    private String name;
    private String number;
    private String cvc;
    private int expiryMonth;
    private int expiryYear;

    /**
     * Create a credit card object to store information, without a client id
     * @param creditCardEntityModel a CreditCardEntityModel object containing unvalidated credit card details
     */
    public CreditCard(CreditCardEntityModel creditCardEntityModel) {
        // using setters to enable data validation
        this.setCreditCardData(creditCardEntityModel.getBrand(), creditCardEntityModel.getName(),
                creditCardEntityModel.getNumber(), creditCardEntityModel.getCvc(),
                creditCardEntityModel.getExpiryMonth(), creditCardEntityModel.getExpiryYear());
    }

    /**
     * Create a credit card object to store information, without a client id
     * @param brand the card association brand (ex: MasterCard, Visa, etc.)
     * @param name name of the card holder
     * @param number credit card's number
     * @param cvc CVC code of the credit card
     * @param expiryMonth expiry month of the credit card
     * @param expiryYear expiry year of the credit card
     */
    public CreditCard(String brand, String name, String number, String cvc, int expiryMonth, int expiryYear) {
        // using setters to enable data validation
        this.setCreditCardData(brand, name, number, cvc, expiryMonth, expiryYear);
    }

    /**
     * Create a credit card object to store information of a client's credit card
     * @param clientId id of client whom the credit card belongs to
     * @param brand the card association brand (ex: MasterCard, Visa, etc.)
     * @param name name of the card holder
     * @param number credit card's number
     * @param cvc CVC code of the credit card
     * @param expiryMonth expiry month of the credit card
     * @param expiryYear expiry year of the credit card
     */
    public CreditCard(String clientId, String brand, String name, String number, String cvc, int expiryMonth, int expiryYear) {
        // using setters to enable data validation
        this.setClientId(clientId);
        this.setCreditCardData(brand, name, number, cvc, expiryMonth, expiryYear);
    }

    public String getClientId() {
        return clientId;
    }

    /**
     *  Method to set client for the credit card;
     * @param clientId user id of the client
     * @return returns current instance of credit card object to enable method chaining
     */
    public CreditCard setClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    private void setCreditCardData(String brand, String name, String number, String cvc, int expiryMonth, int expiryYear) throws IllegalArgumentException {

        // Variable Declaration
        boolean hasError = false;

        // Process: checking for errors
        if (!setBrand(brand)) //invalid brand
            hasError = true;
        if (!setName(name)) //invalid card holder name
            hasError = true;
        if (!setNumber(number)) //invalid card number
            hasError = true;
        if (!setCvc(cvc)) //invalid CVC code
            hasError = true;
        if (!setExpiryMonth(expiryMonth)) //invalid expiration month
            hasError = true;
        if (!setExpiryYear(expiryYear)) //invalid expiration year
            hasError = true;

        // Output
        if (hasError) //exception
            throw new IllegalArgumentException("Sign-up unsuccessful!");

    }

    public String getBrand() {
        return brand;
    }

    private boolean setBrand(String brand) {

        if (validateName(brand, 0)) { //entered field
            this.brand = brand;
            return true;
        }
        else { //empty field
            return false;
        }

    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {

        // Process: validating the name
        if (validateName(name, 2)) { //valid

            this.name = name;
            return true;

        }
        else { //invalid

            return false;

        }
    }

    /**
     * this helper method checks and validates the name inputted by the user
     *
     * @param name
     *  the name entered
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

                        SignupActivity.getCurrentFieldsCredit(index).setBackgroundResource(R.drawable.edterror);

                        SignupActivity.getCurrentFieldsCredit(index).setError("Illegal characters in field"); //setting error icon & msg

                        return false;

                    }

                }

            }

            SignupActivity.getCurrentFieldsCredit(index).setBackgroundResource(R.drawable.edtnormal);

            SignupActivity.getCurrentFieldsCredit(index).setError(null); //setting error icon & msg

            return true;

        }
        else { //nothing inputted

            SignupActivity.getCurrentFields(index).setBackgroundResource(R.drawable.edterror);

            SignupActivity.getCurrentFields(index).setError("Field cannot be empty"); //setting error icon & msg

            return false;

        }

    }

    public String getNumber() {
        return number;
    }

    /**
     * Sets the credit card number
     * @param number
     * @return whether the set is successful
     */
    public boolean setNumber(String number) {

        // Process: validating the number
        if (validateNumber(number)) { //valid

            this.number = number;
            return true;

        }
        else { //invalid

            return false;

        }
    }

    /**
     * this helper method checks and validates the credit card number inputted by the user
     *
     * @param number
     *  the number entered
     *
     * @return
     *  whether the number is valid
     */
    private boolean validateNumber(String number) {

        if (number.length() > 0) { //not empty

            // Variable Declaration
            char[] charsInNumber = number.toCharArray();

            // Process: looping through number
            for (int i = 0; i < charsInNumber.length; i++) {

                // Process: checking for numbers only
                if (!Character.isDigit(charsInNumber[i])) { //is not number

                    SignupActivity.getCurrentFieldsCredit(1).setBackgroundResource(R.drawable.edterror);

                    SignupActivity.getCurrentFieldsCredit(1).setError("Must only contain numbers"); //setting error icon & msg

                    return false;

                }
                else if (!(charsInNumber.length >= 13 && charsInNumber.length <= 16)) { //wrong length

                    SignupActivity.getCurrentFieldsCredit(1).setBackgroundResource(R.drawable.edterror);

                    SignupActivity.getCurrentFieldsCredit(1).setError("Number must be between 13 to 16 digits long"); //setting error icon & msg

                    return false;

                }

            }

            SignupActivity.getCurrentFieldsCredit(1).setBackgroundResource(R.drawable.edtnormal);

            SignupActivity.getCurrentFieldsCredit(1).setError(null); //setting error icon & msg

            return true;

        }
        else { //empty

            SignupActivity.getCurrentFieldsCredit(1).setBackgroundResource(R.drawable.edterror);

            SignupActivity.getCurrentFieldsCredit(1).setError("Credit card number cannot be empty"); //setting error icon & msg

            return false;

        }

    }

    public String getCvc() {
        return cvc;
    }

    public boolean setCvc(String cvc) {
        // Process: validating the cvc number
        if (validateCVC(cvc)) { //valid

            this.cvc = cvc;
            return true;

        }
        else { //invalid

            return false;

        }
    }

    /**
     * This method validates the CVC
     * @param number
     * @return valid or not
     */
    private boolean validateCVC(String number) {
        /*if (number.length() == 3) {
            SignupActivity.getCurrentFieldsCredit(5).setBackgroundResource(R.drawable.edtnormal);
            SignupActivity.getCurrentFieldsCredit(5).setError(null); //setting error icon & msg
            return true;
        }
        else {
            SignupActivity.getCurrentFieldsCredit(5).setBackgroundResource(R.drawable.edterror);
            SignupActivity.getCurrentFieldsCredit(5).setError("Must be 3 digits long"); //setting error icon & msg
            return false;
        }*/
        return true;
    }

    public int getExpiryMonth() {
        return expiryMonth;
    }

    /**
     * Sets the expiry month of the credit card after validating
     * @param expiryMonth
     */
    public boolean setExpiryMonth(int expiryMonth) {
            // Process: validating the expiry month
            if (validateExpiryMonth(expiryMonth)) { //valid

                this.expiryMonth = expiryMonth;
                return true;

            }
            else { //invalid

                return false;

            }
        }

    /**
     * Validates the expiry month
     * @param month
     * @return valid or not
     */
    private boolean validateExpiryMonth(int month) {
            if (month>=1 && month<=12 ) {
                SignupActivity.getCurrentFieldsCredit(3).setBackgroundResource(R.drawable.edtnormal);
                SignupActivity.getCurrentFieldsCredit(3).setError(null); //setting error icon & msg
                return true;
            }
            SignupActivity.getCurrentFieldsCredit(3).setBackgroundResource(R.drawable.edterror);
            SignupActivity.getCurrentFieldsCredit(3).setError("Month must be between 1 and 12"); //setting error icon & msg
            return false;
        }

    public int getExpiryYear() {
        return expiryYear;
    }

    /**
     * Sets the expiry year of the credit card
     * @param expiryYear
     */
    public boolean setExpiryYear(int expiryYear) {
        // Process: validating the expiry year
        if (validateExpiryYear(expiryYear)) { //valid

            this.expiryYear = expiryYear;
            return true;

        }
        else { //invalid

            return false;

        }
    }

    /**
     * Validates the expiry date
     * @param year
     * @return valid or not
     */
    private boolean validateExpiryYear(int year) {
        // Variable Declaration
        int currentYear = Calendar.getInstance().get(Calendar.YEAR)%2000;

        if (year >= currentYear) {
            SignupActivity.getCurrentFieldsCredit(4).setBackgroundResource(R.drawable.edtnormal);
            SignupActivity.getCurrentFieldsCredit(4).setError(null); //setting error icon & msg
            return true;
        }
        SignupActivity.getCurrentFieldsCredit(4).setBackgroundResource(R.drawable.edterror);
        SignupActivity.getCurrentFieldsCredit(4).setError("Must be a valid expiration year"); //setting error icon & msg
        return false;
    }

}
