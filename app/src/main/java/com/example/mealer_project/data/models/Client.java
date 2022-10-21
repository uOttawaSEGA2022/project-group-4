package com.example.mealer_project.data.models;

import com.example.mealer_project.data.entity_models.UserEntityModel;

import java.util.Date;

/**
 * This class instantiates an instance of Client for Mealer App
 * Child Class of User
 */
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
    public Client(String firstName, String lastName, String email, String password, Address address, UserRoles role, CreditCard clientCreditCard) {
        // instantiate Client's data members
        super(firstName, lastName, email, password, address, role);
        // userId should have been created for the client by this point
        this.setClientCreditCard(clientCreditCard);
    }

    /**
     * Create a Client object
     * @param clientData a UserEntityModel object containing unvalidated user details
     * @param clientAddress an Address object containing validated address info
     * @param clientCreditCard credit card info of the client
     */
    public Client(UserEntityModel clientData, Address clientAddress, CreditCard clientCreditCard) {
        // instantiate Client's data members
        super(clientData, clientAddress);
        this.setClientCreditCard(clientCreditCard);
    }



    @Override
    public void setUserId(String userId) {
        super.setUserId(userId);
        clientCreditCard.setClientId(userId);
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
        this.clientCreditCard = clientCreditCard;
        // update user id of new credit card as well, if user id exists (i.e., user registered on database)
        if (this.getUserId() != null && !this.getUserId().equals("")) {
            this.clientCreditCard.setClientId(this.getUserId());
        }
    }

    /**
     * Method to add or update a credit card for a client
     * @param brand card association brand (ex: MasterCard, Visa, etc.)
     * @param name name of the card holder
     * @param number credit card number
     * @param cvc CVC code
     * @param expiryMonth expiry month of the credit card
     * @param expiryYear expiry year of the credit card
     */
    public void setClientCreditCard(String brand, String name, String number, String cvc, int expiryMonth, int expiryYear) {
        // validate provided card info
        // verify client has been instantiated (has valid userId)
        this.clientCreditCard = new CreditCard(this.userId, brand, name, number, cvc, expiryMonth, expiryYear);
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
