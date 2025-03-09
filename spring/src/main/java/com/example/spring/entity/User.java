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
@ApiModel(value="User entity", description="")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "account")
    private String no;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "password")
    private String password;

    private Integer age;

    @ApiModelProperty(value = "sex")
    private GenderEnum sex;
    public enum GenderEnum {
        MALE(0),
        FEMALE(1);
        private final int code;
        GenderEnum(int code) {
            this.code = code;
        }
        public int getCode() { return code; }
    }
    @ApiModelProperty(value = "phone")
    private String phone;

    @ApiModelProperty(value = "role 0supermanager 1manager 2normal")
    private Integer roleId;

    @ApiModelProperty(value = "whether valid,Yvalid,other invalid")
    @TableField(value = "is_valid")
    private Boolean valid;

}
