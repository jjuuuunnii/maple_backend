package com.maple.repository.user;

import com.maple.entity.User;
import com.maple.exception.CustomException;
import com.maple.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Repository
public class UserRepository {

    private final EntityManager em;

    public Long save(User user){
        em.persist(user);
        return user.getId();
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    public void deleteById(Long id) {
        Optional<User> user = findById(id);
        user.ifPresentOrElse(
                em::remove,
                () -> {
                    throw new CustomException(ErrorCode.USER_NOT_FOUND);
                });
    }
}
