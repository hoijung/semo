package com.example.printinfo.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.printinfo.dao.AuthService;
import com.example.printinfo.model.UserDto;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserDto request, HttpSession session) {

		UserDto user = authService.login(request);
		
		if (user == null || !"1".equals(user.getUseYn())) {
		    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
		            .body(Map.of("status", "fail", "message", "아이디 또는 비밀번호가 틀렸습니다."));
		}
		// 세션에 저장
		session.setAttribute("loginUser", user);
		// 로그인 성공
		return ResponseEntity.ok(Map.of("status", "success", "user", user)); 
	}

	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "로그아웃 완료";
	}

    @GetMapping("/user")
    public ResponseEntity<?> getUser(HttpSession session) {
        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(loginUser);
    }
}