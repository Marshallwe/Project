package com.shanzhu.em.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shanzhu.em.entity.Message;

import java.util.List;



public interface MessageService {


    List<Message> findAll();


    IPage<Message> findPage(Page page, Long goodId);


    Message findById(Integer id);

    Integer delete(Integer id);


    Integer update(Message message);

    Integer add(Message message);
}
