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

    @PostMapping("/users/{socialId}/letters")
    public void saveLetter(@PathVariable String socialId, @RequestBody LetterSaveReqDto letterSaveReqDto) {
        letterService.saveLetter(socialId, letterSaveReqDto);
    }

    @GetMapping("/users/{socialId}/letters")
    public LetterListResDto getLetterList(@PathVariable String socialId) {

        return letterService.getLetterList(socialId);
    }

    @GetMapping("/users/{socialId}/letters/{selectedDate}")
    public List<LetterInfoResDto> getLetterInfo(@PathVariable String socialId, @PathVariable int selectedDate) {
        return letterService.getLetterInfo(socialId, selectedDate);
    }

}
