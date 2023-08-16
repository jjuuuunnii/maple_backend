package com.maple.dto.mission.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class StampStatusListResDto {
    private List<StampStatus> stampsStatus;

    public StampStatusListResDto(){}
}
