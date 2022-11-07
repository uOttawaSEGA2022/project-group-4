package com.example.mealer_project.data.models;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserTest {

    // firstName, lastName, email, password, address, role

    @Test
    public void getFirstName() {
        User user = new User("Henry", "Cavil", "hc@gm.com", "Ottawa@123", Address.getSampleAddress(), UserRoles.CLIENT);
        String expected = "Henry";
        String actual = user.getFirstName();
        assertEquals("First name does not match", expected, actual);
    }

    @Test
    public void getLastName() {
        User user = new User("Henry", "Cavil", "hc@gm.com", "Ottawa@123", Address.getSampleAddress(), UserRoles.CLIENT);
        String expected = "Cavil";
        String actual = user.getLastName();
        assertEquals("Last name does not match", expected, actual);
    }

    @Test
    public void getEmail() {
        User user = new User("Henry", "Cavil", "hc@gm.com", "Ottawa@123", Address.getSampleAddress(), UserRoles.CLIENT);
        String expected = "hc@gm.com";
        String actual = user.getEmail();
        assertEquals("Last name does not match", expected, actual);
    }

    @Test
    public void getRole() {
        User user = new User("Henry", "Cavil", "hc@gm.com", "Ottawa@123", Address.getSampleAddress(), UserRoles.CLIENT);
        UserRoles expected = UserRoles.CLIENT;
        UserRoles actual = user.getRole();
        assertEquals("Last name does not match", expected, actual);
    }
}