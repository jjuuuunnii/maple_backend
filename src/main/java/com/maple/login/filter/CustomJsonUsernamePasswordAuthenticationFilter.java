package com.maple.login.filter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.dto.user.UserLoginDto;
import com.maple.exception.custom.ErrorCode;
import com.maple.jwt.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.io.IOException;

@Slf4j
public class CustomJsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {


    private static final String DEFAULT_LOGIN_PATH = "/api/auth/login/self";
    private static final String OAUTH_LOGIN_PATH = "/api/auth/login/oauth";
    private static final String CONTENT_TYPE = "application/json";
    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    public CustomJsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper, JwtService jwtService, AuthenticationManager authenticationManager) {
        super(new OrRequestMatcher(
                new AntPathRequestMatcher(DEFAULT_LOGIN_PATH),
                new AntPathRequestMatcher(OAUTH_LOGIN_PATH)
        ));
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
            throw new AuthenticationException(ErrorCode.INVALID_REQUEST.getCode()){};
        }
        try {
            log.info("=========================로그인 시도============================");
            UserLoginDto userLoginDto = objectMapper.readValue(request.getInputStream(), UserLoginDto.class);
            if(request.getRequestURI() == DEFAULT_LOGIN_PATH){
                return authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword())
                );
            }
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), null)
            );

        } catch (IOException e) {
            log.info("=========================로그인 실패============================");
            throw new AuthenticationException(ErrorCode.LOGIN_FAILED.getCode()){};
        }
    }
}
