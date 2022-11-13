package com.example.mealer_project.ui.screens;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mealer_project.R;
import com.example.mealer_project.data.models.meals.Meal;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MealItemFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MEAL_DATA = "MEAL_DATA";

    // TODO: Rename and change types of parameters
    private String mealData;

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
            mealData = getArguments().getString(MEAL_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_item, container, false);
    }

    private void populateMealItemData() {
//        TextView mealName = (TextView)
    }
}