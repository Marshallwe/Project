package com.example.spring.service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spring.entity.OrderGoods;
import com.example.spring.mapper.OrderGoodsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class OrderGoodsService extends ServiceImpl<OrderGoodsMapper, OrderGoods> {

}
