package com.example.mealer_project.ui.screens;

import static com.example.mealer_project.data.handlers.OrderHandler.dbOperations.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.handlers.OrderHandler;
import com.example.mealer_project.data.models.Address;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.data.models.orders.ChefInfo;
import com.example.mealer_project.data.models.orders.ClientInfo;
import com.example.mealer_project.data.models.orders.SearchMealItem;
import com.example.mealer_project.ui.core.StatefulView;
import com.example.mealer_project.ui.core.UIScreen;

import java.util.ArrayList;
import java.util.Arrays;

public class TestOrdersActivity extends UIScreen implements StatefulView {
    SearchMealItem sMItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_orders);

        getSampleSearchMealItem();

        attachClickListener();

//        displayCartCount();
    }

    private void attachClickListener() {
        Button toOrderScreenBtn = (Button) findViewById(R.id.toOrderScreen);
        Button toSubmitOrderBtn = (Button) findViewById(R.id.toSubmitOrder);
//        Button updateCartCount = (Button) findViewById(R.id.updateCartCount);
        // display order screen
        toOrderScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderScreenIntent = new Intent(getApplicationContext(), OrderScreen.class);
                orderScreenIntent.putExtra(OrderScreen.SEARCH_MEAL_ITEM_ARG_KEY, sMItem);
                startActivity(orderScreenIntent);
            }
        });
        // submit new order
        toSubmitOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitNewOrder();
            }
        });

//        updateCartCount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                displayCartCount();
//            }
//        });
    }

    // not working yet
//    private void displayCartCount() {
//        TextView cartCountTxt = (TextView) findViewById(R.id.cartCountTxt);
//        cartCountTxt.setText(String.valueOf(App.getClient().getCart().size()));
//    }

    private void getSampleSearchMealItem() {
        // sample chef
        ChefInfo chef = new ChefInfo(
                "7lkcibd9a9cRhZNdsJo6iPfzEXF2",
                "Harry Styles",
                4,
                new Address("128 Baker St", "Barrie", "G6X3X3", "Canada"));
        // sample client
        ClientInfo client = new ClientInfo(
                "09byC6VCX3Qi3AvKG6NDEDypewO2",
                "Kapil Sharma",
                "kapil@sharma.com"
        );
        // sample meal
        Meal meal = new Meal(
                "Bubble Tea",
                "P8jYzHebirkiZg3xrYbi",
                "7lkcibd9a9cRhZNdsJo6iPfzEXF2",
                "Asian",
                "Beverages",
                "Tapioca, milk, creamer, brewed tea, sugar, flavourings",
                new ArrayList<String>(Arrays.asList("Milk")),
                "Bubble tea is a tea-based drink that originated in Taiwan in the early 1980s. Can be served cold or hot.",
                true,
                12
        );
        // initialize the SearchMealItem
        sMItem = new SearchMealItem(meal, chef);
    }

    private void submitNewOrder() {
        App.ORDER_HANDLER.dispatch(ADD_ORDER, null, this);
    }

    /**
     * Centralised method to update UI on state changes as well as handle
     * side effects (one state change, causing UI changes at multiple spots)
     */
    @Override
    public void updateUI() {

    }

    @Override
    public void showNextScreen() {

    }

    /**
     * Method to handle success of a DB operation
     *
     * @param dbOperation
     * @param payload
     */
    @Override
    public void dbOperationSuccessHandler(Object dbOperation, Object payload) {
        Log.e("testOrders", "Success: Op: " + dbOperation + ", payload: " + payload);
        if (dbOperation == ADD_ORDER) {
            displaySuccessToast((String) payload);
        }
    }

    /**
     * Method to handle failure of a DB operation
     *
     * @param dbOperation
     * @param payload
     */
    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {
        Log.e("testOrders", "Failure: Op: " + dbOperation + ", payload: " + payload);
        displayErrorToast((String) payload);
    }
}