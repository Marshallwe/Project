package com.example.spring;

import lombok.Data;

@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public static Result success() {
        return success(null);
    }
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.code = 200;
        result.message = "Success";
        result.data = data;
        return result;
    }

    public static Result<?> error(String message) {
        Result<?> result = new Result<>();
        result.code = 400;
        result.message = message;
        return result;
    }


}