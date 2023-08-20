package com.maple.entity;

import static java.util.Locale.*;

public enum SocialType {

    KAKAO,
    NAVER,
    GOOGLE,
    DEFAULT,
    ;

    public static SocialType fromName(String type) {
        return SocialType.valueOf(type.toUpperCase(ENGLISH));
    }
}
