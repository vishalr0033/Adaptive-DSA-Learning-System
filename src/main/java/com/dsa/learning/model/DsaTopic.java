package com.dsa.learning.model;

public enum DsaTopic {
    ARRAYS("Arrays"),
    STRINGS("Strings"),
    HASHMAP("HashMap"),
    STACK("Stack"),
    QUEUE("Queue"),
    LINKED_LIST("Linked List"),
    RECURSION("Recursion"),
    BINARY_SEARCH("Binary Search"),
    SLIDING_WINDOW("Sliding Window"),
    TWO_POINTER("Two Pointer"),
    SORTING("Sorting"),
    TREES("Trees"),
    GRAPHS("Graphs"),
    DYNAMIC_PROGRAMMING("Dynamic Programming");

    private final String displayName;

    DsaTopic(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static DsaTopic fromString(String text) {
        if (text == null) return ARRAYS;
        String clean = text.trim().toUpperCase().replace(" ", "_").replace("-", "_");
        for (DsaTopic topic : DsaTopic.values()) {
            if (topic.name().equalsIgnoreCase(clean) || topic.getDisplayName().equalsIgnoreCase(text.trim())) {
                return topic;
            }
        }
        return ARRAYS;
    }
}
