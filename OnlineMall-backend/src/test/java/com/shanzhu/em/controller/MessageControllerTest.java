package com.shanzhu.em.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shanzhu.em.entity.Message;
import com.shanzhu.em.service.MessageService;
import com.shanzhu.em.utils.R;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    @Test
    void findById_ExistingId_ReturnsMessage() {
        Integer testId = 1;
        Message mockMessage = createTestMessage();

        when(messageService.findById(testId)).thenReturn(mockMessage);

        R<Message> result = messageController.findById(testId);

        assertNotNull(result.getData());
        assertEquals(200, result.getCode());
        assertEquals("Query by Id", result.getMessage());
        assertEquals(testId, result.getData().getId());
    }

    @Test
    void delete_ValidId_ReturnsSuccess() {

        Integer testId = 1;

        when(messageService.delete(testId)).thenReturn(1);

        Integer result = messageController.delete(testId);

        assertEquals(Integer.valueOf(1), result);
        verify(messageService).delete(testId);
    }

    @Test
    void add_SuccessfulInsert_ReturnsSuccess() {
        Message testMessage = createTestMessage();

        when(messageService.add(testMessage)).thenReturn(1);

        R<Integer> result = messageController.add(testMessage);

        assertEquals(200, result.getCode());
        assertEquals("Added successfully", result.getMessage());
        assertEquals(1, result.getData().intValue());
    }

    @Test
    void add_FailedInsert_ReturnsFailure() {
        Message testMessage = createTestMessage();

        when(messageService.add(testMessage)).thenReturn(0);

        R<Integer> result = messageController.add(testMessage);

        assertEquals(400, result.getCode());
        assertEquals("Add failure", result.getMessage());
        assertEquals(0, result.getData().intValue());
    }

    private Message createTestMessage() {
        Message message = new Message();
        message.setId(1);
        message.setTitle("Test Title");
        message.setContent("Test Content");
        message.setScore(5);
        message.setUserId(10001L);
        message.setTime(new Date());
        message.setGoodId(1001L);
        return message;
    }
}