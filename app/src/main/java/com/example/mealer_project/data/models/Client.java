package com.example.mealer_project.data.models;

import com.example.mealer_project.data.entity_models.UserEntityModel;
import com.example.mealer_project.data.models.orders.OrderItem;
import com.example.mealer_project.ui.screens.search.SearchMeals;
import com.example.mealer_project.utils.Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * This class instantiates an instance of Client for Mealer App
 * Child Class of User
 */
public class Client extends User {

    /**
     * Stores order items in cart
     */
    Map<OrderItem, Boolean> cart;
    public final Orders ORDERS;

    private CreditCard clientCreditCard;

    private SearchMeals searchMeals;
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
    public Client(String firstName, String lastName, String email, String password, Address address, UserRoles role, CreditCard clientCreditCard)  throws IllegalArgumentException {
        // instantiate Client's data members
        super(firstName, lastName, email, password, address, role);
        // userId should have been created for the client by this point
        this.setClientCreditCard(clientCreditCard);
        this.cart = new HashMap<>(); //empty cart
        this.ORDERS = new Orders();
        // instantiate a SearchMeals instance to store searchable meals
        this.searchMeals = new SearchMeals();
    }

    /**
     * Create a Client object
     * @param clientData a UserEntityModel object containing unvalidated user details
     * @param clientAddress an Address object containing validated address info
     * @param clientCreditCard credit card info of the client
     */
    public Client(UserEntityModel clientData, Address clientAddress, CreditCard clientCreditCard) throws IllegalArgumentException {
        // instantiate Client's data members
        super(clientData, clientAddress);
        this.setClientCreditCard(clientCreditCard);

        this.cart = new HashMap<>(); //empty cart
        this.ORDERS = new Orders();
        // instantiate a SearchMeals instance to store searchable meals
        this.searchMeals = new SearchMeals();
    }

    @Override
    public void setUserId(String userId) {
        super.setUserId(userId);
        // if we have a valid credit card, update client ID there as well
        if (clientCreditCard != null) {
            clientCreditCard.setClientId(userId);
        }
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
     * Update order items in cart
     * @param orderItem instance of OrderItem
     */
    public void updateOrderItem(OrderItem orderItem) {
        if (orderItem.getQuantity() == 0) {
            this.cart.remove(orderItem);
        } else {
            this.cart.put(orderItem, true);
        }
    }

    /**
     * this method completely clears the cart
     */
    public void clearCart() {
        this.cart.clear(); //cart cleared

    }

    public Map<OrderItem, Boolean> getCart() {
        return cart;
    }

    public void setCart(Map<OrderItem, Boolean> cart) {
        this.cart = cart;
    }

    /**
     * method to get order item information of a meal present in client's cart
     * @return if meal present returns an instance of that Meal, else null
     */
    public OrderItem getOrderItem(String mealId) {
        // cart should not be null or empty
        if (Preconditions.isNotNull(this.cart) && !this.cart.isEmpty()) {
            for (OrderItem orderItem : this.cart.keySet()) {
                // if current order item has the meal we're looking for
                if (orderItem.getSearchMealItem().getMeal().getMealID().equals(mealId)) {
                    // return the order item
                    return orderItem;
                }
            }
        }
        // if cart is empty or no order item found for the meal
        return null;
    }

    /**
     * Method for a client to be able to rate a meal
     */
    public void rateMeal() {
        // logic for a client to rate a meal
    }

    public SearchMeals getSearchMeals() {
        return searchMeals;
    }
}
