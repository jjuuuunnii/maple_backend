package com.maple.oauth2.handler;

import com.maple.jwt.service.JwtService;
import com.maple.oauth2.userinfo.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;

    /**
     * TODO
     *
     * OAuth2 user accessToken refreshToken 확인하기 필요
     *
     * @param request the request which caused the successful authentication
     * @param response the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     * the authentication process.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User auth2User = (CustomOAuth2User) authentication.getPrincipal();
        String accessToken = jwtService.createAccessToken(auth2User.getEmail(), auth2User.getSocialType());
        String refreshToken = jwtService.createRefreshToken();
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
        jwtService.updateRefreshToken(auth2User.getEmail(), auth2User.getSocialType() ,refreshToken);

        log.info("accessToken = {} ", "Bearer " + accessToken);
    }



}
