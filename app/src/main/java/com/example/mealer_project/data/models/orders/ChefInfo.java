package com.example.mealer_project.data.models.orders;

import com.example.mealer_project.data.models.Address;
import com.example.mealer_project.data.models.Chef;

import java.io.Serializable;

public class ChefInfo implements Serializable {
    String chefId;
    String chefName;
    double chefRating;
    Address chefAddress;

    public ChefInfo(String chefId, String chefName, double chefRating, Address chefAddress) {
        this.setChefId(chefId);
        this.setChefName(chefName);
        this.setChefRating(chefRating);
        this.setChefAddress(chefAddress);
    }

    public ChefInfo(Chef chef) {
        this.setChefId(chef.getUserId());
        this.setChefName(chef.getFirstName() + " " + chef.getLastName());
        this.setChefRating(chef.getChefRating());
        this.setChefAddress(chef.getAddress());
    }

    public String getChefId() {
        return chefId;
    }

    public void setChefId(String chefId) {
        this.chefId = chefId;
    }

    public String getChefName() {
        return chefName;
    }

    public void setChefName(String chefName) {
        this.chefName = chefName;
    }

    public double getChefRating() {
        return chefRating;
    }

    public void setChefRating(double chefRating) {
        this.chefRating = chefRating;
    }

    public Address getChefAddress() {
        return chefAddress;
    }

    public void setChefAddress(Address chefAddress) {
        this.chefAddress = chefAddress;
    }
}
