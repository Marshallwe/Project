package com.example.spring.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.spring.entity.Message;
import com.example.spring.service.MessageService;
import com.example.spring.utils.ApiResultHandler;
import com.example.spring.utils.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;


    @GetMapping("/messages/{goodId}/{page}/{size}")
    public R<IPage<Message>> findAll(@PathVariable("goodId") Long goodId, @PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        Page<Message> messagePage = new Page<>(page, size);
        IPage<Message> all = messageService.findPage(messagePage, goodId);
        return ApiResultHandler.buildApiResult(200, "Query all messages", all);
    }


    @GetMapping("/message/{id}")
    public R<Message> findById(@PathVariable("id") Integer id) {
        Message res = messageService.findById(id);
        return ApiResultHandler.buildApiResult(200, "Query by Id", res);
    }


    @DeleteMapping("/message/{id}")
    public Integer delete(@PathVariable("id") Integer id) {
        return messageService.delete(id);
    }


    @PostMapping("/message")
    public R<Integer> add(@RequestBody Message message) {
        Integer res = messageService.add(message);
        if (res == 0) {
            return ApiResultHandler.buildApiResult(400, "Add failure", res);
        } else {
            return ApiResultHandler.buildApiResult(200, "Added successfully", res);
        }
    }

}
