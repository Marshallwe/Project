package com.example.spring.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.spring.entity.Message;
import com.example.spring.mapper.MessageMapper;
import com.example.spring.service.MessageService;
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
