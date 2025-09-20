package com.example.printinfo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.printinfo.model.UserScreenAuthDto;
import com.example.printinfo.service.UserScreenAuthService;

@RestController
@RequestMapping("/api/user-screen-auth")
public class UserScreenAuthController {

    @Autowired
    private UserScreenAuthService service;

    @GetMapping("/{userId}")
    public List<UserScreenAuthDto> getByUserId(@PathVariable String userId) {
        return service.findByUserId(userId);
    }

        @PostMapping
    public void saveAll(@RequestBody List<UserScreenAuthDto> auths) {
        service.saveAll(auths);
    }

    @PostMapping("/single")
    public void insert(@RequestBody UserScreenAuthDto auth) {
        service.insert(auth);
    }

    @DeleteMapping
    public void delete(@RequestBody UserScreenAuthDto auth) {
        service.delete(auth);
    }
}
