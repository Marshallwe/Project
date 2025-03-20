package com.example.spring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.List;


@Data
@TableName(value = "icon")
public class Icon extends Model<Icon> {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    private String value;


    @TableField(exist = false)
    private List<Category> categories;

}