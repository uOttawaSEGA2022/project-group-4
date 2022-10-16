package com.example.mealer_project.data.model;

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
    private Date expiry;

    /**
     * Create a credit card object to store information, without a client id
     * @param brand the card association brand (ex: MasterCard, Visa, etc.)
     * @param name name of the card holder
     * @param number credit card's number
     * @param cvc CVC code of the credit card
     * @param expiry expiry date of the credit card
     */
    public CreditCard(String brand, String name, String number, int cvc, Date expiry) {
        // using setters to enable data validation
        this.setBrand(brand);
        this.setName(name);
        this.setNumber(number);
        this.setCvc(cvc);
        this.setExpiry(expiry);
    }

    /**
     * Create a credit card object to store information of a client's credit card
     * @param clientId id of client whom the credit card belongs to
     * @param brand the card association brand (ex: MasterCard, Visa, etc.)
     * @param name name of the card holder
     * @param number credit card's number
     * @param cvc CVC code of the credit card
     * @param expiry expiry date of the credit card
     */
    public CreditCard(String clientId, String brand, String name, String number, int cvc, Date expiry) {
        // using setters to enable data validation
        this.setClientId(clientId);
        this.setBrand(brand);
        this.setName(name);
        this.setNumber(number);
        this.setCvc(cvc);
        this.setExpiry(expiry);
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

    public void setNumber(String number) {

        // Process: validating the number
        if (validateNumber(number)) { //valid

            this.number = number;

        }
        else { //invalid

            // Output: error msg
            System.out.println("error msg");

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

    public void setCvc(int cvc) {
        this.cvc = cvc;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    /**/

}
