package com.maple.oauth2.handler;

import com.maple.entity.User;
import com.maple.exception.CustomException;
import com.maple.exception.ErrorCode;
import com.maple.jwt.service.JwtService;
import com.maple.login.service.PrincipalDetails;
import com.maple.oauth2.OAuthAttributes;
import com.maple.oauth2.userinfo.CustomOAuth2User;
import com.maple.repository.user.UserRepository;
import com.maple.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User auth2User = (CustomOAuth2User) authentication.getPrincipal();
        String accessToken = jwtService.createAccessToken(auth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(auth2User.getEmail(), refreshToken);
    }



}
