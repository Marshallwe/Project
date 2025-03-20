package com.example.spring.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


@Data
@TableName("good_standard")
public class GoodStandard extends Model<GoodStandard> {


    private Long goodId;


    private String value;


    private Double price;


    private Integer store;

}
