package com.maple.exception.jwt;

import com.maple.exception.custom.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.info("request.getRequestURI() = {} ", request.getRequestURI());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", ErrorCode.INVALID_TOKEN.getCode());
        response.getWriter().write(jsonObject.toString());
        log.info("인증되지 않은 토큰 입니다");
    }
}
