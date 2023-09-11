package com.maple.repository.letter;
import com.maple.dto.letter.LetterCountDto;
import com.maple.entity.Letter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LetterRepository {

    private final EntityManager em;
    @Transactional
    public Long save(Letter letter){
        em.persist(letter);
        return letter.getId();
    }



    public Optional<List<LetterCountDto>> countAllLettersByDateUntilNowDate(Long userId) {
        TypedQuery<Object[]> query = em.createQuery("select l.createdAt, count(l) from Letter l where l.user.id =: userId and l.createdAt <= :nowDate group by l.createdAt", Object[].class);
        query.setParameter("userId", userId);
        query.setParameter("nowDate", 30);

        List<Object[]> resultList = query.getResultList();

        List<LetterCountDto> dtoList = resultList.stream()
                .map(objArr -> new LetterCountDto(((Number) objArr[0]).intValue(), (Long) objArr[1]))
                .collect(Collectors.toList());

        return Optional.ofNullable(dtoList);
    }



    public List<Letter> findByUserIdAndSelectedDate(Long userId, int selectedDate) {
        TypedQuery<Letter> query = em.createQuery("select l from Letter l where l.user.id =: userId And l.createdAt =: selectedDate", Letter.class);
        query.setParameter("userId", userId);
        query.setParameter("selectedDate", selectedDate);
        return query.getResultList();
    }
}
