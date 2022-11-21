package com.example.mealer_project.data.models;

import com.example.mealer_project.data.entity_models.OrderEntityModel;
import com.example.mealer_project.data.models.orders.OrderItem;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Order {

    /**
     * map of order items, from which we can retrieve meal ids & quantity
     */
    private HashMap<OrderItem, Boolean> orderItems;

    private Chef chef;
    private String clientID;
    private String orderID;
    private Date date; //order date
    private boolean isPending;
    private boolean isRejected;
    private boolean isCompleted;

    // Constructor Methods--------------------------------------------------------------------------------------
    /**
     * constructor method
     * @param chef chef who makes the meals
     * @param clientID ID of the client who placed the order
     * @param orderID ID of the order
     * @param date Date and time for when the order was placed
     * @param orderItems map of order items
     */
    public Order(Chef chef, String clientID, String orderID, Date date, HashMap<OrderItem, Boolean> orderItems){

        // Initialization
        this.setChef(chef);
        this.setClientID(clientID);
        this.setOrderID(orderID);
        this.setDate(date);

        this.setIsPending(true);
        this.setIsCompleted(false);

    }

    /**
     * second constructor
     * @param orderEntityModel entity model that contains order data
     */
    public Order(OrderEntityModel orderEntityModel) {

        this.setClientID(orderEntityModel.getClientID());
        this.setChef(orderEntityModel.getChef());
        this.setOrderID(orderEntityModel.getOrderID());
        this.setDate(new Date());
        this.setIsPending(orderEntityModel.getIsPending());
        this.setIsCompleted(orderEntityModel.getIsCompleted());

    }

    //----------------------------------------------------------------------------------------------------------
    /**
     * Sets the value of the chef
     * @param chef
     */
    public void setChef(Chef chef){
        this.chef = chef;
    }

    /**
     * Returns the chef
     * @return chef
     */
    public Chef getChef(){
        return this.chef;
    }

    //----------------------------------------------------------------------------------------------------------
    /**
     * Sets the value of the client ID
     * @param clientID
     */
    public void setClientID(String clientID){
        this.clientID = clientID;
    }

    /**
     * Returns the ID of the client
     * @return client ID
     */
    public String getClientID(){
        return this.clientID;
    }

    //----------------------------------------------------------------------------------------------------------
    /**
     * Sets the value of the order ID
     * @param orderID
     */
    public void setOrderID(String orderID){
        this.orderID = orderID;
    }

    /**
     * Return the ID of the order
     * @return order ID
     */
    public String getOrderID(){
        return this.orderID;
    }

    //----------------------------------------------------------------------------------------------------------
    /**
     * Set the date that the order was placed
     * @param date
     */
    public void setDate(Date date){
        this.date = date;
    }

    /**
     * Return the date and time that the order was placed
     * @return date of order placed
     */
    public Date getOrderDate(){
        return this.date;
    }

    //----------------------------------------------------------------------------------------------------------
    /**
     * set the map of order items
     * @param orderItems
     */
    public void setOrderItems(HashMap<OrderItem, Boolean> orderItems){
        this.orderItems = orderItems;
    }

    /**
     * Returns the map with all the order items
     * @return map of order items
     */
    public HashMap<OrderItem, Boolean> getOrderItems(){
        return this.orderItems;
    }

    //----------------------------------------------------------------------------------------------------------
    /**
     * Set the status of pending
     * @param isPending
     */
    public void setIsPending(boolean isPending){
        this.isPending = isPending;
    }

    /**
     * Returns pending status
     * @return isPending
     */
    public boolean getIsPending(){
        return this.isPending;
    }

    //----------------------------------------------------------------------------------------------------------
    /**
     * Set the status of rejection
     * @param isRejected
     */
    public void isRejected(boolean isRejected){
        this.isRejected = isRejected;
    }

    /**
     * Return rejection status
     * @return isRejected
     */
    public boolean getIsRejected(){
        return this.isRejected;
    }

    //----------------------------------------------------------------------------------------------------------
    /**
     * Set the status of completed
     * @param isCompleted
     */
    public void setIsCompleted(boolean isCompleted){
        this.isCompleted = isCompleted;
    }

    /**
     * Return completion status
     * @return isCompleted
     */
    public boolean getIsCompleted(){
        return this.isCompleted;
    }

}
