package com.maple.oauth.dto.kakao;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;


@Getter
@Setter
@Builder
@AllArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class KakaoToken{
    private String tokenType;
    private String accessToken;
    private Integer expiresIn;
    private String refreshToken;
    private String refreshTokenExpiresIn;
    private String scope;

    public KakaoToken() {}
}
