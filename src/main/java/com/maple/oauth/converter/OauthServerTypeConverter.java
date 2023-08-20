package com.maple.oauth.converter;

import com.maple.entity.SocialType;
import org.springframework.core.convert.converter.Converter;

public class OauthServerTypeConverter implements Converter<String, SocialType> {

    @Override
    public SocialType convert(String source) {
        return SocialType.fromName(source);
    }
}
