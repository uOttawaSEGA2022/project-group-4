package com.example.mealer_project.ui.core;

/**
 * Observer of state changes of another View or component
 */
public interface StateObserver {

    /**
     * Subscribe to a view or component of which state changes needs to be observed
     * @param statePublisher view or component publishing state changes
     */
    void subscribeStatePublisher(StatePublisher statePublisher);

    /**
     * handle state changes from the publisher
     */
    void observeStateChanges(UIState newState);
}
