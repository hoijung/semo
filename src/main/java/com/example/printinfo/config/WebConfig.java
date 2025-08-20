package com.example.printinfo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//인터셉터 등록 예시 (WebMvcConfigurer 구현 클래스)
@Configuration
public class WebConfig implements WebMvcConfigurer {

 @Override
 public void addInterceptors(InterceptorRegistry registry) {
     registry.addInterceptor(new AuthInterceptor())
             .addPathPatterns("/**") // /mypage/** 경로에만 인터셉터 적용
             .excludePathPatterns("/login.html", "/register","/api/auth/login"); // 로그인, 회원가입 경로는 제외
 }
}