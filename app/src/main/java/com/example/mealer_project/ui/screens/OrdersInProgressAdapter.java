package com.example.mealer_project.ui.screens;

import android.content.Context;
import android.util.Log;
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
import com.example.mealer_project.data.handlers.OrderHandler;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.data.models.Orders;
import com.example.mealer_project.data.models.orders.MealInfo;
import com.example.mealer_project.ui.core.StatefulView;

import java.text.SimpleDateFormat;
import java.util.List;


public class OrdersInProgressAdapter extends ArrayAdapter<Order> implements StatefulView {


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
        Orders orders = App.getChef().ORDERS;
        Order order = getItem(position);

        //
        String mealNames = "";
        String quantities = "";


        // check if an existing view is being reused, otherwise, inflate
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.orders_in_progress, parent, false);
        }

        // Process: traversing entire meals map
        for (Order item : orders.getOrdersInProgress()) { // is a list of object Order

            for (MealInfo mI : item.getMeals().values()) { // loop through the list to get each order's name and quantity
                mealNames = mI.getName();
                quantities = String.valueOf(mI.getQuantity());
            }

        }

        // populate data
        ((TextView) convertView.findViewById(R.id.clientText)).setText("Client: " + order.getClientInfo().getClientName());
        ((TextView) convertView.findViewById(R.id.mealNameText)).setText("Meal: " + mealNames);
        ((TextView) convertView.findViewById(R.id.quantityOfMealInProgress)).setText("Q: " + quantities);

        // format the date string
        String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(order.getOrderDate());
        ((TextView) convertView.findViewById(R.id.dateForOrder)).setText(stringDate);


        // on click on completed button
        ((Button) convertView.findViewById(R.id.doneButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                order.setIsCompleted(true);
                App.ORDER_HANDLER.dispatch(OrderHandler.dbOperations.UPDATE_ORDER, order, OrdersInProgressAdapter.this);
            }
        });

        return convertView;
    }

    @Override
    public void updateUI() {

    }

    @Override
    public void showNextScreen() {

    }

    @Override
    public void dbOperationSuccessHandler(Object dbOperation, Object payload) {

    }

    @Override
    public void dbOperationFailureHandler(Object dbOperation, Object payload) {

    }
}
