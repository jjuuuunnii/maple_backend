package com.maple.dto.letter.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LetterListResDto {
    private String treeType;
    private int nowDate;
    private List<Boolean> lettersOverFive;

    public LetterListResDto(){}
}
