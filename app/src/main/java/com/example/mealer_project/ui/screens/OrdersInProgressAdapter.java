package com.example.mealer_project.ui.screens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Orders;
import com.example.mealer_project.data.models.orders.OrderItem;

import java.util.List;


public class OrdersInProgressAdapter extends ArrayAdapter<OrderItem> {


    /**
     * Constructor
     *
     * @param context
     * @param resource
     * @param objects
     */
    public OrdersInProgressAdapter(@NonNull Context context, int resource, @NonNull List<OrderItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // get chef's orders in progress
        Orders order = App.getChef().ORDERS;


        // check if an existing view is being reused, otherwise, inflate
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.orders_in_progress, parent, false);
        }

        // populate data
        ((TextView) convertView.findViewById(R.id.clientText)).setText("");

        return convertView;
    }
}
