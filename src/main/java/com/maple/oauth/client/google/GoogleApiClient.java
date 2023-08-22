package com.maple.oauth.client.google;

import com.maple.oauth.dto.google.GoogleToken;
import com.maple.oauth.dto.google.GoogleUserResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

@Component
public interface GoogleApiClient {

    @PostExchange(url = "https://oauth2.googleapis.com/token", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    GoogleToken fetchToken(@RequestParam(name = "params") MultiValueMap<String, String> params);

    @GetExchange(url = "https://www.googleapis.com/oauth2/v2/userinfo")
    GoogleUserResponse fetchUser(@RequestHeader(name = AUTHORIZATION) String bearerToken);


}
