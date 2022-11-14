package com.example.mealer_project.ui.screens;

import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.data.models.meals.Meal;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealItemFragment extends Fragment implements AdapterView.OnItemClickListener {

    // Meal data property name
    private static final String MEAL_DATA = "MEAL_DATA";

    // store meal data
    private Meal mealData;

    public MealItemFragment() {
        // Required empty public constructor
    }

    /**
     * Received a meal instance and returns a new MealItemFragment
     * @param meal Meal instance
     * @return A new instance of fragment MealItemFragment
     */
    public static MealItemFragment newInstance(Meal meal) {
        MealItemFragment fragment = new MealItemFragment();
        Bundle args = new Bundle();
        args.putSerializable(MEAL_DATA, meal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                mealData = getArguments().getSerializable(MEAL_DATA, Meal.class);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View view = inflater.inflate(R.layout.fragment_meal_item, container, false);

        populateMealItemData(view);

        return view;
    }

    private void populateMealItemData(View view) {
        ((TextView) view.findViewById(R.id.fMealId)).setText(mealData.getMealID());
        ((TextView) view.findViewById(R.id.fMealName)).setText(mealData.getName());
        ((TextView) view.findViewById(R.id.fMealDescription)).setText(mealData.getDescription());
        ((TextView) view.findViewById(R.id.fOffered)).setText(mealData.isOffered() ? "Yes" : "No");
        ((TextView) view.findViewById(R.id.fCuisineType)).setText(mealData.getCuisineType());
    }

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}