package com.maple.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping(value =  {"", "/","/home/{userId}","/login", "/select-character-tree/{userId}",
            "/kakao/callback", "/naver/callback", "/google/callback", "/mypage/{userId}"})
    public String forward() {
        return "forward:/index.html";
    }
}
