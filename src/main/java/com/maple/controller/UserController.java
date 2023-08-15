package com.maple.controller;

import com.maple.dto.user.UserSignupDto;
import com.maple.entity.User;
import com.maple.login.service.PrincipalDetails;
import com.maple.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/signup/self")
    public void saveUser(@RequestBody UserSignupDto userSignupDto) {
        log.info("email = {}", userSignupDto.getEmail());
        log.info("userName ={}", userSignupDto.getUserName());
        log.info("password = {}", userSignupDto.getPassword());

        userService.saveUser(userSignupDto);
    }

    @GetMapping("/test")
    public void test(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        log.info("name = {} , email = {} ", user.getName(), user.getEmail());
    }


}
