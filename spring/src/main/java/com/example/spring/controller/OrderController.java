package com.example.spring.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.spring.common.R;
import com.example.spring.constants.Status;
import com.example.spring.entity.Order;
import com.example.spring.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @GetMapping("/userid/{userid}")
    public R<List<Map<String, Object>>> findOrder(@PathVariable Integer userid) {
        return R.success(orderService.selectByUserId(userid));
    }


    @GetMapping("/orderNo/{orderNo}")
    public R<Map<String, Object>> selectByOrderNo(@PathVariable String orderNo) {
        return R.success(orderService.selectByOrderNo(orderNo));
    }


    @GetMapping
    public R<List<Order>> findAllOrder() {
        return R.success(orderService.list());
    }


    @GetMapping("/page")
    public R<IPage<Order>> findPage(
            @RequestParam Integer pageNum,
            @RequestParam Integer pageSize,
            String orderNo,
            String state
    ) {
        return R.success(orderService.selectByPage(pageNum, pageSize, orderNo, state));
    }

    @PostMapping
    public R<String> save(@RequestBody Order order) {
        return R.success(orderService.saveOrder(order));
    }


    @GetMapping("/paid/{orderNo}")
    public R<Void> payOrder(@PathVariable String orderNo) {
        orderService.payOrder(orderNo);
        return R.success();
    }

    @GetMapping("/delivery/{orderNo}")
    public R<Void> delivery(@PathVariable String orderNo) {
        orderService.delivery(orderNo);
        return R.success();
    }


    @GetMapping("/received/{orderNo}")
    public R<Void> receiveOrder(@PathVariable String orderNo) {
        if (orderService.receiveOrder(orderNo)) {
            return R.success();
        } else {
            return R.error(Status.CODE_500, "Confirm receipt failed");
        }
    }


    @PutMapping
    public R<Void> update(@RequestBody Order order) {
        orderService.updateById(order);
        return R.success();
    }


    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        orderService.removeById(id);
        return R.success();
    }


}
