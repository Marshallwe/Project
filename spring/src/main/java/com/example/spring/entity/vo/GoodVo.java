package com.example.spring.entity.vo;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class GoodVo {


    private Long id;


    private String name;


    private String imgs;

    private BigDecimal price;

}
