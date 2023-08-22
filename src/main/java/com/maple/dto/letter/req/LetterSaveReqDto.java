package com.maple.dto.letter.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Builder
@AllArgsConstructor
public class LetterSaveReqDto {

    private String senderName;
    private String letterContent;

    public LetterSaveReqDto(){}
}
