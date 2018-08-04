package com.baizhi.dao;

import com.baizhi.entity.Poetry;

import java.util.List;

/**
 * @program: SpringBoot
 * @description:
 * @author: zs
 * @create: 2018-07-16 20:33
 **/

public interface PoetryDAO {

    List<Poetry> listAll();

}
