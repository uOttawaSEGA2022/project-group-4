package com.example.mealer_project.utils.TrieSearch;

import java.util.*;

/**
 * Trie-based Search Utility Program
 * @author Pranav Kural
 * @version 1.0.0
 *
 * What this utility accomplishes?
 *
 * Why use this utility for auto-complete or text query search?
 * -> For average cases, this utility will perform the search in under linear time
 */
public class TriesSearchTest {

    Map<String, List<String>> triesData;
    TriesSearch triesSearch;

    public TriesSearchTest() {
        triesData = getSampleTriesMap();
        triesSearch = new TriesSearch(triesData);
    }

    private static ArrayList<String> getStringList(String words) {
        String[] keywords = words.split("\\s+");
        return new ArrayList<>(Arrays.asList(keywords));
    }

    private static List<TriesDataObj> getSampleData() {
        // list to store data
        List<TriesDataObj> triesDataObjs = new ArrayList<>();

        triesDataObjs.add(new TriesDataObj("1", getStringList("pizza italian tomato basil")));
        triesDataObjs.add(new TriesDataObj("2", getStringList("pasta italian cheese broccoli")));
        triesDataObjs.add(new TriesDataObj("3", getStringList("noodles chinese spicy vegan")));
        triesDataObjs.add(new TriesDataObj("4", getStringList("salad european healthy basil")));
        triesDataObjs.add(new TriesDataObj("5", getStringList("tofu pakistani soy tomato cheese healthy")));

        return triesDataObjs;
    }

    private static Map<String, List<String>> getSampleTriesMap() {
        // list to store data
        List<TriesDataObj> triesDataObjs = getSampleData();

        // map to be used to generate tries for tries data
        Map<String, List<String>> triesData = new HashMap<>();

        for (TriesDataObj dataObj : triesDataObjs) {
            triesData.put(dataObj.getKey(), dataObj.getWords());
        }

        return triesData;
    }

    private void printMatch(String query, String expected) {
        System.out.println("Testing match for '" + query + "'; expected: '"+ expected +"'");
        System.out.println("Match for " + query + ": " + triesSearch.pMatch(query));
    }

    public static void main(String[] args) {

        TriesSearchTest searchTest = new TriesSearchTest();

        searchTest.printMatch("vegan", "3");
        searchTest.printMatch("pa", "2,5");
        searchTest.printMatch("tom", "1,5");
        searchTest.printMatch("spic", "3");
        searchTest.printMatch("spice", "");
        searchTest.printMatch("milk", "");
        searchTest.printMatch("basil", "1,4");

    }

}
