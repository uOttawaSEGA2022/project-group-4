package com.example.mealer_project.ui.screens.checkout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mealer_project.R;
import com.example.mealer_project.data.models.orders.OrderItem;

import java.util.List;

/**
 * This class will be used to change the text of each item in the cart based on its information
 * (meal name + price + quantity -> the item's quantity can still be changed in the checkout screen)
 */
public class CheckoutAdapter extends ArrayAdapter<OrderItem>{

    // key to get information
    public static final String CHECKOUT_TYPE_ARG_KEY = "CHECKOUT_TYPE_ARG_KEY";

    private Button minusButton;
    private Button plusButton;

    /**
     *
     * @param context
     * @param resource
     * @param objects
     */
    public CheckoutAdapter(@NonNull Context context, int resource, @NonNull List<OrderItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Get data for item in cart
        OrderItem item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            // using checkout_item view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.checkout_item, parent, false);

        }

        // Populate the data
        ((TextView) convertView.findViewById(R.id.item_meal_title)).setText(item.getSearchMealItem().getMeal().getName());
        ((TextView) convertView.findViewById(R.id.item_price)).setText((String.valueOf(item.getSearchMealItem().getMeal().getPrice())));
        ((TextView) convertView.findViewById(R.id.item_quantity)).setText(String.valueOf(item.getQuantity()));

        // quantity update buttons
        minusButton = (Button) convertView.findViewById(R.id.decrease_quantity);
        plusButton = (Button) convertView.findViewById(R.id.increase_quantity);

        // attach on click listener to minus button
        minusButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });


        // attach on click listener to addition button
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // send information to the CheckoutScreen
        Bundle extras = new Bundle();
        extras.putSerializable(CheckoutScreen.CHECKOUT_DATA_ARG_KEY, item);

        return convertView;
    }
}
