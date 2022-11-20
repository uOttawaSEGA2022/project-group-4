package com.example.mealer_project.data.models.orders;

import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.meals.Meal;

public class SearchMealItem {
    private Meal meal;
    private Chef chef;

    public SearchMealItem(Meal meal, Chef chef) {
        this.meal = meal;
        this.chef = chef;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public Chef getChef() {
        return chef;
    }

    public void setChef(Chef chef) {
        this.chef = chef;
    }
}
