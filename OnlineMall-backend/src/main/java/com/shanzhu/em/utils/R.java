package com.shanzhu.em.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {


    private int code;


    private String message;


    private T data;

}
