package com.example.printinfo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.printinfo.dao.UserRepository;
import com.example.printinfo.model.UserDto;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserDto> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    @Transactional
    public UserDto createUser(UserDto user) {
        // 실제 운영 환경에서는 Spring Security의 PasswordEncoder 등을 사용하여 비밀번호를 반드시 해싱해야 합니다.
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
      userRepository.create(user);
      
      return user;
    }

    @Transactional
    public UserDto updateUser(Integer id, UserDto userDetails) {
    	UserDto user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setUserName(userDetails.getUserName());
        user.setUserId(userDetails.getUserId());
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            // 실제 운영 환경에서는 여기에서도 비밀번호를 해싱해야 합니다.
            user.setPassword(userDetails.getPassword());
        }
        user.setUseYn(userDetails.getUseYn());

        userRepository.update(user);
        
        return user;
    }

    @Transactional
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }
}