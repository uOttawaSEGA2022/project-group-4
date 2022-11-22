package com.example.mealer_project.data.models;

import com.example.mealer_project.data.entity_models.OrderEntityModel;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.data.models.orders.ChefInfo;
import com.example.mealer_project.data.models.orders.ClientInfo;
import com.example.mealer_project.data.models.orders.OrderItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Order {

    private String orderID;
    private ChefInfo chefInfo;
    private ClientInfo clientInfo;
    private Map<String, Meal> meals;
    private Date date; //order date
    private boolean isPending;
    private boolean isRejected;
    private boolean isCompleted;

    // Constructor Methods--------------------------------------------------------------------------------------

    /**
     * Constructor to initialize an empty order
     */
    public Order() {
        this.meals = new HashMap<>();
        this.setIsPending(true);
        this.setIsRejected(false);
        this.setIsCompleted(false);
    }

    /**
     * constructor method
     * @param orderID ID of the order
     * @param chefInfo info of chef who makes the meals
     * @param clientInfo info of the client who placed the order
     * @param meals List of meals part of the order
     * @param date Date and time for when the order was placed
     */
    public Order(String orderID, ChefInfo chefInfo, ClientInfo clientInfo, Map<String, Meal> meals, Date date){
        // Initialization
        this.setOrderID(orderID);
        this.setChefInfo(chefInfo);
        this.setClientInfo(clientInfo);
        this.setMeal(meals);
        this.setDate(date);
        this.setIsPending(true);
        this.setIsRejected(false);
        this.setIsCompleted(false);
    }

    /**
     * Constructor to create an instance of an Order without order ID (ex: when creating new order locally)
     * @param chefInfo info of chef who makes the meals
     * @param clientInfo info of the client who placed the order
     * @param meals List of meals part of the order
     * @param date Date and time for when the order was placed
     */
    public Order(ChefInfo chefInfo, Client clientInfo, Map<String, Meal> meals, Date date){
        this.setChefInfo(chefInfo);
        this.setClientInfo(clientInfo);
        this.setMeal(meals);
        this.setDate(date);
        this.setIsPending(true);
        this.setIsRejected(false);
        this.setIsCompleted(false);
    }

    //----------------------------------------------------------------------------------------------------------

    // Chef Info

    public void setChefInfo(Chef chef){
        this.chefInfo = new ChefInfo(chef);
    }

    public void setChefInfo(ChefInfo chefInfo){
        this.chefInfo = chefInfo;
    }

    public ChefInfo getChefInfo(){
        return this.chefInfo;
    }

    // Client Info

    public void setClientInfo(Client client) {
        this.clientInfo = new ClientInfo(client);
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    //----------------------------------------------------------------------------------------------------------
    /**
     * Sets the value of the meal
     */
    public void setMeal(Map<String, Meal> meals) {
        this.meals = meals;
    }

    public void addMeal(String mealId, Meal meal) {
        this.meals.put(mealId, meal);
    }

    public Map<String, Meal> getMeals() {
        return meals;
    }

    /**
     * Returns the id of an invalid meal within the Order's meals map
     * Invalid meal is where for a given mealId key, the value is null
     * @return non-null, non-empty string if an invalid meal found, else null
     */
    public String getIdOfInvalidMeal() {
        for (String mealId: this.meals.keySet()) {
            if (this.meals.get(mealId) == null) {
                return mealId;
            }
        }
        return null;
    }



    //----------------------------------------------------------------------------------------------------------

    public boolean isRejected() {
        return isRejected;
    }

    public void setIsRejected(boolean rejected) {
        isRejected = rejected;
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
