package com.example.printinfo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.printinfo.dao.사용자Repository;
import com.example.printinfo.model.사용자Dto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final 사용자Repository 사용자Repository;

    public List<사용자Dto> getAllUsers() {
        return 사용자Repository.findAll();
    }

    public Optional<사용자Dto> getUserById(Integer id) {
        return 사용자Repository.findById(id);
    }

    @Transactional
    public 사용자Dto createUser(사용자Dto user) {
        // 실제 운영 환경에서는 Spring Security의 PasswordEncoder 등을 사용하여 비밀번호를 반드시 해싱해야 합니다.
        // user.set비밀번호(passwordEncoder.encode(user.get비밀번호()));
      사용자Repository.update(user);
      
      return user;
    }

    @Transactional
    public 사용자Dto updateUser(Integer id, 사용자Dto userDetails) {
    	사용자Dto user = 사용자Repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.set사용자명(userDetails.get사용자명());
        user.set아이디(userDetails.get아이디());
        if (userDetails.get비밀번호() != null && !userDetails.get비밀번호().isEmpty()) {
            // 실제 운영 환경에서는 여기에서도 비밀번호를 해싱해야 합니다.
            user.set비밀번호(userDetails.get비밀번호());
        }
        user.set사용여부(userDetails.get사용여부());

        사용자Repository.update(user);
        
        return user;
    }

    @Transactional
    public void deleteUser(Integer id) {
        사용자Repository.deleteById(id);
    }
}

