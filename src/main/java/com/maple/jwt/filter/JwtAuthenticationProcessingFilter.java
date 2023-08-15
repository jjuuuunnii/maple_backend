package com.maple.jwt.filter;

import com.maple.entity.SocialType;
import com.maple.entity.User;
import com.maple.exception.custom.CustomException;
import com.maple.exception.custom.ErrorCode;
import com.maple.jwt.service.JwtService;
import com.maple.login.service.PrincipalDetails;
import com.maple.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;


@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL_LOGIN = "/api/auth/login/self";
    private static final String NO_CHECK_URL_SIGNUP = "/api/auth/signup/self";
    
    private final JwtService jwtService;

    private final UserRepository userRepository;

    public JwtAuthenticationProcessingFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getRequestURI().equals(NO_CHECK_URL_LOGIN) || request.getRequestURI().equals(NO_CHECK_URL_SIGNUP) ){
            filterChain.doFilter(request,response);
            return;
        }

        /**
         * refreshToken 유효성 검사
         */
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);
        log.info("refresh Token = {}", refreshToken);

        /**
         * refreshToken 유효한 상태 => refreshToken, accessToken, 둘다 발급해야하는 상태
         */
        if (refreshToken != null) {
              reIssueAccessTokenAndRefreshToken(refreshToken, response);
              return;
        }

        /**
         *  accessToken 유효성을 검사, 그 후 다음 필터로
         */
        if(refreshToken == null){
            checkAccessTokenAndAuthentication(request);
            filterChain.doFilter(request,response);
        }
    }


    private void reIssueAccessTokenAndRefreshToken(String refreshToken, HttpServletResponse response) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(
                        user -> {
                            String reIssuedAccessToken = jwtService.createAccessToken(user.getEmail(), user.getSocialType());
                            String reIssuedRefreshToken = jwtService.createRefreshToken();
                            jwtService.updateRefreshToken(user.getEmail(), user.getSocialType(), reIssuedRefreshToken);
                            jwtService.sendAccessAndRefreshToken(response, reIssuedAccessToken, reIssuedRefreshToken);
                        }

                );

    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request) {
        jwtService.extractAccessToken(request)
                .ifPresent(accessToken -> {
                            String email = jwtService.extractEmail(accessToken).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
                            SocialType socialType = jwtService.extractSocialType(accessToken).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
                            userRepository.findByEmailAndSocialType(socialType, email)
                                    .ifPresent(
                                            this::saveAuthentication
                                    );
                            }
                );
    }

    private void saveAuthentication(User user) {
        PrincipalDetails principalDetails = new PrincipalDetails(user);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(principalDetails, null, Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
