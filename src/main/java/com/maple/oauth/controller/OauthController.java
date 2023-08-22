package com.maple.oauth.controller;

import com.maple.entity.SocialType;
import com.maple.oauth.service.OauthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/oauth")
@RestController
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/{socialType}")
    public void redirectAuthCodeRequestUrl(
            @PathVariable SocialType socialType,
            HttpServletResponse response
    ) throws IOException {
        String redirectUrl = oauthService.getAuthCodeRequestUrl(socialType);
        response.sendRedirect(redirectUrl);
    }


    @GetMapping("/login/{oauthServerType}")
    public ResponseEntity<Void> login(
            @PathVariable SocialType oauthServerType,
            @RequestParam("code") String code,
            HttpServletResponse response
    ) {
        oauthService.loginWithSocial(oauthServerType, code, response);
        return ResponseEntity.ok().build();
    }


}
