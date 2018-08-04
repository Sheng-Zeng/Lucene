package com.baizhi.entity;

import java.io.Serializable;

/**
 * @program: SpringBoot
 * @description: 诗
 * @author: zs
 * @create: 2018-07-16 20:38
 **/

public class Poetry implements Serializable {

    private String id;
    private String content;
    private String title;
    private Poet poet;
    public Poetry() {
    }

    public Poetry(String id, String content, String title, Poet poet) {
        this.id = id;
        this.content = content;
        this.title = title;
        this.poet = poet;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Poet getPoet() {
        return poet;
    }

    public void setPoet(Poet poet) {
        this.poet = poet;
    }

    @Override
    public String toString() {
        return "Poetry{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", poet=" + poet +
                '}';
    }
}
