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
@RequestMapping("/api/users/{userId}/missions")
public class MissionController {

    private final MissionService missionService;

    @PutMapping("/{nowDate}")
    public void firstMission(@RequestBody FirstMissionReqDto firstMissionReqDto,
                             @PathVariable Long userId,
                             @PathVariable int nowDate) {
        missionService.setMissionStatus(firstMissionReqDto.isMenuButtonClicked(), userId, nowDate);
    }

    @GetMapping
    public StampStatusListResDto getStampStatus(@PathVariable Long userId) {
        return missionService.getStampStatus(userId);
    }

    @GetMapping("/todayMission")
    public TodayMissionStatusResDto getTodayMissionStatus(@PathVariable Long userId) {
        return missionService.getTodayMissionStatus(userId);
    }

    @PutMapping("/todayMission")
    public boolean setTodayStampToUser(@PathVariable Long userId, @RequestBody MissionCompleteButtonClickReqDto missionCompleteButtonClickReqDto){
        boolean b = missionService.setTodayStamp(userId, missionCompleteButtonClickReqDto.isMissionCompleteButtonClick());
        return b;
    }
}
