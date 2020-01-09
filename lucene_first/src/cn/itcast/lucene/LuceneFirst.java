package cn.itcast.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class LuceneFirst {
    @Test
    public void createIndex() throws IOException {
        // 1、创建一个Director对象，指定索引库的位置
        // 1.1 把索引库保存在内存中,不安全,一般不用
        // Directory directory = new RAMDirectory();
        // 1.2 把索引库保存在磁盘中
        Directory directory = FSDirectory.open(new File("D:\\lucene\\index").toPath());
        // 2、基于Directory对象创建一个IndexWriter对象
        IndexWriter writer = new IndexWriter(directory, new IndexWriterConfig(new IKAnalyzer()));
        // 3、读取磁盘上的文件，对应每个文件创建一个文档对象。
        File dir = new File("D:\\BaiduNetdiskDownload\\12-lucene\\02.参考资料\\searchsource");
        // 获取目录下的文件
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                // 3.1 获取文件名
                String fileName = file.getName();
                // 3.2 获取文件的路径
                String filePath = file.getPath();
                // 3.3 获取文件的内容
                String fileContent = FileUtils.readFileToString(file, "utf-8");
                // 3.4 获取文件的大小
                long fileSize = FileUtils.sizeOf(file);
                // 3.5 创建Field
                // 3.5.1 参数1:域的名称,参数1:域的内容,参数3:是否存储
                Field fieldName = new TextField("name", fileName, Field.Store.YES);
                // Field fieldPath = new TextField("path", filePath, Field.Store.YES);
                Field fieldPath = new StoredField("path", filePath);
                Field fieldContent = new TextField("content", fileContent, Field.Store.YES);
                // Field fieldSize = new TextField("size", fileSize + "", Field.Store.YES);
                Field fieldSizeStore = new StoredField("size", fileSize + "");
                Field fieldSizeValue = new LongPoint("size", fileSize);
                // 4、创建文档对象
                Document document = new Document();
                // 4.1 向文档对象中添加域
                document.add(fieldName);
                document.add(fieldPath);
                document.add(fieldContent);
                document.add(fieldSizeStore);
                // document.add(fieldSize);
                document.add(fieldSizeValue);
                // 5、将文档对象写入索引库
                writer.addDocument(document);
            }
        }
        // 6、关闭IndexWriter对象
        writer.close();
    }

    @Test
    public void searchIndex() throws IOException {

        // 1、创建一个Director对象，指定索引库的位置
        Directory directory = FSDirectory.open(new File("D:\\lucene\\index").toPath());
        // 2、创建一个IndexReader对象
        IndexReader reader = DirectoryReader.open(directory);
        // 3、创建一个IndexSearcher对象，构造方法中的参数indexReader对象。
        IndexSearcher searcher = new IndexSearcher(reader);
        // 4、创建一个Query对象，TermQuery
        // Query query = new TermQuery(new Term("content", "spring"));
        Query query = new TermQuery(new Term("name", "spring"));
        // 5、执行查询，得到一个TopDocs对象
        // 参数1: 查询对象 参数2: 查询结果返回的最大记录数
        TopDocs topDocs = searcher.search(query, 10);
        // 6、取查询结果的总记录数
        System.out.println("查询总记录数:" + topDocs.totalHits);
        // 7、取文档列表
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        // 8、打印文档中的内容
        Stream.of(scoreDocs).parallel().forEachOrdered(doc -> {
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
        // 9、关闭IndexReader对象
        reader.close();
    }

    @Test
    public void testTokenStream() throws Exception {
        // 1、创建一个Analyzer对象，StandardAnalyzer对象
        // Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer = new IKAnalyzer();
        // 2、使用分析器对象的tokenStream方法获得一个TokenStream对象
        // TokenStream tokenStream = analyzer.tokenStream("", "The Spring Framework provides a comprehensive programming and configuration model.");
        TokenStream tokenStream = analyzer.tokenStream("", "同时包含key1传智播客和key2的关键词,同时色情,允许暴力跨行,则可以这样搜索");
        // 3、向TokenStream对象中设置一个引用，相当于数一个指针
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        // 4、调用TokenStream对象的rest方法。如果不调用抛异常
        tokenStream.reset();
        // 5、使用while循环遍历TokenStream对象
        while (tokenStream.incrementToken())
            System.out.println(charTermAttribute.toString());
        // 6、关闭TokenStream对象
        tokenStream.close();
    }
}
