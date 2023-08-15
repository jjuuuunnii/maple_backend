package com.maple.repository.mission;

import com.maple.entity.Mission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MissionRepository {

    private final EntityManager em;


    public void saveMissionStatusWithNowDateAndStatus(Boolean status, int nowDate, Long userId) {
        Mission mission = (Mission) em.createQuery("select m from Mission m where m.nowDate =: nowDate And m.user.id =: userId")
                .setParameter("nowDate", nowDate)
                .setParameter("userId", userId)
                .getSingleResult();
        mission.setMissionStatus(status);
    }

    public List<Boolean> findMissionStatusListByUserId(Long userId) {
        return em.createQuery("select m.missionStatus from Mission m where m.user.id = :userId", Boolean.class)
                .setParameter("userId", userId)
                .getResultList();
    }

}
