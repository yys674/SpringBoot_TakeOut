package com.bupt.TakeOut.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.bupt.TakeOut.entity.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component//声明为组件，以便扫描到起作用
@Slf4j//日志
public class MyMetaObjecthandler implements MetaObjectHandler {
    //插入时填充---重写该方法，写上具体填充内容
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段填充【insert】....");
        log.info(metaObject.toString());
        //设置填充属性的内容
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        //默认设为管理员创建
/*        metaObject.setValue("createUser", new Long(1));
        metaObject.setValue("updateUser", new Long(1));*/
        metaObject.setValue("createUser",BaseContext.getCurrentId());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());


    }

    //更新时填充
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段填充【update】....");
        log.info(metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
//        metaObject.setValue("updateUser", new Long(1));
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
    }
}
