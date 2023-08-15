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
}
