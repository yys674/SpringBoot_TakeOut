package com.bupt.TakeOut.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Slf4j//启用日志
@Configuration//标注为configuration组件
public class WebMvcConfig extends WebMvcConfigurationSupport {

    //ctrl+O,重写父类方法
    //对增加资源处理器进行重写
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始静态资源的映射");
        //设置路径，由此即可在backend下的目录进行检索
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        //front同理
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }
}
