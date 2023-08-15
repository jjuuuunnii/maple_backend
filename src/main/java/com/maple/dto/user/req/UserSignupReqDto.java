package com.maple.dto.user.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserSignupReqDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    public UserSignupReqDto(){}

}
