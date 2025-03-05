package com.example.spring.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Goods entity", description="")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Primary key")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "goods name")
    private String name;

    @ApiModelProperty(value = "warehouse")
    private Integer storage;

    @ApiModelProperty(value = "classification")
    @TableField("goodsType")
    private Integer goodstype;

    @ApiModelProperty(value = "number")
    private Integer count;

    @ApiModelProperty(value = "remark")
    private String remark;


}
