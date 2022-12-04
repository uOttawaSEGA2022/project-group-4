package com.example.mealer_project.data.models;

import com.example.mealer_project.data.entity_models.CreditCardEntityModel;

import java.util.Calendar;

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
        this.setBrand(creditCardEntityModel.getBrand());
        this.setName(creditCardEntityModel.getName());
        this.setNumber(creditCardEntityModel.getNumber());
        this.setCvc(creditCardEntityModel.getCvc());
        this.setExpiryMonth(creditCardEntityModel.getExpiryMonth());
        this.setExpiryYear(creditCardEntityModel.getExpiryYear());
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) throws IllegalArgumentException {

        if (brand.length() > 0) { //entered field
            this.brand = brand;
        }
        else { //empty field
            throw new IllegalArgumentException("Please enter a credit card brand name.");
        }

    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws IllegalArgumentException {

        // Process: validating the name
        if (validateName(name)) { //valid

            this.name = name;

        }
        else { //invalid

            // Output: error msg
            throw new IllegalArgumentException("Invalid card holder name");

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
    private boolean validateName(String name) {

        if (name.length() > 0) { //not empty

            // Variable Declaration
            char[] charsInName = name.toCharArray();

            // Process: looping through the name
            for (int i = 0; i < charsInName.length; i++) {

                if (!((charsInName[i] >= 65 && charsInName[i] <= 90) ||
                        (charsInName[i] >= 97 && charsInName[i] <= 122) ||
                        (charsInName[i] == 32))) { //only letters or space

                    return false;

                }

            }

            return true;

        }
        else { //empty field

            return false;

        }

    }

    public String getNumber() {
        return number;
    }

    /**
     * Sets the credit card number
     * @param number
     * @throws IllegalArgumentException
     */
    public void setNumber(String number) throws IllegalArgumentException {

        // Process: validating the number
        if (validateNumber(number)) { //valid

            this.number = number;

        }
        else { //invalid

            // Output: error msg
            throw new IllegalArgumentException("Invalid credit card number");

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

                    return false;

                }
                else if (!(charsInNumber.length >= 13 && charsInNumber.length <= 16)) { //wrong length

                    return false;

                }

            }

            return true;

        }
        else { //empty

            return false;

        }

    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) throws IllegalArgumentException {
        // Process: validating the cvc number
        if (validateCVC(cvc)) { //valid

            this.cvc = cvc;

        }
        else { //invalid

            // Output: error msg
            throw new IllegalArgumentException("Invalid CVC");

        }
    }

    /**
     * This method validates the CVC
     * @param number
     * @return valid or not
     */
    private boolean validateCVC(String number) {
        if (true )
            return true;
        return false;
    }

    public int getExpiryMonth() {
        return expiryMonth;
    }

    /**
     * Sets the expiry month of the credit card after validating
     * @param expiryMonth
     * @throws IllegalArgumentException
     */
    public void setExpiryMonth(int expiryMonth) throws IllegalArgumentException {
            // Process: validating the expiry month
            if (validateExpiryMonth(expiryMonth)) { //valid

                this.expiryMonth = expiryMonth;

            }
            else { //invalid

                // Output: error msg
                throw new IllegalArgumentException("Invalid expiry month");

            }
        }

    /**
     * Validates the expiry month
     * @param month
     * @return valid or not
     */
    private boolean validateExpiryMonth(int month) {
            if (month>=1 && month<=12 )
                return true;
            return false;
        }

    public int getExpiryYear() {
        return expiryYear;
    }

    /**
     * Sets the expiry year of the credit card
     * @param expiryYear
     * @throws IllegalArgumentException
     */
    public void setExpiryYear(int expiryYear) throws IllegalArgumentException {
        // Process: validating the expiry year
        if (validateExpiryYear(expiryYear)) { //valid

            this.expiryYear = expiryYear;

        }
        else { //invalid

            // Output: error msg
            throw new IllegalArgumentException("Invalid expiry year");

        }
    }

    /**
     * Validates the expiry date
     * @param year
     * @return valid or not
     */
    private boolean validateExpiryYear(int year) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR)%2000;
        if (year >= currentYear)
            return true;
        return false;
    }
}
