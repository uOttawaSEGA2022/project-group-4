package com.example.mealer_project.ui.core;

/**
 * Interface for UI Screens, Activities or View components which display state and/or observe state changes
 */
public interface StatefulView {
    /**
     * Centralised method to update UI on state changes as well as handle
     * side effects (one state change, causing UI changes at multiple spots)
     */
    void updateUI();
    // display next screen based on state changes
    void showNextScreen();

    /**
     * Method to handle success of a DB operation
     */
    void dbOperationSuccessHandler(Object dbOperation, Object payload);

    /**
     * Method to handle failure of a DB operation
     */
    void dbOperationFailureHandler(Object dbOperation, Object payload);
}
