package com.example.mealer_project.data.entity_models;

public class CreditCardEntityModel {
    private String clientId;
    private String brand;
    private String name;
    private String number;
    private String cvc;
    private int expiryMonth;
    private int expiryYear;

    public CreditCardEntityModel() {};

    public CreditCardEntityModel(String brand, String name, String number, String cvc, int expiryMonth, int expiryYear) {
        this.setBrand(brand);
        this.setName(name);
        this.setNumber(number);
        this.setCvc(cvc);
        this.setExpiryMonth(expiryMonth);
        this.setExpiryYear(expiryYear);
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public int getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(int expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public int getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(int expiryYear) {
        this.expiryYear = expiryYear;
    }
}
