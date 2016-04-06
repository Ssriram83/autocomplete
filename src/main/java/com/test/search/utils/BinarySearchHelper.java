package com.test.search.utils;

import java.util.Comparator;

public class BinarySearchHelper {
    // Return the index of the first key in inputArray[] that equals the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] inputArray, Key term, Comparator<Key> comparator) {
        if (inputArray.length == 0) {
            return -1;
        }
        int l = 0;
        int r = inputArray.length - 1;
        while (l + 1 < r) {
            int mid = l + (r - l) / 2;
            if (comparator.compare(term, inputArray[mid]) <= 0) {
                r = mid;
            } else {
                l = mid;
            }
        }
        if (comparator.compare(term, inputArray[l]) == 0) {
            return l;
        }
        if (comparator.compare(term, inputArray[r]) == 0) {
            return r;
        }
        return -1;

    }

    // Return the index of the last key in a[] that equals the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] inputArray, Key searchTerm, Comparator<Key> comparator) {
        if (inputArray.length == 0) {
            return -1;
        }
        int l = 0;
        int r = inputArray.length - 1;
        while (l + 1 < r) {
            int mid = l + (r - l) / 2;
            if (comparator.compare(searchTerm, inputArray[mid]) < 0) {
                r = mid;
            } else {
                l = mid;
            }
        }
        if (comparator.compare(searchTerm, inputArray[r]) == 0) {
            return r;
        }
        if (comparator.compare(searchTerm, inputArray[l]) == 0) {
            return l;
        }
        return -1;
    }
}