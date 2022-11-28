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
import com.example.mealer_project.utils.SendMailTask;

import java.text.SimpleDateFormat;
import java.util.List;


public class OrdersInProgressAdapter extends ArrayAdapter<Order>{


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

        // get order data item for given position
        String mealNames = "";
        String quantities = "";
        String emailContents = "";

        // check if an existing view is being reused, otherwise, inflate
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.orders_in_progress, parent, false);
        }

        // Process: traversing entire meals map
        for (Order item : orders.getOrdersInProgress()) { // is a list of object Order

            for (MealInfo mI : order.getMeals().values()) {

                mealNames += mI.getName() + "\n";
                quantities += mI.getQuantity() + "\n";
                emailContents += mI.getName() + "\t\t" + mI.getQuantity() + "\n";

            }

        }

        // Variable Declaration
        final String EMAIL_CONTENTS = emailContents; //constant meal names & quantities

        // on click on completed button
        ((Button) convertView.findViewById(R.id.doneButton)).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                order.setIsCompleted(true);
                App.ORDER_HANDLER.dispatch(OrderHandler.dbOperations.UPDATE_ORDER, order,  App.getAppInstance().getOrdersInProgressScreen());
                // Process: sending email to client that order has been rejected
                new SendMailTask().execute("mealerprojectgroup4@gmail.com", "zzzbziucedxljweu",
                        order.getClientInfo().getClientEmail(),
                        "READY-FOR-PICK-UP MEALER Order: " + order.getOrderID().substring(0, 6),
                        "Hello " + order.getClientInfo().getClientName() + ",\n" +
                        "The following order is ready for pick-up at " +
                        order.getChefInfo().getChefAddress().getStreetAddress() + ", " +
                        order.getChefInfo().getChefAddress().getCity() +
                        order.getChefInfo().getChefAddress().getPostalCode() + ".\n\n" +
                        EMAIL_CONTENTS + "\n\n" +
                        "If you have any questions about pick-up, please contact us at mealerprojectgroup4@gmail.com" +
                        " with your order number provided in the subject line.\n\n" +
                        "MEALER Team");
            }
        });

        // populate data
        ((TextView) convertView.findViewById(R.id.clientText)).setText("Client: " + order.getClientInfo().getClientName());
        ((TextView) convertView.findViewById(R.id.mealNameText)).setText("Meal(s): " + "\n" + mealNames);
        ((TextView) convertView.findViewById(R.id.quantityOfMealInProgress)).setText("(#)\n" + quantities);

        // format the date string
        String stringDate = new SimpleDateFormat("yyyy-MM-dd").format(order.getOrderDate());
        ((TextView) convertView.findViewById(R.id.dateForOrder)).setText(stringDate);


        return convertView;
    }

}
