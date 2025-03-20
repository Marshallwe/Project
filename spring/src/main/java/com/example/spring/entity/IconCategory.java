package com.example.spring.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName(value="icon_category")
public class IconCategory {


    private Long iconId;


    private Long categoryId;

}
