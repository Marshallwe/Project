package com.shanzhu.em.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shanzhu.em.common.R;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.Order;
import com.shanzhu.em.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    void findOrder_ValidUserId_ReturnsOrderList() {
        Integer testUserId = 1;
        List<Map<String, Object>> mockOrders = Collections.singletonList(
                Map.of("orderNo", "202308150001", "totalPrice", new BigDecimal("99.99"))
        );

        when(orderService.selectByUserId(testUserId)).thenReturn(mockOrders);

        R<List<Map<String, Object>>> result = orderController.findOrder(testUserId);

        assertNotNull(result.getData());
        assertEquals(Status.CODE_200, result.getCode());
        assertEquals(1, result.getData().size());
        verify(orderService).selectByUserId(testUserId);
    }

    @Test
    void selectByOrderNo_ValidOrderNo_ReturnsOrder() {
        String testOrderNo = "202308150001";
        Map<String, Object> mockOrder = Map.of(
                "orderNo", testOrderNo,
                "state", "Pending payment"
        );

        when(orderService.selectByOrderNo(testOrderNo)).thenReturn(mockOrder);

        R<Map<String, Object>> result = orderController.selectByOrderNo(testOrderNo);

        assertNotNull(result.getData());
        assertEquals(testOrderNo, result.getData().get("orderNo"));
        verify(orderService).selectByOrderNo(testOrderNo);
    }

    @Test
    void findAllOrder_ReturnsAllOrders() {
        List<Order> mockOrders = Collections.singletonList(createTestOrder());

        when(orderService.list()).thenReturn(mockOrders);

        R<List<Order>> result = orderController.findAllOrder();

        assertEquals(1, result.getData().size());
        verify(orderService).list();
    }
    @Test
    void findPage_WithFilters_ReturnsPagedData() {
        int pageNum = 1;
        int pageSize = 10;
        String orderNo = "20230815";
        String state = "Pending payment";
        IPage<Order> mockPage = new Page<>(pageNum, pageSize);
        mockPage.setRecords(Collections.singletonList(createTestOrder()));

        when(orderService.selectByPage(pageNum, pageSize, orderNo, state)).thenReturn(mockPage);

        R<IPage<Order>> result = orderController.findPage(pageNum, pageSize, orderNo, state);

        assertNotNull(result.getData());
        assertEquals(1, result.getData().getRecords().size());
        verify(orderService).selectByPage(pageNum, pageSize, orderNo, state);
    }

    @Test
    void save_ValidOrder_ReturnsSuccess() {
        Order testOrder = createTestOrder();
        String expectedResult = "Order created successfully";

        when(orderService.saveOrder(testOrder)).thenReturn(expectedResult);

        R<String> result = orderController.save(testOrder);

        assertEquals(expectedResult, result.getData());
        verify(orderService).saveOrder(testOrder);
    }

    @Test
    void payOrder_ValidOrderNo_ExecutesPayment() {
        String testOrderNo = "202308150001";

        R<Void> result = orderController.payOrder(testOrderNo);

        assertEquals(Status.CODE_200, result.getCode());
        verify(orderService).payOrder(testOrderNo);
    }

    @Test
    void delivery_ValidOrderNo_UpdatesStatus() {
        String testOrderNo = "202308150001";

        R<Void> result = orderController.delivery(testOrderNo);

        assertEquals(Status.CODE_200, result.getCode());
        verify(orderService).delivery(testOrderNo);
    }

    @Test
    void receiveOrder_Success_ReturnsSuccess() {
        String testOrderNo = "202308150001";

        when(orderService.receiveOrder(testOrderNo)).thenReturn(true);

        R<Void> result = orderController.receiveOrder(testOrderNo);

        assertEquals(Status.CODE_200, result.getCode());
        verify(orderService).receiveOrder(testOrderNo);
    }

    @Test
    void update_ValidOrder_ReturnsSuccess() {
        Order testOrder = createTestOrder();
        R<Void> result = orderController.update(testOrder);
        assertEquals(Status.CODE_200, result.getCode());
        verify(orderService).updateById(testOrder);
    }

    @Test
    void delete_ValidOrderId_RemovesOrder() {
        Long testOrderId = 1L;

        R<Void> result = orderController.delete(testOrderId);

        assertEquals(Status.CODE_200, result.getCode());
        verify(orderService).removeById(testOrderId);
    }

    private Order createTestOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderNo("202308150001");
        order.setTotalPrice(new BigDecimal("199.99"));
        order.setUserId(1001);
        order.setLinkUser(" Zhang SAN ");
        order.setLinkPhone("13800138000");
        order.setLinkAddress(" Chaoyang District, Beijing ");
        order.setState(" to be paid ");
        order.setCreateTime("2023-08-15 10:00:00");
        return order;
    }
}