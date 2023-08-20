package com.maple.oauth.dto.naver;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.maple.entity.SocialType;
import com.maple.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NaverUserResponse {
    private String resultCode;
    private String message;
    private Response response;

    public NaverUserResponse(){}

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Response{
        private String id;
        private String nickname;
        private String name;
        private String email;
        private String gender;
        private String age;
        private String birthday;
        private String profileImage;
        private String birthyear;
        private String mobile;

        public Response(){}
    }

    public User toEntity(){
        return User.toEntity(response.name, response.email, UUID.randomUUID().toString(), SocialType.NAVER, response.id);
    }


}
