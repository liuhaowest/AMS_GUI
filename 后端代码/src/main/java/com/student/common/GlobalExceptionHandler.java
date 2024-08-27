package com.student.common;

import com.student.common.exception.MajorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/**
 *
 * @author 李123
* 全局异常处理
* */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Result handler(Exception e) {
        if(e instanceof UnauthorizedException){
            return Result.fail(401,"你没有权限访问",null);
        } else if (e instanceof MajorException) {
            return Result.fail(402,e.getMessage(),null);
        } else {
            e.printStackTrace();
            return Result.fail(403, e.getMessage(), null);
        }
    }
}
