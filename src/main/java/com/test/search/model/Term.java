package com.test.search.model;

import java.util.Comparator;

/**
 * Term is the atomic data typically -->  text, weight
 * <p>
 * Implements comparable:
 * Compares terms by weight
 * Compares terms lexicographically
 * </p>
 */
public class Term implements Comparable<Term> {
    public String getQuery() {
        return query;
    }

    private String query;
    private double weight;

    // Initialize a term with the given query string and weight.
    public Term(String query, double weight) {
        if (query == null) {
            throw new java.lang.NullPointerException();
        }
        if (weight < 0) {
            throw new java.lang.IllegalArgumentException();
        }
        this.query = query;
        this.weight = weight;
    }

    /**
     * Compare the terms in descending order by weight.
     *
     * @return Comparator
     */

    public static Comparator<Term> byReverseWeightOrder() {
        return new Comparator<Term>() {
            @Override
            public int compare(Term t1, Term t2) {
                if (t1.weight > t2.weight) {
                    return -1;
                } else if (t1.weight == t2.weight) {
                    return 0;
                } else {
                    return 1;
                }
            }
        };

    }

    /**
     * Compare the terms in lexicographic order.
     *
     * @return Comparator
     */

    public static Comparator<Term> byPrefixOrder(int r) {
        return new Comparator<Term>() {
            @Override
            public int compare(Term t1, Term t2) {
                String s1 = t1.query;
                String s2 = t2.query;
                int minlength = s1.length() < s2.length() ? s1.length() : s2.length();
                if (minlength >= r) {
                    return s1.substring(0, r).compareTo(s2.substring(0, r));
                } else if (s1.substring(0, minlength).compareTo(s2.substring(0, minlength)) == 0) {
                    if (s1.length() == minlength) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else {
                    return s1.substring(0, minlength).compareTo(s2.substring(0, minlength));
                }
            }
        };
    }


    /**
     * Compare the terms in lexicographic order by query.
     *
     * @return Comparator
     */

    @Override
    public int compareTo(Term that) {
        String s1 = this.query;
        String s2 = that.query;
        return s1.compareTo(s2);
    }

    @Override
    public String toString() {
        return Double.toString(this.weight) + "\t" + this.query;
    }
}