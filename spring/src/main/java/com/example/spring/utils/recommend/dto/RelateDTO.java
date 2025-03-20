package com.example.spring.utils.recommend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelateDTO {


    private Long userId;


    private Long productId;


    private Integer index;

}
