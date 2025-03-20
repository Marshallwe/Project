package com.example.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spring.entity.Cart;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface CartMapper extends BaseMapper<Cart> {

    @MapKey("id")
    List<Map<String, Object>> selectByUserId(Long userId);
}
