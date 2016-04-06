# AutoComplete Using Sorted Array

#####Requirement:

- Implement Auto Complete for form validation.
- Expose a REST service through which it will be possible to send a prefix and get back response of all possible matches. 
- REST output should be plain text
- Each response match must be in one line. 

#####Assumptions:
1. Data set of around 3000 - 10000
2. Assuming that users might be intersted in searching for cities that have higher population.. Weight proportional to population. 
3. Fairly static data - It will be more read heavy rather than write / update(eg: new cities are not going to be added in real time)
4. Taken the cities and population list from (Link)[http://india.wikia.com/wiki/List_of_cities_and_towns_in_India]

#####Solution Approach:
This is a naive implementation of auto complete by sorting the data lexicographically and then use binary search to locate the data. Sorting is again done on the resulting matches by the order of population.

Other options considered:
1. Trie / Compressed Trie
2. Lucene Search Index with in memory index. - This will be a more flexible approach.

#####Installation:

Dependencies:

JDK 1.8+
MVN installable

Download jar file target/

```
mvn spring-boot:run
```

By default the application runs in the port 9080

Navigate to url to test the app:

```
http://localhost:9080/suggest/cities?q=ch&limit=100
```

