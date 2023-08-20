package com.maple.oauth.client.google;


import com.maple.entity.SocialType;
import com.maple.entity.User;
import com.maple.oauth.client.OauthUserClient;
import com.maple.oauth.config.google.GoogleOauthConfig;
import com.maple.oauth.dto.google.GoogleToken;
import com.maple.oauth.dto.google.GoogleUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class GoogleUserClient implements OauthUserClient {

    private final GoogleApiClient googleApiClient;
    private final GoogleOauthConfig googleOauthConfig;

    @Override
    public SocialType supportServer() {
        return SocialType.GOOGLE;
    }

    @Override
    public User fetch(String authCode) {
        GoogleToken tokenInfo = googleApiClient.fetchToken(tokenRequestParams(authCode));
        GoogleUserResponse googleUserResponse = googleApiClient.fetchUser("Bearer " + tokenInfo.getAccessToken());
        return googleUserResponse.toUser();
    }

    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id",googleOauthConfig.getClientId());
        params.add("client_secret", googleOauthConfig.getClientSecret());
        params.add("code", authCode);
        params.add("grant_type", "authorization_code");
        params.add("redirect_uri", googleOauthConfig.getRedirectUri());
        return params;
    }

}
