package com.maple.service.letter;

import com.maple.dto.letter.LetterCountDto;
import com.maple.dto.letter.req.LetterSaveReqDto;
import com.maple.dto.letter.res.LetterInfoResDto;
import com.maple.dto.letter.res.LetterListResDto;
import com.maple.entity.ConsolationLetter;
import com.maple.entity.Letter;
import com.maple.entity.User;
import com.maple.exception.custom.CustomException;
import com.maple.exception.custom.ErrorCode;
import com.maple.repository.consolationLetter.ConsolationLetterRepository;
import com.maple.repository.letter.LetterRepository;
import com.maple.repository.user.UserRepository;
import com.maple.service.mission.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LetterService {

    private final UserRepository userRepository;
    private final LetterRepository letterRepository;
    private final ConsolationLetterRepository consolationLetterRepository;
    private final MissionService missionService;

    @Transactional
    public void saveLetter(Long userId, LetterSaveReqDto letterSaveReqDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });

        Letter letter = Letter.builder()
                .senderName(letterSaveReqDto.getSenderName())
                .content(letterSaveReqDto.getLetterContent())
                .createdAt(user.getTimeFromSignup())
                .localDateTime(LocalDateTime.now())
                .build();
        letter.setLetterUser(user);

        String content = letter.getSenderName()+" "+letter.getContent();

        /**
         *
         * TODO
         */
        if (missionService.checkWordInLetter(content, user.getTimeFromSignup()) ||
                missionService.checkLetterCount(user.letterCnt(), user.getTimeFromSignup()) ||
                missionService.checkLetterTime(letter.getLocalDateTime(), user.getTimeFromSignup())) {
            user.setTodayMissionStatus(true);
        }


        letterRepository.save(letter);
    }


    public LetterListResDto getLetterList(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });

        LetterCountDto letterCountDto = new LetterCountDto();
        Map<Integer, Long> countByDate = letterRepository.countAllLettersByDateUntilNowDate(userId)
                .orElse(Collections.emptyList())
                .stream()
                .collect(Collectors.toMap(LetterCountDto::getCreatedAt, LetterCountDto::getCount));

        List<Boolean> lettersOverFive = new ArrayList<>();

        for (int date = 1; date <= 30; date++) {
            long count = countByDate.getOrDefault(date, 0L);
            if (date <= user.getTimeFromSignup()) {
                lettersOverFive.add(count >= 5);
            } else {
                lettersOverFive.add(false);
            }
        }

        return LetterListResDto.builder()
                .treeType(user.getTree())
                .nowDate(user.getTimeFromSignup())
                .lettersOverFive(lettersOverFive)
                .build();
    }

    public List<LetterInfoResDto> getLetterInfo(Long userId, int selectedDate) {
        List<Letter> letters = letterRepository.findByUserIdAndSelectedDate(userId, selectedDate);

        if (letters.size() < 5) {
            ConsolationLetter consolationLetter = consolationLetterRepository.findByNowDate(selectedDate);
            LetterInfoResDto consolationDto = LetterInfoResDto.builder()
                    .senderName(consolationLetter.getSenderName())
                    .letterContent(consolationLetter.getContent())
                    .build();

            List<LetterInfoResDto> consolationList = new ArrayList<>();
            consolationList.add(consolationDto);
            return consolationList;
        }

        return letters.stream()
                .map(letter -> LetterInfoResDto.builder()
                        .senderName(letter.getSenderName())
                        .letterContent(letter.getContent())
                        .build())
                .collect(Collectors.toList());
    }


}
