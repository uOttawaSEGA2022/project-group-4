package com.example.mealer_project.ui.screens.search;

import android.os.Bundle;
import android.widget.ListView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.ui.core.ListDataObserver;
import com.example.mealer_project.ui.core.UIScreen;
import com.example.mealer_project.ui.core.search.SearchMealItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchScreen extends UIScreen implements ListDataObserver {

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
    }

    /**
     * Notify of data changes in the list consumed by the view
     */
    @Override
    public void notifyDataChanged() {
        // re-load the data from local store
        loadSearchMealData();
        // re-populate the list view
        populateListView();
    }

    /**
     * Load search meal data from app
     */
    private void loadSearchMealData() {
        // get search meal items from app's current instance
        this.sMItemsData = App.SEARCH_MEALS_LIST.getSearchMealItems();
    }

    /**
     * Use the adapter to update the items being displayed in list view
     */
    private void populateListView() {
        // initialize (or re-initialize) smItems as empty list
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
    }
}
