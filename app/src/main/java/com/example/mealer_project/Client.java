package com.example.mealer_project;

import java.util.Date;

public class Client extends User {
    private CreditCard clientCreditCard;
    /**
     * Create a Client object
     * @param firstName First name of the client
     * @param lastName Last name of the client
     * @param email email of the client
     * @param password password for the client
     * @param address address of the client
     * @param role Role of the client
     * @param clientCreditCard credit card info of the client
     */
    Client(String firstName, String lastName, String email, String password, String address, UserRoles role, CreditCard clientCreditCard) {
        // instantiate Client's data members
        super(firstName, lastName, email, password, address, role);
        // userId should have been created for the client by this point
        this.setClientCreditCard(clientCreditCard);
    }

    /**
     * Get the client's credit card
     * @return CreditCard object
     */
    public CreditCard getClientCreditCard() {
        return clientCreditCard;
    }

    /**
     * Method to add credit card for a client
     * @param clientCreditCard a CreditCard object
     */
    public void setClientCreditCard(CreditCard clientCreditCard) {
        this.clientCreditCard = clientCreditCard.setClientId(this.userId);
    }

    /**
     * Method to add or update a credit card for a client
     * @param brand card association brand (ex: MasterCard, Visa, etc.)
     * @param name name of the card holder
     * @param number credit card number
     * @param cvc CVC code
     * @param expiry credit card expiry
     */
    public void setClientCreditCard(String brand, String name, String number, int cvc, Date expiry) {
        // validate provided card info
        // verify client has been instantiated (has valid userId)
        this.clientCreditCard = new CreditCard(this.userId, brand, name, number, cvc, expiry);
    }

    /**
     * Method for a client to be able to buy an item
     */
    public void buyItem() {
        // logic to initiate a request to buy an item
    }

    /**
     * Method for a client to be able to rate a meal
     */
    public void rateMeal() {
        // logic for a client to rate a meal
    }
}
