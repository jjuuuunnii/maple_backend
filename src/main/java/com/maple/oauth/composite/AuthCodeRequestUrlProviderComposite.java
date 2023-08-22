package com.maple.oauth.composite;

import com.maple.entity.SocialType;
import com.maple.oauth.provider.AuthCodeRequestUrlProvider;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

@Component
public class AuthCodeRequestUrlProviderComposite {

    private final Map<SocialType, AuthCodeRequestUrlProvider> mapping;

    public AuthCodeRequestUrlProviderComposite(Set<AuthCodeRequestUrlProvider> providers){
        mapping=providers.stream()
                .collect(toMap(
                        AuthCodeRequestUrlProvider::supportServer,
                        identity()
                ));
    }

    public String provide(SocialType socialType){
        return getProvider(socialType).provide();
    }

    public AuthCodeRequestUrlProvider getProvider(SocialType socialType){
        return Optional.ofNullable(mapping.get(socialType))
                .orElseThrow(() -> new RuntimeException("지원하지 않는 소셜 로그인 타입입니다"));
    }


}
