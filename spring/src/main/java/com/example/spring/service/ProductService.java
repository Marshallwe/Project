package com.example.spring.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.spring.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;



public interface ProductService extends IService<Product> {
    IPage pageCC(IPage<Product> page, Wrapper wrapper);
}
