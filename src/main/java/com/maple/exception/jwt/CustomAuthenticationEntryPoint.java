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
        log.info("authException = {}", authException.getClass().toString());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", authException.getMessage());
        response.getWriter().write(jsonObject.toString());
    }
}
