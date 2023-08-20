package com.maple.oauth.client.naver;

import com.maple.entity.SocialType;
import com.maple.entity.User;
import com.maple.oauth.client.OauthUserClient;
import com.maple.oauth.config.naver.NaverOauthConfig;
import com.maple.oauth.dto.naver.NaverToken;
import com.maple.oauth.dto.naver.NaverUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class NaverUserClient implements OauthUserClient {

    private final NaverApiClient naverApiClient;
    private final NaverOauthConfig naverOauthConfig;

    @Override
    public SocialType supportServer() {
        return SocialType.NAVER;
    }

    @Override
    public User fetch(String authCode) {
        NaverToken tokenInfo = naverApiClient.fetchToken(tokenRequestParam(authCode));
        NaverUserResponse naverUserResponse = naverApiClient.fetchUser("Bearer "+ tokenInfo.getAccessToken());
        return naverUserResponse.toEntity();
    }

    private MultiValueMap<String, String> tokenRequestParam(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", naverOauthConfig.getClientId());
        params.add("client_secret", naverOauthConfig.getClientSecret());
        params.add("code", authCode);
        params.add("state", naverOauthConfig.getState());
        return params;
    }

}
