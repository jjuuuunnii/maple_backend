package com.maple.login.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.dto.user.UserLoginDto;
import com.maple.exception.CustomException;
import com.maple.exception.ErrorCode;
import com.maple.jwt.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomJsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private static final String DEFAULT_LOGIN_PATH = "/api/auth/login/self";
    private static final String CONTENT_TYPE = "application/json";
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    public CustomJsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper, JwtService jwtService, AuthenticationManager authenticationManager) {
        super(DEFAULT_LOGIN_PATH);
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        try {
            log.info("=========================로그인 시도============================");
            UserLoginDto userLoginDto = objectMapper.readValue(request.getInputStream(), UserLoginDto.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword())
            );
        } catch (IOException e) {
            log.info("=========================로그인 실패============================");
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        String email = userDetails.getUsername();
        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(email, refreshToken);
        log.info("{} 유저 로그인 성공", userDetails.getUsername());

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.info("=========================로그인 실패============================");
        throw new CustomException(ErrorCode.LOGIN_FAILED);
    }
}
