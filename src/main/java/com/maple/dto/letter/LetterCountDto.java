package com.maple.dto.letter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LetterCountDto {

    private int createdAt;
    private Long count;

    public LetterCountDto(){}
}
