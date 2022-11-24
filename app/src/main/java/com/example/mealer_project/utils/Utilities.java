package com.example.mealer_project.utils;

import android.util.Log;

import com.example.mealer_project.utils.TrieSearch.StopWords;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    static public Date getTodaysDate() {
        return DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).getCalendar().getTime();
    }

    static public String getMapPropertyNames(Map mapObj) {
        // print in logcat all properties
        StringBuilder v = new StringBuilder();
        for (Object k: mapObj.keySet()) {
            v.append(String.valueOf(k) + ", ");
        }
        return v.toString();
    }

    /**
     * A utility method to extract a list of keywords from a string
     * Keywords include all words except common words which are irrelevant for a search like verbs (ex: is, are, doing)
     * @param value a string containing one or more words
     * @return a list of string in which each value represents one keyword, empty string if no keywords
     */
    static public List<String> getKeywords(String value) {
        // validate the value received
        if (Preconditions.isNotEmptyString(value)) {
            // list to store our keywords
            List<String> keywords = new ArrayList<>();
            // split on space
            String[] valCharacters = value.split("\\s+");
            // iterate through each word
            for (String word: valCharacters) {
                // if word is a valid keyword, add it to our list of keywords
                if (isKeyword(word)) {
                    keywords.add(word);
                }
            }
            // return the list of keywords
            return keywords;
        }
        // if in valid value, or can't find any keywords, we return empty list
        return new ArrayList<>();
    }

    static private boolean isKeyword(String word) {
        // return true is word is not a stop word, else return false
        return !StopWords.isStopWord(word);
    }
}
