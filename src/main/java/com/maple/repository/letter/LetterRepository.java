package com.maple.repository.letter;
import com.maple.dto.letter.LetterCountDto;
import com.maple.entity.Letter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class LetterRepository {

    private final EntityManager em;
    @Transactional
    public Long save(Letter letter){
        em.persist(letter);
        return letter.getId();
    }

    @Transactional(readOnly = true)
    public Optional<List<Letter>> findByCreatedAt(int createdAt) {
        List<Letter> letters = em.createQuery("select l from Letter l where l.createdAt = :createdAt", Letter.class)
                .setParameter("createdAt", createdAt)
                .getResultList();

        return Optional.ofNullable(letters);
    }


    @Transactional(readOnly = true)
    public Optional<List<LetterCountDto>> countAllLettersByDateUntilNowDate(Long userId) {
        List<Object[]> resultList = em.createQuery("select l.createdAt, count(l) from Letter l where l.user.id =: userId and l.createdAt <= :nowDate group by l.createdAt")
                .setParameter("userId", userId)
                .setParameter("nowDate", 30)
                .getResultList();

        List<LetterCountDto> dtoList = resultList.stream()
                .map(objArr -> new LetterCountDto(((Number) objArr[0]).intValue(), (Long) objArr[1]))
                .collect(Collectors.toList());

        return Optional.ofNullable(dtoList);
    }





    public List<Letter> findByUserIdAndSelectedDate(Long userId, int selectedDate) {
        return em.createQuery("select l from Letter l where l.user.id =: userId And l.createdAt =: selectedDate")
                .setParameter("userId", userId)
                .setParameter("selectedDate", selectedDate)
                .getResultList();
    }
}
