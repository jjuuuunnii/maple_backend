package com.maple.service.mission;
import com.maple.dto.mission.res.StampStatusListResDto;
import com.maple.dto.mission.res.TodayMissionStatusResDto;
import com.maple.entity.Mission;
import com.maple.entity.User;
import com.maple.exception.custom.CustomException;
import com.maple.exception.custom.ErrorCode;
import com.maple.repository.mission.MissionRepository;
import com.maple.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionService {

    private final UserRepository userRepository;
    private final MissionRepository missionRepository;


    @Transactional
    public void setMissionStatus(Boolean status, Long userId, int nowDate) {
        userRepository.findById(userId).ifPresentOrElse(
                user -> {
                    missionRepository.saveMissionStatusWithNowDateAndStatus(status, nowDate, userId);
                    user.setTodayMissionStatus(true);
                },
                () -> {
                    throw new CustomException(ErrorCode.USER_NOT_FOUND);
                }
        );
    }

    @Transactional(readOnly = true)
    public StampStatusListResDto getStampStatus(Long userId) {
        return userRepository.findById(userId).map(
                user -> {
                    List<Mission> missions = missionRepository.findMissionByUserId(userId);

                    missions.sort(Comparator.comparing(Mission::getId));
                    List<Boolean> missionStatuses = new ArrayList<>();
                    for (Mission mission : missions) {
                        log.info("mission = {}", mission.getId());
                        missionStatuses.add(mission.isMissionStatus());
                    }

                    return StampStatusListResDto.builder()
                            .stampsStatus(missionStatuses)
                            .build();
                }).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }



    @Transactional(readOnly = true)
    public TodayMissionStatusResDto getTodayMissionStatus(Long userId) {
        return userRepository.findById(userId)
                .map(user -> TodayMissionStatusResDto.builder()
                        .missionComplete(user.isTodayMissionStatus())
                        .build())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }


    public boolean checkWordInLetter(String content, int nowDate) {
        String[] wordsToCheck = {"사랑", "벌써", "가을", "화이팅", "안녕", "고생", "단풍", "편지", "요즘", "행복"};
        int[] datesToCheck = {2, 4, 7, 12, 14, 17, 19, 22, 24, 27};
        for (int i=0; i<datesToCheck.length; i++) {
            if (content.contains(wordsToCheck[i]) && nowDate == datesToCheck[i]) {
                return true;
            }
        }

        return false;
    }


    public boolean checkLetterCount(int letterCnt, int nowDate) {
        int[] datesToCheck = {3,5,8, 10, 13, 15, 18, 20, 23, 25, 28, 30};
        int[] letterCountToCheck = {5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10};
        for (int i =0; i<datesToCheck.length; i++) {
            if(letterCnt >= letterCountToCheck[i] && nowDate == datesToCheck[i]){
                return true;
            }
        }
        return false;
    }

    public boolean checkLetterTime(LocalDateTime localDateTime, int nowDate) {
        int[] timeToCheck = {9,10,21,22,23};
        int[] datesToCheck ={6, 11, 16, 21, 26};
        for (int i =0; i<datesToCheck.length; i++) {
            if(nowDate == datesToCheck[i] && (localDateTime.getHour()>=timeToCheck[i] && localDateTime.getHour()<=timeToCheck[i+1])){
                return true;
            }
        }
        return false;
    }


    @Transactional
    public boolean setTodayStamp(Long userId, boolean status) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        });
        missionRepository.saveMissionStatusWithNowDateAndStatus(status, user.getTimeFromSignup(), userId);
        return missionRepository.findMissionByUserIdAndNowDate(userId, user.getTimeFromSignup()).isMissionStatus();
    }
}
