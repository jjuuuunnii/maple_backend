package com.maple.controller;

import com.maple.dto.user.req.UserTreeAndCharacterSaveReqDto;
import com.maple.dto.user.res.OwnerHomeResDto;
import com.maple.dto.user.res.UserInfoResDto;
import com.maple.dto.user.req.UserSignupReqDto;
import com.maple.entity.User;
import com.maple.login.service.PrincipalDetails;
import com.maple.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    public void saveUser(@RequestBody UserSignupReqDto userSignupReqDto) {
        log.info("email = {}", userSignupReqDto.getEmail());
        log.info("userName ={}", userSignupReqDto.getUserName());
        log.info("password = {}", userSignupReqDto.getPassword());
        userService.saveUser(userSignupReqDto);
    }

    @GetMapping("/test")
    public void test(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();
        log.info("name = {} , email = {} ", user.getName(), user.getEmail());
    }

    @GetMapping("/users")
    public UserInfoResDto getUserInfo(HttpServletRequest request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        return userService.getUserInfo(principalDetails.getUser().getId());
    }

    @GetMapping("/users/{userId}")
    public OwnerHomeResDto getOwnerHome(@PathVariable Long userId){
        return userService.getOwnerHome(userId);
    }

    @PutMapping("/users/{userId}")
    public void saveTreeAndCharacter(@PathVariable Long userId, @RequestBody UserTreeAndCharacterSaveReqDto userTreeAndCharacterSaveReqDto) {
        userService.saveUserTreeAndCharacter(userId, userTreeAndCharacterSaveReqDto);
    }

    @DeleteMapping("/auth/leave/{userId}")
    public void leaveUser(@PathVariable Long userId) {
        userService.leaveUser(userId);
    }

    @PutMapping("/auth/logout/{userId}")
    public void logout(@PathVariable Long userId) {
        userService.logout(userId);
    }

    @PostMapping("/testDataInit")
    public void saveTestData(){
        userService.saveTestData();
    }
}
