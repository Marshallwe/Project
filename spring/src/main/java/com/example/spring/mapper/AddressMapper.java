package com.example.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spring.entity.Address;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface AddressMapper extends BaseMapper<Address> {

}
