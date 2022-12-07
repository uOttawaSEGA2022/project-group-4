package com.example.mealer_project.ui.screens.pending_orders;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.data.models.orders.MealInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class PendingOrdersAdapterClient extends ArrayAdapter<Order> {

    /**
     * Constructor
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public PendingOrdersAdapterClient(@NonNull Context context, int resource, @NonNull List<Order> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Variable Declaration: getting order data item for given position
        Order order = getItem(position);
        String mealNames = "";
        String quantities = "";

        // Process: checking if existing view is being reused
        if (convertView == null) { //must inflate view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_completed_orders_list_item, parent, false);

        }

        // Process: traversing entire meals map
        for (MealInfo mI : order.getMeals().values()) {

            mealNames += mI.getName() + "\n";
            quantities += mI.getQuantity() + "\n";

        }

         ((TextView) convertView.findViewById(R.id.userNameText)).setText("Chef: " + order.getChefInfo().getChefName());


        // Process: setting the order info to appear on the screen
        ((TextView) convertView.findViewById(R.id.mealNameText2)).setText("\n" + mealNames);
        ((TextView) convertView.findViewById(R.id.quantityText2)).setText("(#)\n" + quantities);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd\nhh:mm:ss");
        ((TextView) convertView.findViewById(R.id.dateText2)).setText("Date:\n" + dateFormat.format(order.getOrderDate()));

        // if client logged in
        if (App.getClient() != null) {
            TextView orderStatus = convertView.findViewById(R.id.orderStatusField);
            // display order status for pending orders
            orderStatus.setVisibility(View.VISIBLE);
            if (!order.getIsPending() && !order.getIsCompleted() && !order.getIsRejected()) {
                orderStatus.setText(R.string.accepted_order_status_being_prepared);

            }
        }



        return convertView;
    }
}