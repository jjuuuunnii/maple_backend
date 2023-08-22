package com.maple.oauth.composite;

import com.maple.entity.SocialType;
import com.maple.entity.User;
import com.maple.oauth.client.OauthUserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;


@Component
@Slf4j
public class OauthUserClientComposite{

    private final Map<SocialType, OauthUserClient> mapping;

    public OauthUserClientComposite(Set<OauthUserClient> clients) {
        mapping = clients.stream()
                .collect(toMap(
                        OauthUserClient::supportServer,
                        identity()
                ));
    }

    public User fetch(SocialType socialType, String authCode){
        return getClient(socialType).fetch(authCode);
    }

    private OauthUserClient getClient(SocialType socialType){
        log.info("socialType = {}", socialType.toString());
        return Optional.ofNullable(mapping.get(socialType))
                .orElseThrow(() -> new RuntimeException("지원하지 않는 소셜 타입입니다"));
    }

}
