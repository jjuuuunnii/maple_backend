package com.maple.oauth.client.naver;

import com.maple.oauth.dto.naver.NaverToken;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public interface NaverApiClient {

    @PostExchange(url = "https://nid.naver.com/oauth2.0/token")
    NaverToken fetchToken(@RequestParam(name = "params") MultiValueMap<String, String> params);

//    @GetExchange(url= "https://openapi.naver.com/v1/nid/me")
//    NaverUserResponse fetchUser(@RequestParam(name = AUTHORIZATION) String bearerToken);
}
