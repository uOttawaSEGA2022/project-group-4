package com.example.mealer_project.utils.TrieSearch;

import java.util.List;

/**
 * Store a data object to be used in storing TriesSearch
 */
public class TriesDataObj {
    private String key;
    private List<String> words;

    public TriesDataObj(String key, List<String> words) {
        this.key = key;
        this.words = words;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}
