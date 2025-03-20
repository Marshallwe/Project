package com.example.spring.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;


@Data
@TableName("avatar")
public class Avatar extends Model<Avatar> {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    private String type;


    private Long size;

    private String url;

    private String md5;

}
