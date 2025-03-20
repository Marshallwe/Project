package com.example.spring.config;


import com.example.spring.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/login", "/register", "/file/**", "/avatar/**", "/api/good/**", "/api/icon/**",
                        "/messages/**", "/api/category/**", "/api/market/**", "/api/carousel/**", "/message","/messages/**","/replay/**"
                )
                .order(0);

        WebMvcConfigurer.super.addInterceptors(registry);
    }


}