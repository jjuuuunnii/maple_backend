package com.maple.oauth.dto.google;

import com.maple.entity.SocialType;
import com.maple.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GoogleUserResponse {

    private String id;
    private String email;
    private String verified_email;
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String locale;

    public GoogleUserResponse(){}

    public User toUser(){
        return User.toEntity(name, email, UUID.randomUUID().toString(), SocialType.GOOGLE, id.toString());
    }
}
