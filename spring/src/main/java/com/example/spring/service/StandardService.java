package com.example.spring.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spring.entity.Standard;
import com.example.spring.mapper.StandardMapper;
import org.springframework.stereotype.Service;


@Service
public class StandardService extends ServiceImpl<StandardMapper, Standard> {


    public boolean delete(Standard standard) {
        return lambdaUpdate()
                .eq(Standard::getGoodId, standard.getGoodId())
                .eq(Standard::getValue, standard.getValue())
                .remove();
    }


    public void deleteAll(Integer goodId) {
        lambdaUpdate()
                .eq(Standard::getGoodId, goodId)
                .remove();
    }
}
