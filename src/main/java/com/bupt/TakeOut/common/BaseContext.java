package com.bupt.TakeOut.common;

//通过ThreadLocal，保存和获取当前用户id
public class BaseContext {
    //存放id,用Long
    public static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
