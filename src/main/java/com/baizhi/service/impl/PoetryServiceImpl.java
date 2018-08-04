package com.baizhi.service.impl;

import com.baizhi.dao.PoetryDAO;
import com.baizhi.entity.Poetry;
import com.baizhi.service.PoetryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: SpringBoot
 * @description:
 * @author: zs
 * @create: 2018-07-16 20:43
 **/

@Service
@Transactional
public class PoetryServiceImpl implements PoetryService {

    @Autowired
    private PoetryDAO poetryDAO;

    @Override
    public List<Poetry> querrryAll() {
        return poetryDAO.listAll();
    }
}
