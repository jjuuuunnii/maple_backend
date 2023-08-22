package com.maple.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@AllArgsConstructor
@Builder
public class UserLoginDto {


    private String email;


    private String password;

    public UserLoginDto(){}
}
