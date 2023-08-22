package com.maple.controller;
import com.maple.dto.letter.req.LetterSaveReqDto;
import com.maple.dto.letter.res.LetterInfoResDto;
import com.maple.dto.letter.res.LetterListResDto;
import com.maple.service.letter.LetterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
public class LetterController {

    private final LetterService letterService;

    @PostMapping("/users/{userId}/letters")
    public void saveLetter(@PathVariable Long userId, @RequestBody LetterSaveReqDto letterSaveReqDto) {
        letterService.saveLetter(userId, letterSaveReqDto);
    }

    @GetMapping("/users/{userId}/letters")
    public LetterListResDto getLetterList(@PathVariable Long userId) {
        return letterService.getLetterList(userId);
    }

    @GetMapping("/users/{userId}/letters/{selectedDate}")
    public List<LetterInfoResDto> getLetterInfo(@PathVariable Long userId, @PathVariable int selectedDate) {
        return letterService.getLetterInfo(userId, selectedDate);
    }

    @PostMapping("/auth/login/kakao")
    public String redirectToKakaoAuth(){
        log.info("Kakao");
        return "redirect:/oauth2/authorization/kakao";
    }

    @PostMapping("/auth/login/naver")
    public String redirectToNaverAuth(){
        log.info("naver");
        return "redirect:/oauth2/authorization/naver";
    }
    @PostMapping("/auth/login/google")
    public String redirectToGoogleAuth(){
        log.info("google");
        return "redirect:/oauth2/authorization/google";
    }


}
