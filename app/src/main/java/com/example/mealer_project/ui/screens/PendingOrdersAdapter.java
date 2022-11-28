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
import com.example.mealer_project.app.AppInstance;
import com.example.mealer_project.data.handlers.OrderHandler;
import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.Client;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.data.models.orders.MealInfo;
import com.example.mealer_project.utils.SendMailTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class PendingOrdersAdapter extends ArrayAdapter<Order> {

    /**
     * Constructor
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public PendingOrdersAdapter(@NonNull Context context, int resource, @NonNull List<Order> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Variable Declaration: getting order data item for given position
        Order order = getItem(position);
        String mealNames = "";
        String quantities = "";
        String emailContents = "";

        // Process: checking if existing view is being reused
        if (convertView == null) { //must inflate view

            if (App.getUser() instanceof Client) { //is CLIENT
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_completed_orders_client_list_item, parent, false);
            } else if (App.getUser() instanceof Chef) { //is CHEF
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_pending_orders_list_item, parent, false);
            }
        }

        // Process: traversing entire meals map
        for (MealInfo mI : order.getMeals().values()) {

            mealNames += mI.getName() + "\n";
            quantities += mI.getQuantity() + "\n";
            emailContents += mI.getName() + "\t\t" + mI.getQuantity() + "\n";

        }

        // Variable Declaration
        final String EMAIL_CONTENTS = emailContents; //constant meal names & quantities

        // Process: checking if chef or client is logged in
        if (App.getUser() instanceof Client) { //is CLIENT
            ((TextView) convertView.findViewById(R.id.userNameText)).setText("Chef: " + order.getChefInfo().getChefName());
        }
        else if (App.getUser() instanceof Chef) { //is CHEF
            ((TextView) convertView.findViewById(R.id.userNameText)).setText("Client: " + order.getClientInfo().getClientName());

            // Process: setting onClicks for accept/reject buttons
            ((Button) convertView.findViewById(R.id.rejectButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Process: updating order to not pending and rejected
                    order.setIsPending(false); //no longer pending
                    order.setIsRejected(true); //rejected

                    App.ORDER_HANDLER.dispatch(OrderHandler.dbOperations.UPDATE_ORDER, order, App.getAppInstance().getPendingOrdersScreen()); //updating in Firebase

                    // Process: sending email to client that order has been rejected
                    new SendMailTask().execute("mealerprojectgroup4@gmail.com", "zzzbziucedxljweu",
                            order.getClientInfo().getClientEmail(),
                            "REJECTED MEALER Order: " + order.getOrderID().substring(0, 6),
                            "Hello " + order.getClientInfo().getClientName() + ",\n" +
                            "We regret to inform you that the following order has been rejected by Chef " + order.getChefInfo().getChefName() +
                            ".\n\n" + EMAIL_CONTENTS + "\n\n" + "Thank you for understanding.\n\nMEALER Team");
                }
            });

            ((Button) convertView.findViewById(R.id.acceptButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Process: updating order to not pending and rejected
                    order.setIsPending(false); //no longer pending
                    order.setIsRejected(false); //not rejected

                    App.ORDER_HANDLER.dispatch(OrderHandler.dbOperations.UPDATE_ORDER, order, App.getAppInstance().getPendingOrdersScreen()); //updating in Firebase

                    // Process: sending email to client that order has been accepted
                    new SendMailTask().execute("mealerprojectgroup4@gmail.com", "zzzbziucedxljweu",
                            order.getClientInfo().getClientEmail(),
                            "ACCEPTED MEALER Order: " + order.getOrderID().substring(0, 6),
                            "Hello " + order.getClientInfo().getClientName() + ",\n" +
                                    "The following order has been accepted by Chef " + order.getChefInfo().getChefName() + ".\n" +
                                    "You will receive another email notification when your order is ready for pick-up at " +
                                    order.getChefInfo().getChefAddress().getStreetAddress() + ", " +
                                    order.getChefInfo().getChefAddress().getCity() +
                                    order.getChefInfo().getChefAddress().getPostalCode() + ".\n\n" +
                                    EMAIL_CONTENTS + "\n\n" + "Thank you for placing your order through the MEALER app!\n" +
                                    "If you have any questions about your order, please email us directly at " +
                                    "mealerprojectgroup4@gmail.com with your order number provided in the subject line.\n\n" +
                                    "MEALER Team");
                }
            });
        }
        // Process: setting the order info to appear on the screen
        ((TextView) convertView.findViewById(R.id.mealNameText2)).setText("\n" + mealNames);
        ((TextView) convertView.findViewById(R.id.quantityText2)).setText("(#)\n" + quantities);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd\nhh:mm:ss");
        ((TextView) convertView.findViewById(R.id.dateText2)).setText("Date:\n" + dateFormat.format(order.getOrderDate()));

        return convertView;
    }
}
