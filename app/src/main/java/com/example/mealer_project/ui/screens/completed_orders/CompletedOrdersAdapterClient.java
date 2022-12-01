package com.example.mealer_project.ui.screens.completed_orders;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mealer_project.R;
import com.example.mealer_project.app.App;
import com.example.mealer_project.data.models.Order;
import com.example.mealer_project.data.models.orders.MealInfo;
import com.example.mealer_project.ui.screens.MakeComplaint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class CompletedOrdersAdapterClient extends ArrayAdapter<Order> {


    /**
     * Constructor
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public CompletedOrdersAdapterClient(@NonNull Context context, int resource, @NonNull List<Order> objects) {
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

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_completed_orders_client_list_item, parent, false);

        }

        // Process: traversing entire meals map
        for (MealInfo mI : order.getMeals().values()) {

            mealNames += mI.getName() + "\n";
            quantities += mI.getQuantity() + "\n";

        }


        ((TextView) convertView.findViewById(R.id.userNameText)).setText("Chef: " + order.getChefInfo().getChefName());

        // Process: setting onClicks for complaint button
        Button complaintButton = (Button) convertView.findViewById(R.id.fileComplaintButton);

        // Process: Check if null
        if (complaintButton != null) {
            complaintButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("complaintCheck", "it works");

                    Bundle orderInfo = new Bundle();
                    orderInfo.putSerializable("background_pattern.png", order);
                    Intent goToComplaint = new Intent(getContext(), MakeComplaint.class);
                    //goToComplaint.putExtras(orderInfo);
                    startActivity(getContext(), goToComplaint, null);
                    //parent.getContext().startActivity(new Intent(parent.getContext(), ClientScreen.class));
                }
            });
        }

        RatingBar ratingBar = (RatingBar) convertView.findViewById(R.id.ratingBar);
        Button submitButton = (Button) convertView.findViewById(R.id.submitButton);

        // Process: Check if null
        if (submitButton != null) {
            if (ratingBar != null) {
                // perform click event on button
                submitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get values and then displayed in a toast
                        String rating = "Rating :: " + ratingBar.getRating();
                        App.ORDER_HANDLER.updateChefRating(order.getChefInfo().getChefId(), Double.valueOf(ratingBar.getRating()));
                        Toast.makeText(getContext(), rating, Toast.LENGTH_SHORT).show();
                        ratingBar.setIsIndicator(true);
                        submitButton.setVisibility(View.GONE);

                    }
                });
            }
        }



        // Process: setting the order info to appear on the screen
        ((TextView) convertView.findViewById(R.id.mealNameText2)).setText("\n" + mealNames);
        ((TextView) convertView.findViewById(R.id.quantityText2)).setText("(#)\n" + quantities);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd\nhh:mm:ss");
        ((TextView) convertView.findViewById(R.id.dateText2)).setText("Date:\n" + dateFormat.format(order.getOrderDate()));

        return convertView;
    }
}


