package com.example.mealer_project.ui.screens.search;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchMeals {

    Map<String, SearchMealItem> searchMealItems;
    // store a reference to the SearchMealScreen so it could be notified of the updates to the list
    SearchScreen searchScreen;

    public SearchMeals() {
        this.searchMealItems = new HashMap<>();
    }

    public Map<String, SearchMealItem> getSearchMealItems() {
        return searchMealItems;
    }

    public void addItem(SearchMealItem item) {
        this.searchMealItems.put(item.getId(), item);
        // if we have a subscribed search screen observing data changes
        if (this.searchScreen != null) {
            // notify search screen of changes
            this.searchScreen.newSearchItemAdded(item);
        }
    }

    public void addItems(List<SearchMealItem> items) {
        // TODO test
        Log.e("searchMeals", "adding new items: " + items.size());
        for (SearchMealItem item: items) {
            this.searchMealItems.put(item.getId(), item);
        }
        // if we have a subscribed search screen observing data changes
        if (this.searchScreen != null) {
            // notify search screen of changes
            this.searchScreen.newSearchItemsAdded(items);
        }
    }

    public void subscribeToDataChanges(SearchScreen dataObserver) {
        this.searchScreen = dataObserver;
    }
}
