package com.hqy.mdf.web.starter.config;

import com.hqy.mdf.web.starter.interceptor.DemoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hqy
 */
@Configuration
@Import({DemoInterceptor.class})
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private DemoInterceptor demoInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册身份验证拦截器
        registry.addInterceptor(demoInterceptor)
                .addPathPatterns("/**")  // 拦截所有请求
//                .excludePathPatterns("/login", "/static/**")// 排除登录接口和静态资源
        ;
    }
}
