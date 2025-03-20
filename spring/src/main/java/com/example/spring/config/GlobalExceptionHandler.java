package com.example.spring.config;


import com.example.spring.common.R;
import com.example.spring.exception.BizException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(BizException.class)
    public R handle(BizException e) {
        return R.error(e.getCode(), e.getMessage());
    }

}
