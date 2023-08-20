package com.maple.oauth.provider.google;

import com.maple.entity.SocialType;
import com.maple.oauth.config.google.GoogleOauthConfig;
import com.maple.oauth.provider.AuthCodeRequestUrlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GoogleAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private final GoogleOauthConfig googleOauthConfig;


    @Override
    public SocialType supportServer() {
        return SocialType.GOOGLE;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", googleOauthConfig.getClientId())
                .queryParam("redirect_uri", googleOauthConfig.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", String.join(" ", googleOauthConfig.getScope()))
                .toUriString();
    }
}
