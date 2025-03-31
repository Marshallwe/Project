package com.shanzhu.em.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.em.constants.Status;
import com.shanzhu.em.entity.Order;
import com.shanzhu.em.entity.OrderGoods;
import com.shanzhu.em.entity.OrderItem;
import com.shanzhu.em.exception.BizException;
import com.shanzhu.em.mapper.GoodMapper;
import com.shanzhu.em.mapper.OrderGoodsMapper;
import com.shanzhu.em.mapper.OrderMapper;
import com.shanzhu.em.mapper.StandardMapper;
import com.shanzhu.em.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class OrderService extends ServiceImpl<OrderMapper, Order> {

    private final OrderMapper orderMapper;

    private final OrderGoodsMapper orderGoodsMapper;

    private final StandardMapper standardMapper;

    private final GoodMapper goodMapper;

    private final CartService cartService;

    public IPage<Order> selectByPage(
            Integer pageNum,
            Integer pageSize,
            String orderNo,
            String state
    ) {
        QueryWrapper<Order> orderQueryWrapper =
                Wrappers.<Order>query()
                        .ne("state", "Pending_payment")
                        .eq(StrUtil.isNotBlank(state), "state", state)
                        .like(StrUtil.isNotBlank(orderNo), "order_no", orderNo)
                        .orderByDesc("create_time");

        return this.page(new Page<>(pageNum, pageSize), orderQueryWrapper);
    }


    @Transactional
    public String saveOrder(Order order) {
        order.setUserId(TokenUtils.getCurrentUser().getId());
        String orderNo = DateUtil.format(
                new Date(), DatePattern.PURE_DATETIME_PATTERN) + RandomUtil.randomNumbers(6);
        order.setOrderNo(orderNo);
        order.setCreateTime(DateUtil.now());
        orderMapper.insert(order);

        OrderGoods orderGoods = new OrderGoods();
        orderGoods.setOrderId(order.getId());

        String goods = order.getGoods();
        List<OrderItem> orderItems = JSON.parseArray(goods, OrderItem.class);
        for (OrderItem orderItem : orderItems) {
            Long good_id = orderItem.getId();
            String standard = orderItem.getStandard();
            int num = orderItem.getNum();
            orderGoods.setGoodId(good_id);
            orderGoods.setCount(num);
            orderGoods.setStandard(standard);

            orderGoodsMapper.insert(orderGoods);
        }

        cartService.removeById(order.getCartId());

        return orderNo;
    }


    @Transactional
    public void payOrder(String orderNo) {
        orderMapper.payOrder(orderNo);

        Map<String, Object> orderMap = orderMapper.selectByOrderNo(orderNo);
        int count = (int) orderMap.get("count");
        Long goodId = (Long) orderMap.get("goodId");
        String standard = (String) orderMap.get("standard");
        int store = standardMapper.getStore(goodId, standard);
        if (store < count) {
            throw new BizException(Status.CODE_500, "Shortage of goods in stock");
        }

        standardMapper.deductStore(goodId, standard, store - count);

        Order order = lambdaQuery().eq(Order::getOrderNo, orderNo).one();

        goodMapper.saleGood(goodId, count, order.getTotalPrice());
    }


    public List<Map<String, Object>> selectByUserId(int userid) {
        return orderMapper.selectByUserId(userid);
    }


    public Map<String, Object> selectByOrderNo(String orderNo) {
        return orderMapper.selectByOrderNo(orderNo);
    }


    public void delivery(String orderNo) {
        this.update(
                Wrappers.<Order>lambdaUpdate()
                        .eq(Order::getOrderNo, orderNo)
                        .set(Order::getState, "Shipped")
        );
    }


    public boolean receiveOrder(String orderNo) {
        return orderMapper.receiveOrder(orderNo);
    }

}
