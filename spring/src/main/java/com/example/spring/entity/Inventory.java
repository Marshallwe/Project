package com.example.spring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Inventory entity", description="Inventory management entity")
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Primary key", example = "10001")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "SKU code", required = true, example = "SKU202309001")
    @TableField("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "Current stock quantity", example = "1500")
    private Integer quantity;

    @ApiModelProperty(value = "Last update time", example = "2023-09-15 14:30:00")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "Logical deletion flag (0:normal 1:deleted)", example = "0")
    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;
}