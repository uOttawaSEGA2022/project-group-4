package com.example.mealer_project.ui.core;

import java.util.ArrayList;

/**
 * Notifies other observing views of state changes
 */
public interface StatePublisher {

    /**
     * Notify subscribed views (dependent screens or components) of state changes
     */
    void notifyStateChanges(UIState newState);

    /**
     * subscribe a view or component to be able to observe state changes
     */
    void addStateObserver(StateObserver stateObserver);
}
