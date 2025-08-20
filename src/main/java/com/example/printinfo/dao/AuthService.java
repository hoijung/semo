package com.example.printinfo.dao;

import org.springframework.stereotype.Service;

import com.example.printinfo.model.사용자Dto;

@Service
public class AuthService {

    private final 사용자Repository 사용자Repo;

    public AuthService(사용자Repository 사용자Repo) {
        this.사용자Repo = 사용자Repo;
    }

    public 사용자Dto login(사용자Dto 요청) {
    	
    	사용자Dto result;
    	
    	result = 사용자Repo.findBy아이디(요청.get아이디(), 요청.get비밀번호());
    	    	
        return result;
    }
}