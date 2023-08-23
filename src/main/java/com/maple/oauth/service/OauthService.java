package com.maple.oauth.service;

import com.maple.entity.SocialType;
import com.maple.entity.User;
import com.maple.jwt.service.JwtService;
import com.maple.oauth.composite.AuthCodeRequestUrlProviderComposite;
import com.maple.oauth.composite.OauthUserClientComposite;
import com.maple.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class OauthService {

    private final AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    private final OauthUserClientComposite oauthUserClientComposite;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public String getAuthCodeRequestUrl(SocialType socialType) {
        return authCodeRequestUrlProviderComposite.provide(socialType);
    }

    @Transactional
    public void loginWithSocial(SocialType socialType, String authCode, HttpServletResponse response) {
        User user = fetchOrSaveUser(socialType, authCode);
        authenticateUser(user);

        TokenPair tokens = generateTokens(user);
        saveRefreshToken(user, tokens.getRefreshToken());
        sendTokensToClient(response, tokens);

//        logTokens(tokens);
    }

    private User fetchOrSaveUser(SocialType socialType, String authCode) {
        User fetchedUser = oauthUserClientComposite.fetch(socialType, authCode);
        return userRepository.findBySocialTypeAndSocialId(socialType, fetchedUser.getSocialId())
                .orElseGet(() -> {
                    userRepository.save(fetchedUser);
                    return fetchedUser;
                });
    }

    private void authenticateUser(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    private TokenPair generateTokens(User user) {
        String accessToken = jwtService.createAccessToken(user.getEmail(), user.getSocialType(), user.getSocialId());
        String refreshToken = jwtService.createRefreshToken();
        return new TokenPair(accessToken, refreshToken);
    }

    private void saveRefreshToken(User user, String refreshToken) {
        jwtService.updateRefreshToken(user.getSocialId(), user.getSocialType(), refreshToken);
    }

    private void sendTokensToClient(HttpServletResponse response, TokenPair tokens) {
        jwtService.sendAccessAndRefreshToken(response, tokens.getAccessToken(), tokens.getRefreshToken());
    }

   /* private void logTokens(TokenPair tokens) {
        log.info("accessToken = {}", tokens.getAccessToken());
        log.info("refreshToken = {}", tokens.getRefreshToken());
    }*/

    private static class TokenPair {
        private final String accessToken;
        private final String refreshToken;

        public TokenPair(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
    }
}
