package com.example.mealer_project.utils;

import android.util.Log;

import com.example.mealer_project.utils.TrieSearch.StopWords;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Calendar today = Calendar.getInstance();
        return today.getTime();
        //return DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).getCalendar().getTime();
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
     * @param data a list in which each value contains a sequence of words separated by space
     * @return a list of string in which each value represents one keyword, empty string if no keywords
     */
    static public List<String> getKeywords(List<String> data) {
        // validate data
        if (Preconditions.isNotNull(data)) {
            // list to store our keywords
            Map<String, Boolean> keywords = new HashMap<>();
            // iterate through each row containing sequence of words
            for (String rawWords: data) {
                // validate the row containing words is not a null value or empty string
                if (Preconditions.isNotEmptyString(rawWords)) {
                    // split on space
                    String[] words = rawWords.split("\\s+");
                    // store normalized word, ex: remove commas or semicolons
                    String normalizedWord;
                    // for each word
                    for (String word: words) {
                        Log.e("wordK", "w: " + word);
                        // if current word is not an empty string
                        if (Preconditions.isNotEmptyString(word)) {
                            // get normalized word
                            normalizedWord = getNormalizedWord(word);
                            // if word is a valid keyword, add it to our list of keywords
                            if (isKeyword(normalizedWord)) {
                                Log.e("wordK", "N: " + normalizedWord);
                                // if the keyword has not already been added
                                if (keywords.get(normalizedWord) == null) {
                                    keywords.put(normalizedWord, true);
                                }
                            }
                        }
                    }
                }
            }
            // return the list of keywords
            return new ArrayList<>(keywords.keySet());
        }

        // if in valid value, or can't find any keywords, we return empty list
        return new ArrayList<>();
    }

    static private String getNormalizedWord(String word) {
        // returns string only containing a-z, A-Z, and apostrophe
        return word.replaceAll("[^a-zA-Z0-9'-]","");
    }

    static private boolean isKeyword(String word) {
        // return true is word is not a stop word, else return false
        return !StopWords.isStopWord(word);
    }
}
