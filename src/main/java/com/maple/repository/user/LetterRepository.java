package com.maple.repository.user;

import com.maple.entity.Letter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LetterRepository {

    private final EntityManager em;

    public Long save(Letter letter){
        em.persist(letter);
        return letter.getId();
    }

    public Optional<List<Letter>> findByCreatedAt(int createdAt) {
        List<Letter> letters = em.createQuery("select l from Letter l where l.createdAt = :createdAt", Letter.class)
                .setParameter("createdAt", createdAt)
                .getResultList();

        return Optional.ofNullable(letters);
    }


}
