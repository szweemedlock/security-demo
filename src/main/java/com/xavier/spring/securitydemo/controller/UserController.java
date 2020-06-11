package com.xavier.spring.securitydemo.controller;

import com.xavier.spring.securitydemo.entity.Users;
import com.xavier.spring.securitydemo.service.IUsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user/api")
public class UserController {

    private final IUsersService usersService;

    public UserController(IUsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok().body("Hello, user");
    }

    @PostMapping
    public ResponseEntity<String> register(@RequestBody Users u) {
        if (usersService.register(u)) {
            return ResponseEntity.ok().body("注册成功");
        }
        return ResponseEntity.status(500).body("注册失败");
    }
}
