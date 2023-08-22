package com.maple.oauth.provider.kakao;

import com.maple.entity.SocialType;
import com.maple.oauth.config.kakao.KakaoOauthConfig;
import com.maple.oauth.provider.AuthCodeRequestUrlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private final KakaoOauthConfig kakaoOauthConfig;

    @Override
    public SocialType supportServer() {
        return SocialType.KAKAO;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type","code")
                .queryParam("client_id",kakaoOauthConfig.getClientId())
                .queryParam("redirect_uri",kakaoOauthConfig.getRedirectUri())
                .queryParam("scope",String.join(",", kakaoOauthConfig.getScope()))
                .toUriString();
    }
}
