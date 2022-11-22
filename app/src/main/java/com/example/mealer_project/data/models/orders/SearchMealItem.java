package com.example.mealer_project.data.models.orders;

import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.meals.Meal;

public class SearchMealItem {
    private Meal meal;
    private ChefInfo chef;

    public SearchMealItem(Meal meal, ChefInfo chef) {
        this.setMeal(meal);
        this.setChef(chef);
    }

    public SearchMealItem(Meal meal, Chef chef) {
        this.setMeal(meal);
        this.setChef(chef);
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public ChefInfo getChef() {
        return chef;
    }

    public void setChef(Chef chef) {
        this.chef = new ChefInfo(chef);
    }

    public void setChef(ChefInfo chef) {
        this.chef = chef;
    }
}
