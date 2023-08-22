package com.maple.dto.user.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserSignupReqDto {


    private String email;

    private String userName;

    private String password;

    public UserSignupReqDto(){}

}
