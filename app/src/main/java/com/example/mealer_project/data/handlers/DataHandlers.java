package com.example.mealer_project.data.handlers;

public class DataHandlers {
    // instance handlers
    private final UserHandler userHandler;
    private final InboxHandler inboxHandler;
    private final MealHandler mealHandler;
    private final OrderHandler orderHandler;

    // instantiate repository and handlers
    public DataHandlers() {
        this.userHandler = new UserHandler();
        this.inboxHandler = new InboxHandler();
        this.mealHandler = new MealHandler();
        this.orderHandler = new OrderHandler();
    }

    public UserHandler getUserHandler() {
        return userHandler;
    }

    public InboxHandler getInboxHandler() { return inboxHandler; }

    public MealHandler getMealHandler() {  return mealHandler; }

    public OrderHandler getOrderHandler() { return orderHandler; }
}
