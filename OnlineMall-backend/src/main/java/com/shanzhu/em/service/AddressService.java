package com.shanzhu.em.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shanzhu.em.entity.Address;
import com.shanzhu.em.mapper.AddressMapper;
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
