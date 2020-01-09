package cn.itcast.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.BeforeClass;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;

public class IndexManager {

    private static IndexWriter writer;

    @BeforeClass
    public static void init() throws Exception {
        Directory directory = FSDirectory.open(new File("D:\\lucene\\index").toPath());
        // 创建一个IndexWriter对象,需要使用IKAnalyzer作为分析器
        writer = new IndexWriter(directory, new IndexWriterConfig(new IKAnalyzer()));
    }

    @Test
    public void addDocument() throws Exception {
        // 创建一个Document对象
        Document document = new Document();
        // 向document对象中添加域
        document.add(new TextField("name", "新添加的文件", Field.Store.YES));
        document.add(new TextField("content", "新添加的文件内容", Field.Store.NO));
        document.add(new StoredField("path", "D:\\lucene/hello"));
        // 把文档写入索引库
        writer.addDocument(document);
        // 关闭索引库
        writer.close();
    }

    @Test
    public void deleteAllDocument() throws IOException {
        // 删除全部文档
        writer.deleteAll();
        // 关闭索引库
        writer.close();
    }

    @Test
    public void deleteDocumentByQuery() throws IOException {
        // 删除全部文档
        writer.deleteDocuments(new Term("name", "apache"));
        // 关闭索引库
        writer.close();
    }

    @Test
    public void updateDocument() throws IOException {
        // 创建一个新的文档对象
        Document document = new Document();
        // 向document对象中添加域
        document.add(new TextField("name1", "更新之后的文档1", Field.Store.YES));
        document.add(new TextField("name2", "更新之后的文档2", Field.Store.YES));
        document.add(new TextField("name3", "更新之后的文档3", Field.Store.YES));
        // 更新操作
        writer.updateDocument(new Term("name","spring"), document);
        // 关闭索引库
        writer.close();
    }
}
