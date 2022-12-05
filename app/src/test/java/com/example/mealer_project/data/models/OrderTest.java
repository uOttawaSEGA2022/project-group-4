package com.example.mealer_project.data.models;

import static org.junit.Assert.*;
import org.junit.Test;

public class OrderTest {

    /**
     * Test to confirm an Order's default pending state is true after being created
     */
    @Test
    public void testDefaultIsPending() {
        // create an empty order
        Order order = new Order();
        boolean expected = true; // expected to be true by default
        boolean actual = order.getIsPending();
        // confirm if order is indeed pending after being created
        assertEquals(expected, actual);
    }

    /**
     * Test to confirm an Order's default completed state is false after being created
     */
    @Test
    public void testDefaultIsCompleted() {
        // create an empty order
        Order order = new Order();
        boolean expected = false; // expected to be false
        boolean actual = order.getIsCompleted();
        // confirm if order is indeed uncompleted
        assertEquals(expected, actual);
    }

    /**
     * Test rejecting an Order
     */
    @Test
    public void testOrderRejection() {
        // create an empty order
        Order order = new Order();
        // reject the order
        order.setIsRejected(true);
        // get the actual value
        boolean actual = order.getIsRejected();
        // expected value of isRejected
        boolean expected = true;
        // confirm if order is indeed rejected
        assertEquals(expected, actual);
    }

    /**
     * Test setting rating
     */
    @Test
    public void testOrderRating() {
        // create an empty order
        Order order = new Order();
        // set a rating
        order.setRating(4);
        // get the actual value
        double actual = order.getRating();
        // expected value of rating
        double expected = 4.0;
        // confirm if order is indeed rejected
        assertEquals(expected, actual, 1.0);
    }
}
