package com.example.mealer_project.data.entity_models;

import com.example.mealer_project.data.models.UserRoles;

public class UserEntityModel {

    String firstName;
    String lastName;
    String email;
    String password;
    AddressEntityModel address;
    String userId;
    // data member containing user's role
    UserRoles role;

    public UserEntityModel() {};

    public UserEntityModel(String firstName, String lastName, String email, String password, AddressEntityModel address, UserRoles role) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setPassword(password);
        this.setAddress(address);
        this.setRole(role);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AddressEntityModel getAddress() {
        return address;
    }

    public void setAddress(AddressEntityModel address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

}
