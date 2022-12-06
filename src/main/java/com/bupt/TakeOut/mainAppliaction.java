package com.bupt.TakeOut;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j//记录日志
@SpringBootApplication//作为boot启动类
@ServletComponentScan//为使得过滤器起作用，需要在启动类加上注解
public class mainAppliaction {
    public static void main(String[] args) {
        SpringApplication.run(mainAppliaction.class, args);
        log.info("启动成功...");//打印日志
    }
}
