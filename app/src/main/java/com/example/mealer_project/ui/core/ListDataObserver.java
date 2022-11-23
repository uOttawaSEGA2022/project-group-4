package com.example.mealer_project.ui.core;

/**
 * Interface to be implemented by a View displaying a list by consuming data stored locally so
 * it could be notified of any changes
 */
public interface ListDataObserver {
    /**
     * Notify of data changes in the list consumed by the view
     */
    void notifyDataChanged();
}
