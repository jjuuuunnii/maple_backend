package com.maple.dto.letter.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LetterInfoResDto {
    private String senderName;
    private String letterContent;

    public LetterInfoResDto(){}
}
