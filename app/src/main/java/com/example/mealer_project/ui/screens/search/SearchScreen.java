package com.example.mealer_project.ui.screens.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.ui.core.UIScreen;
import com.example.mealer_project.ui.screens.checkout.CheckoutScreen;
import com.example.mealer_project.utils.PostalCodes.PostalCodeComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SearchScreen extends UIScreen {

    // map to load search meal items
    Map<String, SearchMealItem> sMItemsData;
    // list used by SearchMealItemsAdapter to populate SearchMealItems on the ListView
    List<SearchMealItem> sMItems;

    ImageButton backButton;
    //ImageButton searchButton;
    ImageButton checkoutButton;

    ListView sMList;
    EditText searchBox;
    TextView noSearchResultMessage;

    // adapter to handle list view
    private SearchMealItemsAdapter sMItemsAdapter;

    // instantiate a Postal Code comparator to sort search results by
    // closeness to Client's postal code
    PostalCodeComparator postalCodeComparator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_meal_screen);

        backButton = findViewById(R.id.back_btn3);
        //searchButton = findViewById(R.id.searchBtn); //add when screen is done
        checkoutButton = findViewById(R.id.cartBtn);

        // get the list view component
        sMList = findViewById(R.id.smMealsList);
        searchBox = (EditText) findViewById(R.id.searchBox);
        noSearchResultMessage = (TextView) findViewById(R.id.noSearchResultMessage);

        attachOnClickListeners();

        // load the search meal data
        loadSearchMealData();
        // populate the list view
        populateListView();
        // subscribe to SearchMeals for data updates
        subscribeToDataChanges();

        try {
            postalCodeComparator = new PostalCodeComparator(App.getClient().getAddress().getPostalCode());
        } catch (Exception e) {
            Log.e("searchMeals", "Unable to create instance of postal code comparator: " + e.getMessage());
            displayErrorToast("Unable to sort results by closeness to client");
        }

        // CAUTION: uncomment below only when keywords needs to be re-generated for all meals in database
        // App.getPrimaryDatabase().MEALS.generateMealKeywords();
    }

    public void attachOnClickListeners(){
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
                if (App.getClient().getCart().size() == 0)
                    displayErrorToast("No items in cart!");
                else {
                    Intent intent = new Intent(getApplicationContext(), CheckoutScreen.class);
                    startActivity(intent);
                }
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // if nothing in search box
                if(s.length() != 0) {
                    // display search results based on the query entered by client
                    displaySearchResult(s.toString());
                } else {
                    // display all meals
                    populateListView();
                }
                Log.e("searchR", "Search box text changed: " + s);
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

    private void displaySearchResult(String query) {
        if (App.getClient() != null) {
            // get the list of SearchMealItems matching the query entered in search box
            List<SearchMealItem> searchResult = App.getClient().getSearchMeals().searchMealItems(query);

            // if there are no matching results, display a message indicating so and return
            if (searchResult.isEmpty()) {
                setNoSearchResultMessageVisibility(true);
                return;
            } else {
                // hide the no search result message and continue processing
                setNoSearchResultMessageVisibility(false);
            }

            // sort the search results by closeness to client (based on postal codes)
            if (postalCodeComparator != null) {
                Collections.sort(searchResult, (sR1, sR2) -> postalCodeComparator.comparePostalCodes(sR1.getChef().getChefAddress().getPostalCode(), sR2.getChef().getChefAddress().getPostalCode()));
                Log.e("searchMeals", "search results sorted");
            }
            // clear current items in sMItems
            this.sMItems = new ArrayList<>();
            // get the adapter
            this.sMItemsAdapter = new SearchMealItemsAdapter(this, R.layout.activity_search_meal_item, this.sMItems);
            // attach adapter to list view
            sMList.setAdapter(this.sMItemsAdapter);
            // load the result meals
            this.sMItems.addAll(searchResult);
        }
    }

    private void setNoSearchResultMessageVisibility(boolean visible) {
        if (visible) {
            // clear the list items, and display no result
            sMItemsAdapter.clear();
            noSearchResultMessage.setVisibility(View.VISIBLE);
        } else {
            noSearchResultMessage.setVisibility(View.GONE);
        }
    }
}
