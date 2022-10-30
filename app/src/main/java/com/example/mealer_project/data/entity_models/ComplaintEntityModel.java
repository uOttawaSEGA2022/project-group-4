package com.example.mealer_project.data.entity_models;

/**
 * Entity Model to hold unvalidated client's complaint data
 */
public class ComplaintEntityModel {
    private String id;
    private String title;
    private String description;
    private String mealId;
    private String clientId;
    private String chefId;
    private String dateSubmitted;

    public ComplaintEntityModel(String id, String title, String description,String mealId, String clientId, String chefId, String dateSubmitted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.mealId = mealId;
        this.clientId = clientId;
        this.chefId = chefId;
        this.dateSubmitted = dateSubmitted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.clientId = mealId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getChefId() {
        return chefId;
    }

    public void setChefId(String chefId) {
        this.chefId = chefId;
    }

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(String dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }
}
