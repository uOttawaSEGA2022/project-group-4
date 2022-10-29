package com.example.mealer_project.utils;

import java.util.HashMap;
import java.util.Map;

public class Utilities {
    static public String strValOf(Object obj) {
        return String.valueOf(obj);
    }

    static public Map<String, String> convertMapValuesToString(Map<String, Object> objectMap) {
        Map<String, String> stringMap = new HashMap<>(objectMap.size());
        for (String key : objectMap.keySet()) {
            stringMap.put(key, String.valueOf(objectMap.get(key)));
        }
        return stringMap;
    }
}
