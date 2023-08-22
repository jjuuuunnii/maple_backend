package com.maple.oauth.client.kakao;


import com.maple.entity.SocialType;
import com.maple.entity.User;
import com.maple.oauth.client.OauthUserClient;
import com.maple.oauth.config.kakao.KakaoOauthConfig;
import com.maple.oauth.dto.kakao.KakaoToken;
import com.maple.oauth.dto.kakao.KakaoUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

@Component
@RequiredArgsConstructor
public class KakaoUserClient implements OauthUserClient {

    private final KakaoApiClient kakaoApiClient;
    private final KakaoOauthConfig kakaoOauthConfig;


    @Override
    public SocialType supportServer() {
        return SocialType.KAKAO;
    }

    @Override
    public User fetch(String authCode) {
        KakaoToken kakaoToken = kakaoApiClient.fetchToken(tokenRequestParams(authCode));
        KakaoUserResponse kakaoUserResponse = kakaoApiClient.fetchMember("Bearer " + kakaoToken.getAccessToken());
        return kakaoUserResponse.toUser();
    }

    private LinkedMultiValueMap<String, String> tokenRequestParams(String authCode) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOauthConfig.getClientId());
        params.add("redirect_uri", kakaoOauthConfig.getRedirectUri());
        params.add("code", authCode);
        params.add("client_secret", kakaoOauthConfig.getClientSecret());
        return params;
    }
}
