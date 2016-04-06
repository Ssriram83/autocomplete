package com.test.search.autocomplete;

import com.test.search.model.Term;
import com.test.search.utils.BinarySearchHelper;

import java.io.*;
import java.lang.*;
//import BinarySearchDeluxe;
//import Term;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * This is a naive implementation of auto complete by sorting the data lexicographically
 * and then use binary search to locate the data. Sorting is again done on the resulting matches by the order of population.
 * <p>
 * {@link Term} - This class is used to represent the data that needs to be searched
 * {@link BinarySearchHelper} - This class is used for finding the helper
 * </p>
 * <p>
 *
 *     Considerations:
 *
 *     - Needed a representation to store the search terms and the weight of each search term.
 *     - I made an assumption that users are most like to search cities with higher population..
 *     - Data set - size = 5MB, entries = 4000 - 10000
 *     - Fairly static data - It is more read heavy rather than write(eg: new cities are not going to be added in real time)
 *
 *
 *    Options Considered:
 *     1. Lucene Search Index -
 *
 *     City can be stored as string field and population as numeric.
 *     Used RamDirectory for indexing faster read + data size is lower.
 *     Flexible to add new dimensions to the data..
 *     But for this simple problem - it proved to be an over-kill.
 *
 *     2. Trie / Ternary Search Tree -
 *
 *     I felt trie data structure will be very input sensitive.
 *     Imagined a skewed trie especially if input data is pre-sorted in some order.
 *     In a skewed tree retreival might not be optimal
 *     Also - could'nt figure out factoring the weight in a trie structure...
 *     Theoretically the time for retreival should be faster - O(N)
 *
 *     3. Naive Implementation:
 *      Sort the data lexicographically.
 *      use Binary Search by using the search query to narrow down the results
 *      Then apply sorting based on weight.
 *      Theoretically the time for retrieval should be O(Log (N)  + M Log M )
 *      [N=total number]
 *      [M = Matches]
 *
 *    Performance Results;
 *    Scenario: N= 3K records, Query term = used Ch, which had 100 matching entries, number of iterations = 100000
 *    Trie = 850ms
 *    Naive Implementation = 250ms
 *    Lucene = 1800ms
 *
 *    Considering the performance and the complexity decided to implement the Naive impl..
 *
 *
 * </p>
 */

public class ArraySearchImpl {
    private Term[] searchTerms;

    // Initialize the data structure from the given array of terms.
    public ArraySearchImpl(File dataSourceLoc) throws IOException {
        ArrayList<Term> tmp = new ArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(dataSourceLoc))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split("\t");
                Double weight = 0D;
                try {
                    weight = Double.parseDouble(data[0]);
                } catch (Exception e) {
                    weight = 0D;
                }
                tmp.add(new Term(data[1], weight));
            }
        }
        Collections.sort(tmp);
        this.searchTerms = tmp.toArray(new Term[tmp.size()]);
    }


    public ArrayList getMatches(String queryString, int hits) {
        if (queryString == null) {
            throw new java.lang.IllegalArgumentException();
        }
        Term temp = new Term(queryString, 0);

        int i = BinarySearchHelper.firstIndexOf(searchTerms, temp, Term.byPrefixOrder(queryString.length()));
        int j = BinarySearchHelper.lastIndexOf(searchTerms, temp, Term.byPrefixOrder(queryString.length()));
        if (i == -1 || j == -1) {
            return null;
        }

        Term[] matches = Arrays.copyOfRange(searchTerms, i, j);
        Arrays.sort(matches, Term.byReverseWeightOrder());
        ArrayList result = new ArrayList();
        for (Term match : matches) {
            result.add(match.getQuery());
        }
        return result;
    }

}