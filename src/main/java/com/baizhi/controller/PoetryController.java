package com.baizhi.controller;

import com.baizhi.entity.Poet;
import com.baizhi.entity.Poetry;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: SpringBoot
 * @description: 从前端获取关键字，并对关键字进行分词，然后返回结果
 * @author: zs
 * @create: 2018-07-16 21:10
 **/

@RestController
public class PoetryController {

    @RequestMapping("/search")
    public Map<String, Object> searchAll(String keyword, Integer page, Integer rows) throws Exception {
        // 打开本地索引文件存放的地址
        FSDirectory fsDirectory = FSDirectory.open(Paths.get("E:\\lucene\\02"));

        IndexReader indexReader = DirectoryReader.open(fsDirectory);

        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        // 设置使用的分词器 这个地方由于IKAnalyzer maven中心仓库没有所以要自己下载jar包然后安装到maven本地仓库
        QueryParser queryParser = new QueryParser("content",new IKAnalyzer());

        // 根据分词后的关键字查所有命中的诗句
        Query query = queryParser.parse(keyword);

        TopDocs topDocs = null;

        // 当前页
        int nowPage = page;
        // 每页显示的数据
        int pageSize = rows;

        // 分页查询 Lucene的分页查询实现起来比较麻烦
        // 当前页为1时则直接查询单页的数据
        if(nowPage <= 1){
            topDocs = indexSearcher.search(query,pageSize);
        }else{
            // 当前页大于1时需要先查出当前页之前N页的所有数据
            topDocs = indexSearcher.search(query,(nowPage-1)*pageSize);
            // 当前页的第一条记录的上一条ScoreDoc对象
            ScoreDoc before = topDocs.scoreDocs[topDocs.scoreDocs.length-1];
            // 踢出当前页之前的数据获取当前页的数据
            topDocs = indexSearcher.searchAfter(before, query, pageSize);
        }

        System.out.println("符合条件的记录条数："+topDocs.totalHits);

        // 高亮器
        Scorer scorer = new QueryScorer(query);
        // 通过添加一个<span>标签来实现高亮
        Formatter formatter = new SimpleHTMLFormatter("<span style='color:red;font-size:20px'>","</span>");
        Highlighter highlighter = new Highlighter(formatter,scorer);

        // 分数文档对象 文档得分 + 文档在索引库中的编号
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        ArrayList<Poetry> poetries = new ArrayList<Poetry>();

        for (ScoreDoc scoreDoc : scoreDocs) {
            System.out.println("文档的得分："+scoreDoc.score); //lucene 分数自动相关度排序
            int docID = scoreDoc.doc;
            System.out.println("文档在数据存储区中的编号："+docID);
            Document document = indexReader.document(docID);
            String content = highlighter.getBestFragment(new StandardAnalyzer(), "content", document.get("content"));

            Poetry poetry = new Poetry();
            poetry.setId(document.get("id"));
            poetry.setTitle(document.get("title"));
            poetry.setPoet(new Poet(null,document.get("author")));
            poetry.setContent(content);
            poetries.add(poetry);
        }

        // 前端使用的是EasyUI的分页展示，所以此处返回的是一个Map的Json形式
        Map<String, Object> mapEmps = new HashMap<String, Object>();
        mapEmps.put("total", topDocs.totalHits);
        mapEmps.put("rows", poetries);
        return mapEmps;
    }


}
