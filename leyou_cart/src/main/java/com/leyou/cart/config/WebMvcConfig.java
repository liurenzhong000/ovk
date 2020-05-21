package com.leyou.cart.config;

import com.leyou.cart.interceptor.CartInterceptor;
import com.leyou.cart.properties.JwtProperties;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableConfigurationProperties(JwtProperties.class)
@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtProperties jwtProp;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CartInterceptor(jwtProp)).addPathPatterns("/**");
    }
}
