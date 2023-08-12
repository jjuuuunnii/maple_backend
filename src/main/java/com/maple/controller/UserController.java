package com.maple.controller;

import com.maple.dto.user.UserSignupDto;
import com.maple.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup/self")
    public void saveUser(@RequestBody UserSignupDto userSignupDto) {
        log.info("email = {}", userSignupDto.getEmail());
        log.info("userName ={}", userSignupDto.getUserName());
        log.info("password = {}", userSignupDto.getPassword());

        userService.saveUser(userSignupDto);
    }
}
