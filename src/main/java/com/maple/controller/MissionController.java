package com.maple.controller;


import com.maple.dto.mission.req.FirstMissionReqDto;
import com.maple.dto.mission.req.MissionCompleteButtonClickReqDto;
import com.maple.dto.mission.res.StampStatusListResDto;
import com.maple.dto.mission.res.TodayMissionStatusResDto;
import com.maple.service.mission.MissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users/{socialId}/missions")
public class MissionController {

    private final MissionService missionService;

    @PutMapping("/{nowDate}")
    public void firstMission(@RequestBody FirstMissionReqDto firstMissionReqDto,
                             @PathVariable String socialId,
                             @PathVariable int nowDate) {
        missionService.setMissionStatus(firstMissionReqDto.isMenuButtonClicked(), socialId, nowDate);
    }

    @GetMapping
    public StampStatusListResDto getStampStatus(@PathVariable String socialId) {
        return missionService.getStampStatus(socialId);
    }

    @GetMapping("/todayMission")
    public TodayMissionStatusResDto getTodayMissionStatus(@PathVariable String socialId) {
        return missionService.getTodayMissionStatus(socialId);
    }

    @PutMapping("/todayMission")
    public boolean setTodayStampToUser(@PathVariable String socialId, @RequestBody MissionCompleteButtonClickReqDto missionCompleteButtonClickReqDto){
        return missionService.setTodayStamp(socialId, missionCompleteButtonClickReqDto.isMissionCompleteButtonClick());
    }
}
