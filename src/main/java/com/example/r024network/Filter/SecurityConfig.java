package com.example.r024network.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    // 设置jwt
    @Bean
    public FilterRegistrationBean<JWTRequestFilter> jwtFilter(JWTRequestFilter jwtRequestFilter) {
        FilterRegistrationBean<JWTRequestFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(jwtRequestFilter);
        registrationBean.addUrlPatterns("/apifox/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}