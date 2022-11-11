package com.example.mealer_project.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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


    static public Date getDateFromString(String dateValue) throws ParseException {
        return DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).parse(dateValue);
    }
}
