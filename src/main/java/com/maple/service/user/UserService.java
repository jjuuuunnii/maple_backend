package com.maple.service.user;
import com.maple.dto.letter.LetterCountDto;
import com.maple.dto.user.req.UserTreeAndCharacterSaveReqDto;
import com.maple.dto.user.res.OwnerHomeResDto;
import com.maple.dto.user.res.UserInfoResDto;
import com.maple.dto.user.req.UserSignupReqDto;
import com.maple.entity.*;
import com.maple.exception.custom.CustomException;
import com.maple.exception.custom.ErrorCode;
import com.maple.repository.consolationLetter.ConsolationLetterRepository;
import com.maple.repository.letter.LetterRepository;
import com.maple.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 인코딩을 위한 인스턴스
    private final ConsolationLetterRepository consolationLetterRepository;
    private final LetterRepository letterRepository;
    private final EntityManager entityManager;

    @Transactional
    public void saveUser(UserSignupReqDto userSignupReqDto){
        validateDuplicateEmailAndSocialType(userSignupReqDto.getEmail());
        User user = User.toEntity(userSignupReqDto.getUserName(), userSignupReqDto.getEmail(), getEncodedPassword(userSignupReqDto));
        userRepository.save(user);
        log.info("{} 유저 회원가입 완료", user.getEmail());
    }

    @Transactional(readOnly = true)
    public void validateDuplicateEmailAndSocialType(String email) {
        userRepository.findDefaultUserByEmail(email)
                .ifPresent(u -> { throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS); });
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateTimeFromSignup() {
        int pageSize = 100;
        int pageNumber = 0;
        List<User> users;

        do {
            users = userRepository.findAllWithPaging(pageNumber, pageSize);
            users.forEach(user -> {
                user.setTimeFromSignup(user.getTimeFromSignup() + 1);
                user.setTodayMissionStatus(false);
            });
            pageNumber++;
        } while (!users.isEmpty());
    }


    private String getEncodedPassword(UserSignupReqDto userSignupReqDto) {
        return passwordEncoder.encode(userSignupReqDto.getPassword());
    }

    @Transactional(readOnly = true)
    public UserInfoResDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });
        return UserInfoResDto.builder()
                .userId(user.getId().toString())
                .userName(user.getName())
                .build();
    }

    @Transactional(readOnly = true)
    public OwnerHomeResDto getOwnerHome(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.USER_NOT_FOUND);
                });

        List<LetterCountDto> letterCounts = letterRepository.countAllLettersByDateUntilNowDate(userId)
                .orElse(Collections.emptyList());

        List<Boolean> lettersOverFive = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            final int day = i;

            if (i <= user.getTimeFromSignup()) {
                lettersOverFive.add(letterCounts.stream()
                        .anyMatch(lc -> lc.getCreatedAt() == day && lc.getCount() >= 5));
            } else {
                lettersOverFive.add(false);
            }
        }

        return OwnerHomeResDto.builder()
                .treeType(user.getTree())
                .characterType(user.getCharacter())
                .userName(user.getName())
                .nowDate(user.getTimeFromSignup())
                .lettersOverFive(lettersOverFive)
                .build();
    }




    @Transactional
    public void saveUserTreeAndCharacter(Long userId, UserTreeAndCharacterSaveReqDto userTreeAndCharacterSaveReqDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });

        user.updateTreeAndCharacter(
                userTreeAndCharacterSaveReqDto.getTreeType(),
                userTreeAndCharacterSaveReqDto.getCharacterType());

    }

    @Transactional
    public void leaveUser(Long userId) {
        userRepository.deleteById(userId);
    }
    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });
        user.setRefreshToken(null);
    }

    @Transactional
    public void saveTestData() {


        for (int i = 1; i <= 30; i++) {
            ConsolationLetter letter = ConsolationLetter.builder()
                    .senderName("단풍이")
                    .content("안녕하세요!!")
                    .build();
            consolationLetterRepository.save(letter);


            User user = User.builder()
                    .name("testUser" + i)
                    .email("testUser" + i + "@naver.com")
                    .password(passwordEncoder.encode("test"))
                    .socialType(SocialType.DEFAULT)
                    .missions(new ArrayList<>())
                    .timeFromSignup(i)
                    .build();

            user.setDefaultTreeAndCharacter();

            for (int nowDate = 1; nowDate <= 30; nowDate++) {
                Mission mission = Mission.builder()
                        .missionStatus(false)
                        .nowDate(nowDate)
                        .build();
                user.getMissions().add(mission);
                mission.setUser(user);
            }

            userRepository.save(user);
        }
    }
}
