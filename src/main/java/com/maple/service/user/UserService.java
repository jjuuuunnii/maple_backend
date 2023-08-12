package com.maple.service.user;

import com.maple.dto.user.UserSignupDto;
import com.maple.entity.User;
import com.maple.exception.CustomException;
import com.maple.exception.ErrorCode;
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
    public void saveUser(UserSignupDto userSignupDto){
        validateDuplicateEmail(userSignupDto.getEmail());
        User user = User.toEntity(userSignupDto.getUserName(), userSignupDto.getEmail(), getEncodedPassword(userSignupDto));
        userRepository.save(user);
        log.info("{} 유저 회원가입 완료", user.getEmail());
    }

    @Transactional(readOnly = true)
    public void validateDuplicateEmail(String email) {
        userRepository.findByEmail(email)
                .ifPresent(u -> { throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS); });
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateTimeFromSignup() {
        int pageSize = 100;
        int pageNumber = 0;
        List<User> users;

        do {
            users = userRepository.findAllWithPaging(pageNumber, pageSize);
            users.forEach(user -> user.setTimeFromSignup(user.getTimeFromSignup() + 1));
            pageNumber++; // 다음 페이지로
        } while (!users.isEmpty()); // 더 이상 데이터가 없을 때까지 반복
    }


    private String getEncodedPassword(UserSignupDto userSignupDto) {
        return passwordEncoder.encode(userSignupDto.getPassword());
    }
}