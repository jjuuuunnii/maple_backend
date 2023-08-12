package com.maple.oauth2.userinfo;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo{

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(getResponseMap().get("id"));
    }

    @Override
    public String getName() {
        return String.valueOf(getResponseMap().get("name"));

    }

    @Override
    public String getEmail() {
        return String.valueOf(getResponseMap().get("email"));
    }

    private Map<String, Object> getResponseMap() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if(response == null){
            return null;
        }
        return response;
    }
}
