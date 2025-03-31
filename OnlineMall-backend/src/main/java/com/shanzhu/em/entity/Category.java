package com.shanzhu.em.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


@Data
@TableName("category")
public class Category extends Model<Category> {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    private String name;


    @TableField(exist = false)
    private Long iconId;

}