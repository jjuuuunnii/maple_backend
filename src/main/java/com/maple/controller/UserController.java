package com.maple.controller;

import com.maple.dto.user.req.UserSignupReqDto;
import com.maple.dto.user.req.UserTreeAndCharacterSaveReqDto;
import com.maple.dto.user.res.OwnerHomeResDto;
import com.maple.dto.user.res.UserInfoResDto;
import com.maple.login.service.PrincipalDetails;
import com.maple.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
        userService.saveUser(userSignupReqDto);
    }

    @GetMapping("/users")
    public UserInfoResDto getUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        return userService.getUserInfo(principalDetails.getUser().getId());
    }

    @GetMapping("/users/{socialId}")
    public OwnerHomeResDto getOwnerHome(@PathVariable String socialId){

        return userService.getOwnerHome(socialId);
    }

    @PutMapping("/users/{socialId}")
    public void saveTreeAndCharacter(@PathVariable String socialId, @RequestBody UserTreeAndCharacterSaveReqDto userTreeAndCharacterSaveReqDto) {
        userService.saveUserTreeAndCharacter(socialId, userTreeAndCharacterSaveReqDto);
    }

    @DeleteMapping("/auth/leave/{socialId}")
    public void leaveUser(@PathVariable String socialId) {
        userService.leaveUser(socialId);
    }

    @PutMapping("/auth/logout/{socialId}")
    public void logout(@PathVariable String socialId) {
        userService.logout(socialId);
    }

/*    @PostMapping("/testDataInit")
    public void saveTestData(){
        userService.saveTestData();
    }*/

    @GetMapping("/auth/refresh")
    public ResponseEntity<Void> refreshToken(){
        return ResponseEntity.ok().build();}
}
