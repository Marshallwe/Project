package com.example.spring.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


@Data
@TableName("carousel")
public class Carousel extends Model<Carousel> {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    private Long goodId;

    private Integer showOrder;


    @TableField(exist = false)
    private String goodName;

    @TableField(exist = false)
    private String img;

}