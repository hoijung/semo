package com.example.printinfo.config;

import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

//세션 체크 인터셉터 예시
public class AuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		if (session.getAttribute("loginUser") == null) {
			// 세션에 사용자 정보가 없으면 로그인 페이지로 리다이렉트
			response.sendRedirect("/login.html");
			return false; // 요청 처리 중단
		}
		return true; // 요청 처리 계속    
	}
}
