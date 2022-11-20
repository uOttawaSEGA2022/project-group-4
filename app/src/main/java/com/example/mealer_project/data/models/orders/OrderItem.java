package com.example.mealer_project.data.models.orders;

public class OrderItem {
    private SearchMealItem searchMealItem;
    private int quantity;

    public OrderItem(SearchMealItem searchMealItem, int quantity) {
        this.searchMealItem = searchMealItem;
        this.quantity = quantity;
    }

    public SearchMealItem getSearchMealItem() {
        return searchMealItem;
    }

    public void setSearchMealItem(SearchMealItem searchMealItem) {
        this.searchMealItem = searchMealItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}