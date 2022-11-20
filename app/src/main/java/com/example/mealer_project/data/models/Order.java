package com.example.mealer_project.data.models;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Order {
    private HashMap<String, Integer> listOfMeals;
    private String chefID;
    private String clientID;
    private String orderID;
    private Date date;
    private boolean pending;
    private boolean completed;


    /**
     * constructor method
     * @param chefID ID of the chef who makes the meals
     * @param clientID ID of the client who placed the order
     * @param orderID ID of the order
     * @param date Date and time for when the order was placed
     * @param listOfMeals list of meals in the order of size 1 to many
     */
    public Order(String chefID, String clientID, String orderID, Date date, List<String> listOfMeals){
        this.setClientID(clientID);
        this.setChefID(chefID);
        this.setOrderID(orderID);
        this.setDate(date);
        this.setPending(true);
        this.isCompleted(false);
    }

    /**
     * Returns the chef ID
     * @return chef ID
     */
    public String getChefID(){
        return this.chefID;
    }

    /**
     * Returns the ID of the client
     * @return client ID
     */
    public String getClientID(){
        return this.clientID;
    }

    /**
     * Return the ID of the order
     * @return order ID
     */
    public String getOrderID(){
        return this.orderID;
    }

    /**
     * Return the date and time that the order was placed
     * @return date of order placed
     */
    public Date getOrderDate(){
        return this.date;
    }

    /**
     * Returns the order with all the list of meals
     * @return list of meals
     */
    public HashMap<String, Integer> getListOfMeals(){
        return this.listOfMeals;
    }

    /**
     * Returns pending status
     * @return pending
     */
    public boolean getPendingStatus(){
        return this.pending;
    }

    /**
     * Return completion status
     * @return completed status
     */
    public boolean getIsCompleted(){
        return this.completed;
    }

    /**
     * Sets the value of the chef ID
     * @param chefID
     */
    public void setChefID(String chefID){
        this.chefID = chefID;
    }

    /**
     * Sets the value of the client ID
     * @param clientID
     */
    public void setClientID(String clientID){
        this.clientID = clientID;
    }

    /**
     * Sets the value of the order ID
     * @param orderID
     */
    public void setOrderID(String orderID){
        this.orderID = orderID;
    }

    /**
     * Set the date that the order was placed
     * @param date
     */
    public void setDate(Date date){
        this.date = date;
    }

    /**
     * Set the list of meals for the order
     * @param listOfMeals
     */
    public void setListOfMeals(HashMap<String, Integer> listOfMeals){
        this.listOfMeals = listOfMeals;
    }

    /**
     * Set the status of pending
     * @param status
     */
    public void setPending(boolean status){
        this.pending = status;
    }

    /**
     * Set the status of completed
     * @param completed
     */
    public void isCompleted(boolean completed){
        this.completed = completed;
    }
}
