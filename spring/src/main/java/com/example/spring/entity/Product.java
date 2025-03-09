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
@ApiModel(value="Product entity", description="Inventory product entity")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Primary key", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "Product name", required = true, example = "iPhone 13")
    private String name;

    @ApiModelProperty(value = "Inventory ID", example = "1001")
    private Integer storage;

    @ApiModelProperty(value = "Product category", example = "2")
    @TableField("goodsType")
    private Integer goodstype;

    @ApiModelProperty(value = "Stock quantity", example = "500")
    private Integer count;

    @ApiModelProperty(value = "Product remarks", example = "Fragile items")
    private String remark;

    @ApiModelProperty(value = "Product status (0:in stock,1:out of stock)", example = "0")
    @TableField("prodStatus")
    private Integer prodstatus;
}