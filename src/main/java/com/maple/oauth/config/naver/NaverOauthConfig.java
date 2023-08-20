package com.maple.oauth.config.naver;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ConfigurationProperties(prefix = "oauth.naver")
public class NaverOauthConfig {
    String redirectUri;
    String clientId;
    String clientSecret;
    String[] scope;
    String state;
}
