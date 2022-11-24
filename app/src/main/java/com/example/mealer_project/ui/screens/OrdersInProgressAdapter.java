package com.example.mealer_project.ui.screens;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.data.models.orders.MealInfo;
import com.example.mealer_project.data.models.orders.OrderItem;

import java.util.List;


public class OrdersInProgressAdapter extends ArrayAdapter<Order> {


    /**
     * Constructor
     *
     * @param context
     * @param resource
     * @param objects
     */
    public OrdersInProgressAdapter(@NonNull Context context, int resource, @NonNull List<Order> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // get chef's orders in progress
        Order order = getItem(position);

        //
        String mealNames = "";
        String quantities = "";


        // check if an existing view is being reused, otherwise, inflate
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.orders_in_progress, parent, false);
        }

        // Process: traversing entire meals map
        for (MealInfo mI : order.getMeals().values()) {

            mealNames += mI.getName() + "\n";
            quantities += mI.getQuantity() + "\n";

        }

        // populate data
        ((TextView) convertView.findViewById(R.id.clientText)).setText(order.getClientInfo().getClientName());
        ((TextView) convertView.findViewById(R.id.dateForOrder)).setText(order.getOrderDate().toString());
        ((TextView) convertView.findViewById(R.id.mealNameText)).setText(mealNames);
        ((TextView) convertView.findViewById(R.id.quantityOfMealInProgress)).setText("Q: " + quantities);
        ((Button) convertView.findViewById(R.id.doneButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                order.setIsCompleted(true);
                // also remove the completed order from this screen!
            }
        });

        return convertView;
    }
}
