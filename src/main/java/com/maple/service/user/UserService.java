package com.maple.service.user;
import com.maple.dto.user.req.UserTreeAndCharacterSaveReqDto;
import com.maple.dto.user.res.OwnerHomeResDto;
import com.maple.dto.user.res.UserInfoResDto;
import com.maple.dto.user.req.UserSignupReqDto;
import com.maple.entity.User;
import com.maple.exception.custom.CustomException;
import com.maple.exception.custom.ErrorCode;
import com.maple.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // 비밀번호 인코딩을 위한 인스턴스

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
                user.setTodayMissionStatus(false);  // 여기에서 todayMissionStatus를 false로 설정
            });
            pageNumber++; // 다음 페이지로
        } while (!users.isEmpty()); // 더 이상 데이터가 없을 때까지 반복
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


        return OwnerHomeResDto.builder()
                .treeType(user.getTree())
                .characterType(user.getCharacter())
                .userName(user.getName())
                .nowDate(user.getTimeFromSignup())
                .lettersOverFive(user.isLettersOverFive())
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
}
