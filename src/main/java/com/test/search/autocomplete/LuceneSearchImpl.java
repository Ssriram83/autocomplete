package com.test.search.autocomplete;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

public class LuceneSearchImpl {

    private TopScoreDocCollector collector;
    private IndexReader reader;
    private IndexSearcher searcher;
    private final Directory index = new RAMDirectory();
    private final StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);

    public LuceneSearchImpl(String dataSourceLoc) throws IOException {
        buildIndex(dataSourceLoc);
        reader = DirectoryReader.open(index);
        searcher = new IndexSearcher(reader);
    }

    void buildIndex(String dataSourceLoc) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_40, analyzer);
        IndexWriter w = new IndexWriter(index, config);
        BufferedReader br = new BufferedReader(new FileReader(dataSourceLoc));
        String s = br.readLine();
        while (s != null) {
            String[] data = s.split("\t");
            Double population;
            try {
                population = Double.parseDouble(data[0]);
            } catch (Exception e) {
                population = 0D;
            }
            addDoc(w, data[1], population);
            s = br.readLine();
        }
        w.close();
    }

    public ArrayList getMatches(String queryStr, int hits) throws IOException, ParseException {
        collector = TopScoreDocCollector.create(hits, true);
        Query q = null;
        ArrayList results = new ArrayList();
        q = new QueryParser(Version.LUCENE_40, "city", analyzer).parse(queryStr);
        searcher.search(q, collector);
        ScoreDoc[] searchHits = collector.topDocs().scoreDocs;
        if (searchHits != null && searchHits.length > 0) {
            for (ScoreDoc hit : searchHits) {
                results.add(searcher.doc(hit.doc).get("city"));
            }
        }
        return results;
    }

    private static void addDoc(IndexWriter w, String city, double population) throws IOException {
        Document doc = new Document();
        doc.add(new StringField("city", city, Field.Store.YES));
        doc.add(new DoubleField("population", population, Field.Store.YES));
        w.addDocument(doc);
    }

}