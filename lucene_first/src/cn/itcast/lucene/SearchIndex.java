package cn.itcast.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class SearchIndex {
    private IndexReader reader;
    private IndexSearcher searcher;

    @Before
    public void init() throws Exception {
        // 1、创建一个Director对象，指定索引库的位置
        Directory directory = FSDirectory.open(new File("D:\\lucene\\index").toPath());
        // 2、创建一个IndexReader对象
        reader = DirectoryReader.open(directory);
        // 3、创建一个IndexSearcher对象，构造方法中的参数indexReader对象。
        searcher = new IndexSearcher(reader);
    }

    private void printResult(Query query) throws Exception {
        // 执行查询
        TopDocs topDocs = searcher.search(query, 10);
        System.out.println("总记录数" + topDocs.totalHits);
        Stream.of(topDocs.scoreDocs).parallel().forEachOrdered(doc -> {
            // 取文档id
            int docId = doc.doc;
            try {
                // 根据id取文档对象
                Document document = searcher.doc(docId);
                System.out.println(document.get("name"));
                System.out.println(document.get("path"));
                System.out.println(document.get("size"));
                // System.out.println(document.get("content"));
                System.out.println("-------------寂寞的分割线-------------");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    @Test
    public void testRangeQuery() throws Exception {
        // 创建一个Query对象
        Query query = LongPoint.newRangeQuery("size", 0l, 100l);
        printResult(query);
        // 关闭索引库
        reader.close();
    }



    @Test
    public void testQueryParser() throws Exception {
        // 创建一个QueryParser对象
        // 参数1: 默认搜索域 参数2: 分析器对象
        QueryParser queryParser = new QueryParser("name", new IKAnalyzer());
        // 使用QueryParser对象来创建一个Query对象
        Query query = queryParser.parse("lucene是一个Java开发的全文检索工具包");
        // 执行查询
        printResult(query);
        // 关闭索引库
        reader.close();
    }
}
