package com.example.printinfo.dao;

import org.springframework.stereotype.Service;
import com.example.printinfo.model.*;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto login(UserDto request) {
    	
    	UserDto result;
    	
    	result = userRepository.findByUserIdAndPassword(request.getId(), request.getPassword());
    	    	
        return result;
    }
}