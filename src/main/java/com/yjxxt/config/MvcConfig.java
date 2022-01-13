package com.yjxxt.config;

import com.yjxxt.interceptors.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Bean
    public NoLoginInterceptor noLoginInterceptor() {
        return new NoLoginInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
// 需要一个实现HandlerInterceptor接口的拦截器实例，这里使用的是NoLoginInterceptor
        registry.addInterceptor(noLoginInterceptor())
                //添加拦截路径
                .addPathPatterns("/**")
                //放行路径
                .excludePathPatterns("/index", "/user/login", "/css/**", "/images/**", "/js/**", "/lib/**");
    }
}
