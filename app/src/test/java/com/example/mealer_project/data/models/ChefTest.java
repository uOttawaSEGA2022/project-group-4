package com.example.mealer_project.data.models;

import static org.junit.Assert.*;
import org.junit.Test;

public class ChefTest {

    // description, chefRating, email, numberofMealsSold, role

    //Testing the chef's description from primary class chef
    @Test
    public void getChefDiscription() {
        Chef chef = new Chef("Gordon", "Ramsey", "gordon123@gmail.com", "Password@123", Address.getSampleAddress(), UserRoles.CHEF, "I am a good chef", "myVoidChqeue", 233, 5);
        String expected = "I am a good chef";
        String actual = chef.getDescription();
        assertEquals("Chef description does not match", expected, actual);
    }

    //Testing the chef's rating from primary class chef
    @Test
    public void getChefRating() {
        Chef chef = new Chef("Gordon", "Ramsey", "gordon123@gmail.com", "Password@123", Address.getSampleAddress(), UserRoles.CHEF, "I am a good chef", "myVoidChqeue", 233, 5);
        int expected = 5;
        int actual = chef.getChefRating();
        assertEquals("Chef rating does not match", expected, actual);
    }

    //Testing the chef's email address from primary class chef
    @Test
    public void getChefEmail() {
        Chef chef = new Chef("Gordon", "Ramsey", "gordon123@gmail.com", "Password@123", Address.getSampleAddress(), UserRoles.CHEF, "I am a good chef", "myVoidChqeue", 233, 5);
        String expected = "gordon123@gmail.com";
        String actual = chef.getEmail();
        assertEquals("Chef email does not match", expected, actual);
    }

    //Testing the chef's number of meals sold from primary class chef
    @Test
    public void getNumberOfMealsSold() {
        Chef chef = new Chef("Gordon", "Ramsey", "gordon123@gmail.com", "Password@123", Address.getSampleAddress(), UserRoles.CHEF, "I am a good chef", "myVoidChqeue", 233, 5);
        int expected = 233;
        int actual = chef.getNumOfMealsSold();
        assertEquals("Chef's total meals sold does not match", expected, actual);
    }

    //Testing the chef's role from primary class chef
    @Test
    public void getRole() {
        Chef chef = new Chef("Gordon", "Ramsey", "gordon123@gmail.com", "Password@123", Address.getSampleAddress(), UserRoles.CHEF, "I am a good chef", "myVoidChqeue", 233, 5);
        UserRoles expected = UserRoles.CHEF;
        UserRoles actual = chef.getRole();
        assertEquals("Last name does not match", expected, actual);
    }
}