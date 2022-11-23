package com.example.mealer_project.ui.core.search;

import com.example.mealer_project.data.models.Chef;
import com.example.mealer_project.data.models.meals.Meal;
import com.example.mealer_project.data.models.orders.ChefInfo;

import java.io.Serializable;
import java.util.UUID;

public class SearchMealItem implements Serializable {
    private UUID id;
    private Meal meal;
    private ChefInfo chef;

    public SearchMealItem(Meal meal, ChefInfo chef) {
        this.setId();
        this.setMeal(meal);
        this.setChef(chef);
    }

    public SearchMealItem(Meal meal, Chef chef) {
        this.setId();
        this.setMeal(meal);
        this.setChef(chef);
    }

    public void setId() {
        this.id = UUID.randomUUID();
    }

    public String getId() {
        return this.id.toString();
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
