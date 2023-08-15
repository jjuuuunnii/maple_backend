package com.maple.dto.user.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserInfoResDto {
    private String userId;
    private String userName;

    public UserInfoResDto(){}
}
