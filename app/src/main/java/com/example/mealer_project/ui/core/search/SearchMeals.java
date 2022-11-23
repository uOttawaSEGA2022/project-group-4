package com.example.mealer_project.ui.core.search;

import com.example.mealer_project.ui.core.ListDataObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchMeals {

    Map<String, SearchMealItem> searchMealItems;
    // store a reference to the SearchMealScreen so it could be notified of the updates to the list
    ListDataObserver dataObserver;

    public SearchMeals() {
        this.searchMealItems = new HashMap<>();
    }

    public SearchMeals(List<SearchMealItem> items) {
        this.searchMealItems = new HashMap<>();
        addItems(items);
    }

    public Map<String, SearchMealItem> getSearchMealItems() {
        return searchMealItems;
    }

    public void setSearchMealItems(Map<String, SearchMealItem> searchMealItems) {
        this.searchMealItems = searchMealItems;
    }

    public void addItem(SearchMealItem item) {
        this.searchMealItems.put(item.getId(), item);
        updateSearchScreen();
    }

    public void addItems(List<SearchMealItem> items) {
        for (SearchMealItem item: items) {
            addItem(item);
        }
        updateSearchScreen();
    }

    public void removeItem(String searchMealItemId) {
        this.searchMealItems.remove(searchMealItemId);
        updateSearchScreen();
    }

    public void subscribeToDataChanges(ListDataObserver dataObserver) {
        this.dataObserver = dataObserver;
    }

    private void updateSearchScreen() {
        // if we have a subscribed data observer
        if (this.dataObserver != null) {
            // notify data observer of changes
            this.dataObserver.notifyDataChanged();
        }
    }
}
