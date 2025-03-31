package com.shanzhu.em.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.math.BigDecimal;


@Data
@TableName("t_order")
public class Order extends Model<Order> {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    private String orderNo;


    private BigDecimal totalPrice;


    private int userId;


    private String linkUser;


    private String linkPhone;


    private String linkAddress;


    private String state;


    private String createTime;


    @TableField(exist = false)
    private String goods;


    @TableField(exist = false)
    private Long cartId;

}