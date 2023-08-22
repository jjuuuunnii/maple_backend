package com.maple.dto.mission.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class FirstMissionReqDto {
    private boolean menuButtonClicked;

    public FirstMissionReqDto(){}
}
