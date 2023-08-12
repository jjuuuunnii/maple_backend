package com.maple.oauth2.userinfo;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo{

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getName() {
        Map<String, Object> account = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");
        if (account == null || profile == null) {
            return null;
        }

        return String.valueOf(profile.get("nickname"));
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = String.valueOf(kakaoAccount.get("email"));
        if (kakaoAccount == null || email == null) {
            return null;
        }
        return email;
    }
}