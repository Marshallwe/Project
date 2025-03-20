package com.example.spring.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spring.entity.Address;
import com.example.spring.mapper.AddressMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AddressService extends ServiceImpl<AddressMapper, Address> {


    public List<Address> selectById(Long userId) {
        return lambdaQuery()
                .eq(Address::getUserId, userId)
                .list();
    }

}
