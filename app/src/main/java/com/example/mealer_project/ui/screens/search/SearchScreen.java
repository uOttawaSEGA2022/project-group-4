package com.example.mealer_project.ui.screens.search;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.ui.core.UIScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchScreen extends UIScreen {

    // map to load search meal items
    Map<String, SearchMealItem> sMItemsData;
    // list used by SearchMealItemsAdapter to populate SearchMealItems on the ListView
    List<SearchMealItem> sMItems;

    // adapter to handle list view
    private SearchMealItemsAdapter sMItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_meal_screen);
        // load the search meal data
        loadSearchMealData();
        // populate the list view
        populateListView();
        // subscribe to SearchMeals for data updates
        subscribeToDataChanges();
    }


    public void newSearchItemAdded(SearchMealItem newItem) {
        // update our local data store
        loadSearchMealData();
        // add new item
        this.sMItems.add(newItem);
        // inform adapter of the change
        sMItemsAdapter.notifyDataSetChanged();
        // TODO: below line for test, remove later
        Log.e("searchMeals", "new search item added, sM: " + this.sMItems.size() + " sM D: " + this.sMItemsData.size());
    }

    public void newSearchItemsAdded(List<SearchMealItem> newItems) {
        // update our local data store
        loadSearchMealData();
        // add all new items
        this.sMItems.addAll(newItems);
        // inform adapter of the change
        sMItemsAdapter.notifyDataSetChanged();
        // TODO: below line for test, remove later
        Log.e("searchMeals", "new search items added, sM: " + this.sMItems.size() + " sM D: " + this.sMItemsData.size());
    }

    /**
     * Load search meal data from app
     */
    private void loadSearchMealData() {
        // ensure we have a valid client logged in (only client's search meals)
        if (App.getClient() != null) {
            // get search meal items from app's current instance
            this.sMItemsData = App.getClient().getSearchMeals().getSearchMealItems();
        }
        Log.e("searchMeals", "Loaded meals: " + this.sMItemsData.size());
    }

    /**
     * Use the adapter to update the items being displayed in list view
     */
    private void populateListView() {
        // initialize smItems as empty list
        this.sMItems = new ArrayList<>();
        // get the list view component
        ListView sMList = findViewById(R.id.smMealsList);
        // get the adapter
        this.sMItemsAdapter = new SearchMealItemsAdapter(this, R.layout.activity_search_meal_item, this.sMItems);
        // attach adapter to list view
        sMList.setAdapter(this.sMItemsAdapter);
        // add data to the adapter
        for (String sMItemId: this.sMItemsData.keySet()) {
            this.sMItems.add(this.sMItemsData.get(sMItemId));
        }
        Log.e("searchMeals", "Populated the list: " + this.sMItems.size());
    }

    /**
     * Subscribe to data changes for search meal items
     */
    private void subscribeToDataChanges() {
        // ensure we have a valid client logged in
        if (App.getClient() != null) {
            App.getClient().getSearchMeals().subscribeToDataChanges(this);
        }
    }
}