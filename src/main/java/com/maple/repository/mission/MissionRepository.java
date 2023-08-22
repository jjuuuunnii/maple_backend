package com.maple.repository.mission;

import com.maple.entity.Mission;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
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

    public List<Mission> findMissionByUserId(Long userId){
        return em.createQuery("select m from Mission m where m.user.id =: userId",Mission.class)
                .setParameter("userId", userId)
                .getResultList();
    }


    public Mission findMissionByUserIdAndNowDate(Long userId, int nowDate) {
        return em.createQuery("select m from Mission m where m.user.id = :userId and m.nowDate = :nowDate", Mission.class)
                .setParameter("userId", userId)
                .setParameter("nowDate", nowDate)
                .getSingleResult();

    }
}
