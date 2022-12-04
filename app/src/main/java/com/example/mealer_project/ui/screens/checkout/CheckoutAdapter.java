package com.example.mealer_project.ui.screens.checkout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mealer_project.R;
import com.example.mealer_project.data.models.orders.OrderItem;

import java.text.DecimalFormat;
import java.util.List;

/**
 * This class will be used to change the text of each item in the cart based on its information
 * (meal name + price + quantity -> the item's quantity can still be changed in the checkout screen)
 */
public class CheckoutAdapter extends ArrayAdapter<OrderItem>{

    // format price to two decimal places
    private static final DecimalFormat df = new DecimalFormat("0.00");

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
        double price = item.getSearchMealItem().getMeal().getPrice();
        ((TextView) convertView.findViewById(R.id.item_price)).setText("$ " + df.format(price));
        TextView quantity = ((TextView) convertView.findViewById(R.id.item_quantity));
        quantity.setText(String.valueOf(item.getQuantity()));

        return convertView;
    }
}