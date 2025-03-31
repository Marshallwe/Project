package com.shanzhu.em.config;

import com.shanzhu.em.common.R;
import com.shanzhu.em.exception.BizException;
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
