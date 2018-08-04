package com.baizhi.poetry;

import com.baizhi.entity.Poetry;
import com.baizhi.service.PoetryService;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class PoetryApplicationTests {

    @Autowired
    private PoetryService poetryService;

    /**
     * 从数据库获取原数据，然后经过分词，创建索引文件
     * @throws Exception
     */
    @Test
    public void contextLoads() throws Exception {
        List<Poetry> poetries = poetryService.querrryAll();
//        poetries.forEach(poetry -> System.out.println(poetry));
        FSDirectory fsDirectory = FSDirectory.open(Paths.get("E:\\lucene\\02"));

        IndexWriter indexWriter = new IndexWriter(fsDirectory, new IndexWriterConfig(new IKAnalyzer()));

        for (Poetry poetry : poetries) {
            Document document= new Document();
            // Store.YES | NO 代表是不是要将域值保存到索引库的数据存储区
            document.add(new TextField("id",poetry.getId(), Field.Store.YES)); // 指定数据的编号
            document.add(new TextField("title",poetry.getTitle(), Field.Store.YES));
            document.add(new TextField("content",poetry.getContent(), Field.Store.YES));
            document.add(new TextField("author",poetry.getPoet().getName(), Field.Store.YES));

            indexWriter.addDocument(document);
        }
        indexWriter.flush();

        indexWriter.commit();


    }
}
