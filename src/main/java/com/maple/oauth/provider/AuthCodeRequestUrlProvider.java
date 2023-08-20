package com.maple.oauth.provider;

import com.maple.entity.SocialType;

public interface AuthCodeRequestUrlProvider {

    SocialType supportServer();

    String provide();
}
