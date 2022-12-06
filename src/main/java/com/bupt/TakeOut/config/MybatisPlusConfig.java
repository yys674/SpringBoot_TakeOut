package com.bupt.TakeOut.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
* MP中的分页插件，通过拦截器的方式将分页插件加入
* */
@Configuration
public class MybatisPlusConfig {
    @Bean
    //设置MP拦截器，=======
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        //创建拦截器
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        //将MP中的分页插件PaginationInnerInterceptor加入拦截器
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }
}
