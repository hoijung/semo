package com.example.printinfo.web;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.printinfo.model.사용자Dto;
import com.example.printinfo.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<사용자Dto> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public 사용자Dto createUser(@RequestBody 사용자Dto user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<사용자Dto> updateUser(@PathVariable Integer id, @RequestBody 사용자Dto userDetails) {
    	사용자Dto updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}