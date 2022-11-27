package com.example.mealer_project.ui.screens.search;

import android.util.Log;

import com.example.mealer_project.utils.TrieSearch.TriesSearch;
import com.example.mealer_project.utils.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchMeals {

    // map to store SearchMealItem with their id's as the key value
    Map<String, SearchMealItem> searchMealItems;
    // instance of TrieSearch - the utility we use for search
    TriesSearch triesSearch;
    // store a reference to the SearchMealScreen so it could be notified of the updates to the list
    SearchScreen searchScreen;

    public SearchMeals() {
        this.searchMealItems = new HashMap<>();
        this.triesSearch = new TriesSearch();
    }

    public Map<String, SearchMealItem> getSearchMealItems() {
        return searchMealItems;
    }

    public void addItems(List<SearchMealItem> items) {
        // TODO test
        Log.e("searchMeals", "adding new items: " + items.size());
        for (SearchMealItem item: items) {
            // store the item in our map
            this.searchMealItems.put(item.getId(), item);
            // add meal's keywords to the TriesSearch dataset with an associated SearchMealItem id
            // if there is a match in these keywords, we would get the corresponding SearchMealItem id
            this.triesSearch.addData(item.getId(), item.getMeal().getKeywords());
        }
        // if we have a subscribed search screen observing data changes
        if (this.searchScreen != null) {
            // notify search screen of changes
            this.searchScreen.newSearchItemsAdded(items);
        }
    }

    public List<SearchMealItem> searchMealItems(String query) {
        // use TriesSearch to perform a pattern match and
        // get a list containing ids of SearchMealItems which have a match
        List<String> triesSearchResult = this.triesSearch.pMatch(query);
        // store result
        List<SearchMealItem> sMItems = new ArrayList<>();
        // for each id in search result
        for (String sMItemId: triesSearchResult) {
            // get the corresponding SearchMealItem from our local map and add to result list
            sMItems.add(this.searchMealItems.get(sMItemId));
        }
        // return result list, will be empty if no match
        return sMItems;
    }

    public void subscribeToDataChanges(SearchScreen dataObserver) {
        this.searchScreen = dataObserver;
    }
}
