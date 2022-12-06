package com.bupt.TakeOut.config;

import com.bupt.TakeOut.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

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

    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        //创建消息转换器
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
        //设置对象转换器，底层使用Jackson将java对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
        //将消息转换器追加到mvc框架的集合中，同时设为优先调用,否则不起作用
//        converters.add(messageConverter);
        converters.add(0,messageConverter);
        super.extendMessageConverters(converters);
    }
}
