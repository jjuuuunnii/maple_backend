package com.maple.oauth.dto.kakao;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.maple.entity.User;
import com.maple.entity.SocialType;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.*;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
@Builder
@JsonNaming(SnakeCaseStrategy.class)
public class KakaoUserResponse {
    private Long id;
    private boolean hasSignedUp;
    private LocalDateTime connectedAt;
    private KakaoAccount kakaoAccount;
    private Profile profile;


    public KakaoUserResponse(){}

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class KakaoAccount {
        private boolean profileNeedsAgreement;
        private boolean profileNicknameNeedsAgreement;
        private boolean profileImageNeedsAgreement;
        private Profile profile;
        private boolean nameNeedsAgreement;
        private String name;
        private boolean emailNeedsAgreement;
        private boolean isEmailValid;
        private boolean isEmailVerified;
        private String email;
        private boolean ageRangeNeedsAgreement;
        private String ageRange;
        private boolean birthyearNeedsAgreement;
        private String birthyear;
        private boolean birthdayNeedsAgreement;
        private String birthday;
        private String birthdayType;
        private boolean genderNeedsAgreement;
        private String gender;
        private boolean phoneNumberNeedsAgreement;
        private String phoneNumber;
        private boolean ciNeedsAgreement;
        private String ci;
        private LocalDateTime ciAuthenticatedAt;

        public KakaoAccount(){}
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    public static class Profile {
        private String nickname;
        private String thumbnailImageUrl;
        private String profileImageUrl;
        private boolean isDefaultImage;

        public Profile(){}
    }

    public User toUser() {
        return User.toEntity(kakaoAccount.profile.nickname, kakaoAccount.email, UUID.randomUUID().toString(), SocialType.KAKAO);
    }
}
