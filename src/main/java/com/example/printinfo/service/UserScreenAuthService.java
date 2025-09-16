package com.example.printinfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.printinfo.dao.UserScreenAuthRepository;
import com.example.printinfo.model.UserScreenAuthDto;

@Service
public class UserScreenAuthService {

    @Autowired
    private UserScreenAuthRepository repository;

    public List<UserScreenAuthDto> findByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    public void saveAll(List<UserScreenAuthDto> auths) {
        repository.saveAll(auths);
    }
}
