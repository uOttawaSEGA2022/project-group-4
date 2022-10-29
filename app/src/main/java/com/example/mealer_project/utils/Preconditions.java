package com.example.mealer_project.utils;

public abstract class Preconditions {
    /**
     * Checks if provided object is not null
     * @param obj object to check
     * @return true if object is not null, else false
     */
    static public boolean isNotNull(Object obj) {
        return (obj != null);
    }

    /**
     * Checks if provided String object is not an empty string or null
     * @param strObj String object to check
     * @return true if string is not null and empty, else false
     */
    static public boolean isNotEmptyString(String strObj) {
        return isNotNull(strObj) && (strObj.length() != 0);
    }
}
