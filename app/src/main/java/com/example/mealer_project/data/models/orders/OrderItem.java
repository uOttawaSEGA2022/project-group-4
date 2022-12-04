package com.example.mealer_project.data.models.orders;

import com.example.mealer_project.ui.screens.search.SearchMealItem;

import java.io.Serializable;

public class OrderItem implements Serializable {
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

    /*@Override
    public boolean equals(Object o) {

        // Process: checking if comparing to self
        if (o == this) {
            // Output
            return true;
        }

        // Process: checking if comparing another order item
        if (!(o instanceof OrderItem)) { //not order item
            // Output
            return false;
        }

        // Variable Declaration
        OrderItem orderItem = (OrderItem) o;

        // Output: checking if ids are the same
        return orderItem.getSearchMealItem().getId().equals(this.getSearchMealItem().getId());

    }*/
}
