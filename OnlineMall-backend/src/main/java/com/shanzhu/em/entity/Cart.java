package com.shanzhu.em.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


@Data
@TableName("cart")
public class Cart extends Model<Cart> {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    private Integer count;


    private String createTime;


    private Long goodId;


    private String standard;


    private Long userId;

}