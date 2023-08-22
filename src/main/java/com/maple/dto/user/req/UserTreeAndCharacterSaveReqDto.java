package com.maple.dto.user.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserTreeAndCharacterSaveReqDto {
    private String treeType;
    private String characterType;

    public UserTreeAndCharacterSaveReqDto(){}
}
