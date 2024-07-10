package org.ingcar_boot_war.config;

import org.ingcar_boot_war.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/**") // 모든 경로에 인터셉터 적용
                .excludePathPatterns("/","/login", "/loginPage", "/css/**", "/js/**", "/fonts/**", "/scss/**","/images/**","/check/checkUserId","/sendCode","/register","/check/findPw","/check/resetPassword","/register-success"); // 로그인이 필요하지 않은 경로 예외로 추가
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/templates/", "classpath:/static/")
                .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
    }
}