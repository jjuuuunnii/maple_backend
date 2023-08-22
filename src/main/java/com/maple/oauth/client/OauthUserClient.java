package com.maple.oauth.client;

import com.maple.entity.SocialType;
import com.maple.entity.User;

public interface OauthUserClient {

    SocialType supportServer();

    User fetch(String code);
}
