package com.dzd.product.exception;

import com.dzd.common.exception.BizCodeEnum;
import com.dzd.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author:DengZD
 * @description:
 * @date:2021-11-10
 * @modified By:
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.dzd.product.controller")
public class DMallExceptionControllerAdvice {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题{},异常类型{}", e.getMessage(), e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> map = new HashMap<>(10);
        bindingResult.getFieldErrors().forEach((item) -> {
            String field = item.getField();
            String defaultMessage = item.getDefaultMessage();
            map.put(field, defaultMessage);
        });
        return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), BizCodeEnum.VALID_EXCEPTION.getMsg()).put("data", map);
    }

    @ExceptionHandler(value = {Exception.class})
    public R handleException(Exception e) {
        log.error("出现了异常{},异常类型{}", e.getMessage(), e.getClass());
        return R.error(BizCodeEnum.UNKONW_EXCEPTON.getCode(), BizCodeEnum.UNKONW_EXCEPTON.getMsg());
    }

}
