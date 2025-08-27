package com.example.printinfo.config;

import com.example.printinfo.model.UserDto;
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
		UserDto loginUser = (UserDto) session.getAttribute("loginUser");

		if (loginUser == null) {
			response.sendRedirect("/login.html");
			return false;
		}

		String authority = loginUser.getAuthority();
		String requestURI = request.getRequestURI();
		String method = request.getMethod();

		// Admin can do anything
		if ("관리자".equals(authority)) {
			return true;
		}

		// Read-only can only view
		if ("모든 데이터 조회".equals(authority)) {
			if (!"GET".equalsIgnoreCase(method)) {
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				return false;
			}
			return true;
		}

		// Print Team
		if ("인쇄팀 대시보드".equals(authority)) {
			if (requestURI.startsWith("/")) { // Assuming this is the API for logistics
				return true;
			}
			if (requestURI.startsWith("/api/prints")) {
				return true;
			}
			if (requestURI.startsWith("/printList.html") || requestURI.startsWith("/api/commoncodes")) {
				return true;
			}
			if ("GET".equalsIgnoreCase(method)) {
				return true;
			}
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return false;
		}

		// Logistics Team
		if ("물류팀 대시보드".equals(authority)) {
			if (requestURI.startsWith("/")) { // Assuming this is the API for logistics
				return true;
			}
			if (requestURI.startsWith("/api/logistics")) { // Assuming this is the API for logistics
				return true;
			}
			if (requestURI.startsWith("/logistList_1.html") || requestURI.startsWith("/api/commoncodes")) {
				return true;
			}
			if ("GET".equalsIgnoreCase(method)) {
				return true;
			}
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			return false;
		}

		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		return false;
	}
}
