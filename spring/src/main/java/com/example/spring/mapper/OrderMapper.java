package com.example.spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.spring.entity.Order;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;


@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Update("update t_order set state = 'Paid for' where order_no = #{orderNo}")
    void payOrder(String orderNo);

    @MapKey("id")
    List<Map<String, Object>> selectByUserId(int userId);

    @Update("update t_order set state = 'Goods received' where order_no = #{orderNo}")
    boolean receiveOrder(String orderNo);

    Map<String, Object> selectByOrderNo(String orderNo);
}
