package com.example.mealer_project.ui.screens.search;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mealer_project.R;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.data.models.orders.ChefInfo;
import com.example.mealer_project.ui.screens.OrderScreen;

import java.util.List;

public class SearchMealItemsAdapter extends ArrayAdapter<SearchMealItem> {

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public SearchMealItemsAdapter(@NonNull Context context, int resource, @NonNull List<SearchMealItem> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        SearchMealItem sMItem = getItem(position);
        // retrieve the meal
        Meal meal = sMItem.getMeal();
        // retrieve ChefInfo
        ChefInfo chefInfo = sMItem.getChef();
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            // using activity_meals_list_item view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_search_meal_item, parent, false);
        }
        // Populate the meal data
        ((TextView) convertView.findViewById(R.id.smMealId)).setText(meal.getMealID());
        ((TextView) convertView.findViewById(R.id.smMealName)).setText(meal.getName());
        ((TextView) convertView.findViewById(R.id.smMealDescription)).setText(meal.getDescription());
        ((TextView) convertView.findViewById(R.id.smChef)).setText(chefInfo.getChefName());
        // TODO: implement chef rating
        // ((TextView) convertView.findViewById(R.id.smChefRating)).setText();
        // attach on click listener to the meal item
        LinearLayout mealItemContainer = convertView.findViewById(R.id.smItemContainer);
        // Cache row position inside the button using `setTag`
        mealItemContainer.setTag(position);
        // attach the click event handler
        mealItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                // Use the cached row position number inside LinearLayout's tag
                SearchMealItem sMItemData = getItem(position);

                Intent orderScreenIntent = new Intent(getContext(), OrderScreen.class);
                orderScreenIntent.putExtra(OrderScreen.SEARCH_MEAL_ITEM_ARG_KEY, sMItemData);

                // show order screen, passing it the search meal item data which contains meal and chef info
                startActivity(getContext(), orderScreenIntent, null);
            }
        });
        return convertView;
    }
}
