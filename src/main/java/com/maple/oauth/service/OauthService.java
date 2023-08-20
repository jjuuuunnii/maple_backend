package com.maple.oauth.service;

import com.maple.entity.SocialType;
import com.maple.entity.User;
import com.maple.jwt.service.JwtService;
import com.maple.oauth.composite.AuthCodeRequestUrlProviderComposite;
import com.maple.oauth.composite.OauthUserClientComposite;
import com.maple.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        User user = userRepository.findByEmailAndSocialType(socialType, authCode)
                .orElseGet(() -> {
                    User newUser = oauthUserClientComposite.fetch(socialType, authCode);
                    userRepository.save(newUser);
                    return newUser;
                });

        // 스프링 시큐리티 인증 처리
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        String accessToken = jwtService.createAccessToken(user.getEmail(), user.getSocialType());
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(user.getEmail(),user.getSocialType(),refreshToken);
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    }

}
