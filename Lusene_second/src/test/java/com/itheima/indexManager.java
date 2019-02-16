package com.itheima;

import com.itheima.dao.IBookDao;
import com.itheima.dao.impl.BookDaoImpl;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.flexible.standard.nodes.NumericRangeQueryNode;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import pojo.Book;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class indexManager {

    @Test
    public void createIndex() throws IOException {
        IBookDao bookDao = new BookDaoImpl();
        List<Book> bookList = bookDao.findAll();

        List<Document> documents = new ArrayList<>();
        for (Book book : bookList) {
            Document doc = new Document();

            doc.add(new StringField("bookId", book.getId() + "", Field.Store.YES));
            doc.add(new TextField("bookName", book.getBookname() + "", Field.Store.YES));
            doc.add(new TextField("bookPrice", book.getPrice() + "", Field.Store.YES));
            doc.add(new TextField("bookPic", book.getPic() + "", Field.Store.YES));
            doc.add(new TextField("bookDesc", book.getBookdesc() + "", Field.Store.YES));
            documents.add(doc);
        }

        // 分词器
        Analyzer analyzer = new StandardAnalyzer();

        // 索引创建器配置
        IndexWriterConfig indexWriterConfig =
                new IndexWriterConfig(Version.LUCENE_4_10_3, analyzer);
        // 设置打开模式为每次都创建
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        // 索引库目录
        Directory directory = FSDirectory.open(new File("F:/index2"));
        // 索引操作对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        for (Document document : documents) {
            // 文档写入索引库
            indexWriter.addDocument(document);
            // 提交事物
            indexWriter.commit();
        }
        // 释放资源
        indexWriter.close();
    }

    // 搜索索引库
    @Test
    public void searchIndex() throws Exception {
        Analyzer analyzer = new StandardAnalyzer();
        QueryParser queryParser = new QueryParser("bookName", analyzer);
        Query query = queryParser.parse("java");
        //Query query = NumericRangeQuery.newDoubleRange("bookPrice", 80d, 100d, true, true);
        System.out.println("检索语句: " + query);
        Directory directory = FSDirectory.open(new File("f:/index2"));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("总命中记录数: " + topDocs.totalHits);

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("文档id: "+scoreDoc.doc + "文档分值: " + scoreDoc.score);
            System.out.println("<===================|---|");

            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println("图书ID: " + doc.get("bookId"));
            System.out.println("图书名称: " + doc.get("bookName"));
            System.out.println("图书价格: " + doc.get("bookPrice"));
            System.out.println("图书照片: " + doc.get("bookPic"));
            System.out.println("图书描述: " + doc.get("bookDesc"));
        }
    }
	
	// 测试git
	@Test
	public void testGit() {
		System.out.println("git")
		System.out.println("svn")
	}

}
