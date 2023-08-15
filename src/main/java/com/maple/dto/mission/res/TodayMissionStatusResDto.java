package com.maple.dto.mission.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TodayMissionStatusResDto {

    private boolean missionComplete;

    public TodayMissionStatusResDto(){}
}
