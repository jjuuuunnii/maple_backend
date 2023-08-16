package com.maple.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${my-env.first}")
    private String first;

    @Value("${my-env.second}")
    private Integer second;

    @GetMapping("/hello")
    String ping() {
        return String.format(
                "안녕! 설정된 정보는 FIRST = [%s], SECOND= [%d] !",
                first,
                second);
    }
}
