package com.wu.config.handler;

import com.wu.result.CodeEnum;
import com.wu.result.R;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.io.IOException;


@RestControllerAdvice //aop。拦截标注了@RestController的controller中的所有方法
public class GlobalExceptionHandler {


    @ExceptionHandler(value = Exception.class)
    public R handException(Exception e) {
        e.printStackTrace(); //在控制台打印异常信息
        return R.FAIL(e.getMessage());
    }


    @ExceptionHandler(value = DataAccessException.class)
    public R handException3(DataAccessException e) {
        e.printStackTrace(); //在控制台打印异常信息
        return R.FAIL(CodeEnum.DATA_ACCESS_EXCEPTION);
    }


    @ExceptionHandler(value = AccessDeniedException.class)
    public R handException(AccessDeniedException e) {
        e.printStackTrace(); //在控制台打印异常信息
        return R.FAIL(CodeEnum.ACCESS_DENIED);
    }
}
