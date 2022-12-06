package com.bupt.TakeOut.common;

//自定义异常
public class CustomException extends RuntimeException{
    //构造器
    public CustomException(String message) {
        super(message);
    }
}
