package com.shanzhu.em.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.math.BigDecimal;

@TableName("good_standard")
public class Standard extends Model<Standard> {
    private Integer goodId;
    private String name;  // 新增的 name 字段
    private String value;
    private BigDecimal price;
    private Integer store;

    // 新增 name 的 getter/setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 其他现有方法保持不变
    public Integer getGoodId() {
        return goodId;
    }

    public void setGoodId(Integer goodId) {
        this.goodId = goodId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return "Standard{" +
                "goodId=" + goodId +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", price=" + price +
                ", store=" + store +
                '}';
    }
}