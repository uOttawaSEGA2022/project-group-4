package com.example.mealer_project.data.models;

import com.example.mealer_project.data.entity_models.CreditCardEntityModel;

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
    private int cvc;
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

    /**
     * Create a credit card object to store information, without a client id
     * @param brand the card association brand (ex: MasterCard, Visa, etc.)
     * @param name name of the card holder
     * @param number credit card's number
     * @param cvc CVC code of the credit card
     * @param expiryMonth expiry month of the credit card
     * @param expiryYear expiry year of the credit card
     */
    public CreditCard(String brand, String name, String number, int cvc, int expiryMonth, int expiryYear) {
        // using setters to enable data validation
        this.setBrand(brand);
        this.setName(name);
        this.setNumber(number);
        this.setCvc(cvc);
        this.setExpiryMonth(expiryMonth);
        this.setExpiryYear(expiryYear);
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
    public CreditCard(String clientId, String brand, String name, String number, int cvc, int expiryMonth, int expiryYear) {
        // using setters to enable data validation
        this.setClientId(clientId);
        this.setBrand(brand);
        this.setName(name);
        this.setNumber(number);
        this.setCvc(cvc);
        this.setExpiryMonth(expiryMonth);
        this.setExpiryYear(expiryYear);
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

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
            throw new IllegalArgumentException("Incorrect credit card number");

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

        // Variable Declaration
        char[] charsInNumber = number.toCharArray();

        // Process: looping through number
        for (int i = 0; i < charsInNumber.length; i++) {

            // Process: checking for numbers only
            if (!Character.isDigit(charsInNumber[i])) { //is not number

                return false;

            }

        }

        return true;

    }

    public int getCvc() {
        return cvc;
    }

    public void setCvc(int cvc) throws IllegalArgumentException {
        // Process: validating the cvc number
        if (validateCVC(cvc)) { //valid

            this.cvc = cvc;

        }
        else { //invalid

            // Output: error msg
            throw new IllegalArgumentException("Invalid cvc");

        }
    }

    /**
     * This method validates the CVC
     * @param number
     * @return valid or not
     */
    private boolean validateCVC(int number) {
        if (number>99 && number<1000 )
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
        if (year >= currentYear )
            return true;
        return false;
    }

}
