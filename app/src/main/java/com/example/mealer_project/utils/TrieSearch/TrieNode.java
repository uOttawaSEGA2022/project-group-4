package com.example.mealer_project.utils.TrieSearch;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Represent a single Trie-node
 */
public class TrieNode {

    // map to store children nodes
    Map<Character, TrieNode> children;

    // flag to indicate if a complete word
    boolean isCompleteWord;

    /**
     * Constructor to initialize a Trie node
     */
    protected TrieNode() {
        this.children = new HashMap<>();
        this.isCompleteWord = false;
    }

    /**
     * Insert a word into the Trie
     * @param word string representing the word
     */
    protected void insert(String word)  {
        // currentNode will initially be root
        TrieNode currentNode = this;
        // use only lower case characters
        word = word.toLowerCase(Locale.ROOT);

        // add each character
        for (char c : word.toCharArray()) {
            // store a new child if character not already there
            if (currentNode.children.get(c) == null) {
                currentNode.children.put(c, new TrieNode());
            }
            // next node
            currentNode = currentNode.children.get(c);
        }

        // once all characters added, mark the word as complete
        currentNode.isCompleteWord = true;
    }

    /**
     * pMatch - Pattern Match
     * Method performs a non-exact search of a query in the Trie data
     * @param query string representing characters to be found in Trie
     * @return True, if match found, else false
     */
    protected boolean pMatch(String query) {
        // validate query
        if (query == null || query.isEmpty())
            return false;

        // turn query to lower case
        query = query.toLowerCase(Locale.ROOT);

        // start from root
        TrieNode currentNode = this;

        // check presence of characters
        for (char c: query.toCharArray()) {

            // get the node for current character
            currentNode = currentNode.children.get(c);
            // if character doesn't exist, means no match
            if (currentNode == null) {
                return false;
            }
        }
        // if all characters of query were found
        return true;
    }

    /**
     * eMatch - Exact Match
     * Method performs a search to find an exact match of the query provided in Trie data
     * @param word string representing the word to be found in Trie
     * @return True, if match found, else false
     */
    protected boolean eMatch(String word) {
        // validate word
        if (word == null || word.isEmpty())
            return false;

        // start from root
        TrieNode currentNode = this;

        // check presence of characters
        for (char c: word.toCharArray()) {

            // get the node for current character
            currentNode = currentNode.children.get(c);
            // if character doesn't exist, means no match
            if (currentNode == null) {
                return false;
            }
        }
        // return true if all characters were found, and we're reached the end of a complete word
        return currentNode.isCompleteWord;
    }
}
