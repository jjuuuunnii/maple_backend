package com.maple.repository.consolationLetter;

import com.maple.entity.ConsolationLetter;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



@Repository
@RequiredArgsConstructor
@Slf4j
public class ConsolationLetterRepository {

    private final EntityManager em;

    public void save(ConsolationLetter letter) {
        em.persist(letter);
    }


    public ConsolationLetter findByNowDate(int nowDate) {
        return (ConsolationLetter) em.createQuery("select c from ConsolationLetter c where c.id =: nowDate")
                .setParameter("nowDate", Long.valueOf(nowDate))
                .getSingleResult();
    }
}
