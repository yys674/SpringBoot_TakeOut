package com.bupt.TakeOut.common;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ResponseBody
@ControllerAdvice(annotations = {RestController.class, Controller.class})//对括号中的注解处进行捕获
public class GlobalExceptionHandler {

    //进行异常处理方法
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException e){
        log.error(e.getMessage());
        //如果为上述类型的错误，则提示用户已存在，否则报未知错误
        if(e.getMessage().contains("Duplicate entry")){
            //如果提示实体重复，则返回该用户已存在（思路不太好
            String[] split = e.getMessage().split(" ");
            String msg = split[2];
            return R.error(msg+"已经存在了！");
        }

        return R.error("未知错误");
    }

}
