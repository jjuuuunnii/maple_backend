package com.maple.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@Builder
public class UserLoginDto {

    @Email
    private String email;

    @NotBlank
    private String password;

    public UserLoginDto(){}
}
