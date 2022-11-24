package com.example.mealer_project.ui.screens.search;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.ui.core.UIScreen;
import com.example.mealer_project.ui.screens.ClientScreen;
import com.example.mealer_project.ui.screens.IntroScreen;
import com.example.mealer_project.ui.screens.checkout.CheckoutScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchScreen extends UIScreen {

    // map to load search meal items
    Map<String, SearchMealItem> sMItemsData;
    // list used by SearchMealItemsAdapter to populate SearchMealItems on the ListView
    List<SearchMealItem> sMItems;
    ImageButton backButton;
    ImageButton searchButton;
    ImageButton checkoutButton;

    // adapter to handle list view
    private SearchMealItemsAdapter sMItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_meal_screen);

        backButton = findViewById(R.id.back_btn3);
        searchButton = findViewById(R.id.searchBtn); //add when screen is done
        checkoutButton = findViewById(R.id.cartBtn);
        createOnClickListener();

        // load the search meal data
        loadSearchMealData();
        // populate the list view
        populateListView();
        // subscribe to SearchMeals for data updates
        subscribeToDataChanges();
    }

    public void createOnClickListener(){
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // close the activity
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CheckoutScreen.class);
                startActivity(intent);
            }
        });
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
