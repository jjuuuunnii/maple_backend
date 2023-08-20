package com.maple.login.handler;
import com.maple.entity.SocialType;
import com.maple.entity.User;
import com.maple.jwt.service.JwtService;
import com.maple.login.service.PrincipalDetails;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        User user = principalDetails.getUser();
        String email = user.getEmail();
        SocialType socialType = user.getSocialType();

        String accessToken = jwtService.createAccessToken(email, socialType);
        String refreshToken = jwtService.createRefreshToken();
        jwtService.updateRefreshToken(email, socialType, refreshToken);
        log.info("{} 유저 로그인 성공", user.getEmail());

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    }


}
