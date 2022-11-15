package com.example.mealer_project.ui.screens.meals;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mealer_project.R;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.ui.screens.MealInfoScreen;
import com.example.mealer_project.ui.screens.SignupScreen;

import java.util.List;

public class MealsAdapter extends ArrayAdapter<Meal> {

    public static final String MEALS_DATA_ARG_KEY = "MEAL_DATA_ARG_KEY";

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public MealsAdapter(@NonNull Context context, int resource, @NonNull List<Meal> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Meal meal = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            // using activity_meals_list_item view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_meals_list_item, parent, false);
        }
        // Populate the meal data
        ((TextView) convertView.findViewById(R.id.mlMealId)).setText(meal.getMealID());
        ((TextView) convertView.findViewById(R.id.mlMealName)).setText(meal.getName());
        ((TextView) convertView.findViewById(R.id.mlMealDescription)).setText(meal.getDescription());
        ((TextView) convertView.findViewById(R.id.mlOffered)).setText(meal.isOffered() ? "Yes" : "No");
        ((TextView) convertView.findViewById(R.id.mlCuisineType)).setText(meal.getCuisineType());
        // attach on click listener to the meal item
        LinearLayout mealItemContainer = convertView.findViewById(R.id.mlItemContainer);
        // Cache row position inside the button using `setTag`
        mealItemContainer.setTag(position);
        // attach the click event handler
        mealItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                // Use the cached row position number inside LinearLayout's tag
                Meal mealData = getItem(position);

                Bundle extras = new Bundle();
                extras.putSerializable(MealInfoScreen.MEAL_DATA_ARG_KEY, mealData);
                Intent mealInfoIntent = new Intent(getContext(), MealInfoScreen.class);
                mealInfoIntent.putExtras(extras);

                // show meal info screen, passing it the meal's data
                startActivity(getContext(), mealInfoIntent, null);
            }
        });
        return convertView;
    }
}
