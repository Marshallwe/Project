package com.example.spring.service.impl;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.spring.entity.Product;
import com.example.spring.mapper.ProductMapper;
import com.example.spring.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Resource
    private ProductMapper goodsMapper;
    @Override
    public IPage pageCC(IPage<Product> page, Wrapper wrapper) {
        return goodsMapper.pageCC(page,wrapper);
    }
}
