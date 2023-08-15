package com.maple.dto.user.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class OwnerHomeResDto {
    private String treeType;
    private String characterType;
    private String userName;
    private int nowDate;
    private boolean lettersOverFive;

}
