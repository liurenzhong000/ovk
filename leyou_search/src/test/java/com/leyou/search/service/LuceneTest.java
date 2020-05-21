package com.leyou.search.service;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class LuceneTest {

    @Test
    public void createIndex(){
        try {
            Directory directory = FSDirectory.open(new File("/Users/liurenzhong/index").toPath());
            IndexWriter indexWriter = new IndexWriter(directory,new IndexWriterConfig());
            Field field1 = new TextField("name","liurenzhong", Field.Store.YES);
            Field field2 = new TextField("age","23", Field.Store.YES);
            Field field3 = new TextField("score","99", Field.Store.YES);
            Document document = new Document();
            document.add(field1);
            document.add(field2);
            document.add(field3);
            indexWriter.addDocument(document);
            indexWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
