package com.example.mealer_project.data.models;

import com.example.mealer_project.data.entity_models.AddressEntityModel;

import java.io.Serializable;

public class Address implements Serializable {
    private String streetAddress;
    private String city;
    private String postalCode;
    private String country;

    public Address(String streetAddress, String city, String postalCode, String country) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public Address(AddressEntityModel addressEntityModel) {
        this.streetAddress = addressEntityModel.getStreetAddress();
        this.city = addressEntityModel.getCity();
        this.postalCode = addressEntityModel.getPostalCode();
        this.country = addressEntityModel.getCountry();
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static Address getSampleAddress() {
        return new Address("23 St", "Barrie", "L4M7B6", "Canada");
    }
}
