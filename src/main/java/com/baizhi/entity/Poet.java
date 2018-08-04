package com.baizhi.entity;

import java.io.Serializable;

/**
 * @program: SpringBoot
 * @description: 诗人类
 * @author: zs
 * @create: 2018-07-16 20:35
 **/

public class Poet implements Serializable {

    private String id;
    private String name;

    public Poet() {
    }

    public Poet(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Poet{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
