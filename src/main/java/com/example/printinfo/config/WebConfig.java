package com.example.printinfo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//인터셉터 등록 예시 (WebMvcConfigurer 구현 클래스)
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /File/** URL 요청이 오면
        // file.upload-dir에 설정된 실제 물리 경로에서 파일을 찾아 제공합니다.
        registry.addResourceHandler("/File/**")
                .addResourceLocations("file:" + uploadDir + "/");
    }

 @Override
 public void addInterceptors(InterceptorRegistry registry) {
     registry.addInterceptor(new AuthInterceptor())
             .addPathPatterns("/**") // /mypage/** 경로에만 인터셉터 적용
             .excludePathPatterns("/login.html", "/register","/api/auth/login", "/api/users/list", "/File/**", "/css/**", "/js/**", "/image/**"); // 로그인, 회원가입 및 정적 리소스 경로 제외
 }
}