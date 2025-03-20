package com.example.spring.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spring.entity.Cart;
import com.example.spring.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class CartService extends ServiceImpl<CartMapper, Cart> {

    private final CartMapper cartMapper;

    public List<Map<String, Object>> selectByUserId(Long userId) {
        return cartMapper.selectByUserId(userId);
    }
}
