package com.maple.service.user;

import com.maple.dto.letter.LetterCountDto;
import com.maple.dto.user.req.UserSignupReqDto;
import com.maple.dto.user.req.UserTreeAndCharacterSaveReqDto;
import com.maple.dto.user.res.OwnerHomeResDto;
import com.maple.dto.user.res.UserInfoResDto;
import com.maple.dto.user.res.VisitorHomeResDto;
import com.maple.entity.SocialType;
import com.maple.entity.User;
import com.maple.exception.custom.CustomException;
import com.maple.exception.custom.ErrorCode;
import com.maple.repository.consolationLetter.ConsolationLetterRepository;
import com.maple.repository.letter.LetterRepository;
import com.maple.repository.user.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final EntityManager em;


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 인코딩을 위한 인스턴스
    private final ConsolationLetterRepository consolationLetterRepository;
    private final LetterRepository letterRepository;


    @Transactional
    public void saveUser(UserSignupReqDto userSignupReqDto){
        validateDuplicateEmailAndSocialType(userSignupReqDto.getEmail());
        User user = User.toEntity(userSignupReqDto.getUserName(), userSignupReqDto.getEmail(), getEncodedPassword(userSignupReqDto), SocialType.DEFAULT, UUID.randomUUID().toString());
        userRepository.save(user);
        log.info("{} 유저 회원가입 완료", user.getEmail());
    }

    @Transactional(readOnly = true)
    public void validateDuplicateEmailAndSocialType(String email) {
        userRepository.findDefaultUserByEmail(email)
                .ifPresent(u -> { throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS); });
    }

  /*  @Transactional
    @Scheduled(cron = "0 59 2 * * ?")
    public void updateTimeFromSignup() {

        int batchSize = 100;
        int offset = 0;

        while (true) {
            List<User> users = userRepository.findAll(offset, batchSize);
            if (users.isEmpty()) {
                break;
            }

            for (User user : users) {
                user.setTimeFromSignup(user.getTimeFromSignup() + 1);
                user.setTodayMissionStatus(false);
            }

            offset += batchSize;
        }

        // 배치 업데이트를 위해 flush 호출
        em.flush();
        em.clear();
    }
*/

    @Transactional
    @Scheduled(cron = "0 1 3 * * ?")
    public void updateTimeFromSignup() {

        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            em.lock(user, LockModeType.PESSIMISTIC_WRITE); // 페시미스틱 락 적용
            user.setTimeFromSignup(user.getTimeFromSignup() + 1);
            user.setTodayMissionStatus(false);
        });
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
                .userId(user.getSocialId())
                .userName(user.getName())
                .build();
    }

    @Transactional(readOnly = true)
    public OwnerHomeResDto getOwnerHome(String socialId) {

        User user = userRepository.findBySocialId(socialId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.USER_NOT_FOUND);
                });

        List<LetterCountDto> letterCounts = letterRepository.countAllLettersByDateUntilNowDate(user.getId())
                .orElse(Collections.emptyList());

        List<Boolean> lettersOverFive = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            final int day = i;

            if (i <= user.getTimeFromSignup()) {
                lettersOverFive.add(letterCounts.stream()
                        .anyMatch(lc -> lc.getCreatedAt() == day && lc.getCount() >= 2));
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

    @Transactional(readOnly = true)
    public VisitorHomeResDto getVisitorHome(String socialId) {

        User user = userRepository.findBySocialId(socialId)
                .orElseThrow(() -> {
                    throw new CustomException(ErrorCode.USER_NOT_FOUND);
                });

        List<LetterCountDto> letterCounts = letterRepository.countAllLettersByDateUntilNowDate(user.getId())
                .orElse(Collections.emptyList());

        List<Boolean> lettersOverFive = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            final int day = i;

            if (i <= user.getTimeFromSignup()) {
                lettersOverFive.add(letterCounts.stream()
                        .anyMatch(lc -> lc.getCreatedAt() == day && lc.getCount() >= 2));
            } else {
                lettersOverFive.add(false);
            }
        }

        return VisitorHomeResDto.builder()
                .treeType(user.getTree())
                .characterType(user.getCharacter())
                .userName(user.getName())
                .nowDate(user.getTimeFromSignup())
                .lettersOverFive(lettersOverFive)
                .build();
    }




    @Transactional
    public void saveUserTreeAndCharacter(String socialId, UserTreeAndCharacterSaveReqDto userTreeAndCharacterSaveReqDto) {
        User user = userRepository.findBySocialId(socialId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });

        user.updateTreeAndCharacter(
                userTreeAndCharacterSaveReqDto.getTreeType(),
                userTreeAndCharacterSaveReqDto.getCharacterType());

        log.info("=== {} 유저, 나무 = {}, 캐릭터 = {} 저장완료 ===", user.getTree(), user.getCharacter());
    }

    @Transactional
    public void leaveUser(String socialId) {
        User user = userRepository.findBySocialId(socialId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });
        String userName = user.getName();
        userRepository.deleteById(user.getId());
        log.info("=== {} 유저 삭제 ===", userName);

    }
    @Transactional
    public void logout(String socialId) {
        User user = userRepository.findBySocialId(socialId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });
        user.setRefreshToken(null);
        log.info("=== {} 유저 로그아웃 ===", user.getName());

    }


}
