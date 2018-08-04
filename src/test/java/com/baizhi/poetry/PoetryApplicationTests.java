package com.baizhi.poetry;

import com.baizhi.entity.Poetry;
import com.baizhi.service.PoetryService;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
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
        // 从数据库获取原数据
        List<Poetry> poetries = poetryService.querrryAll();
        // 指定索引文件存储位置  有两种存储当时 基于内存存储  基于磁盘存储
        // 索引文件保存在内存中
        // RAMDirectory ramDirectory = new RAMDirectory();
        // 索引保存在磁盘中
        FSDirectory fsDirectory = FSDirectory.open(Paths.get("E:\\lucene\\02"));
        // 创建索引写入器 参数1：指定索引的信息 参数2：索引写入配置对象
        IndexWriter indexWriter = new IndexWriter(fsDirectory, new IndexWriterConfig(new IKAnalyzer()));

        for (Poetry poetry : poetries) {
            Document document= new Document();
            /*
             * 第一个参数：域名
             * 第二个参数：域值
             * 第三个参数：存储  存|不存
             *                 存：域值保存到索引文件中
             *                 不存：不会将域值保存到文件中
             */
            document.add(new TextField("id",poetry.getId(), Field.Store.YES)); // 指定数据的编号
            document.add(new TextField("title",poetry.getTitle(), Field.Store.YES));
            document.add(new TextField("content",poetry.getContent(), Field.Store.YES));
            document.add(new TextField("author",poetry.getPoet().getName(), Field.Store.YES));
            // 添加索引数据
            indexWriter.addDocument(document);
        }

        indexWriter.flush();
        indexWriter.commit();


    }
}
