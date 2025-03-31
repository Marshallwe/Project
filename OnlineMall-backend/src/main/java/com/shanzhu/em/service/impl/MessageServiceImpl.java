package com.shanzhu.em.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shanzhu.em.entity.Message;
import com.shanzhu.em.mapper.MessageMapper;
import com.shanzhu.em.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageMapper messageMapper;


    @Override
    public List<Message> findAll() {
        return messageMapper.findAllMessage();
    }


    @Override
    public IPage<Message> findPage(Page page, Long goodId) {
        return messageMapper.findAll(page,goodId);
    }


    @Override
    public Message findById(Integer id) {
        return messageMapper.findById(id);
    }


    @Override
    public Integer delete(Integer id) {
        return messageMapper.delete(id);
    }


    @Override
    public Integer update(Message message) {
        return messageMapper.update(message);
    }


    @Override
    public Integer add(Message message) {
        return messageMapper.add(message);
    }
}
