package com.maple.repository.user;

import com.maple.entity.SocialType;
import com.maple.entity.User;
import com.maple.exception.custom.CustomException;
import com.maple.exception.custom.ErrorCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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

    public List<User> findAll(){
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    public Optional<User> findByEmail(String email){
        try {
            User user = (User) em.createQuery("select u from User u where u.email = :email")
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findByRefreshToken(String refreshToken){
        try {
            User user = (User) em.createQuery("select u from User u where u.refreshToken = :refreshToken")
                    .setParameter("refreshToken", refreshToken)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

/*
    public List<User> findAllWithPaging(int pageNumber, int pageSize) {
        return em.createQuery("select u from User u", User.class)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }
*/

    public List<User> findAllWithPaging(int pageNumber, int pageSize, LocalDateTime beforeTime) {
        return em.createQuery("select u from User u where (u.createdAt is null or u.createdAt < :beforeTime)", User.class)
                .setParameter("beforeTime", beforeTime)
                .setFirstResult(pageNumber * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }



    public Optional<User> findBySocialId(String socialId) {
        try {
            User user = (User) em.createQuery("select u from User u where u.socialId = :socialId")
                    .setParameter("socialId", socialId)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
    public Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId) {
        try {
            User user = (User) em.createQuery("select u from User u where u.socialType = :socialType and u.socialId = :socialId")
                    .setParameter("socialType", socialType)
                    .setParameter("socialId", socialId)
                    .getSingleResult();
            return Optional.ofNullable(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<User> findDefaultUserByEmail(String email) {
        try{
            User user = (User) em.createQuery("select u from User u where u.email =: email and u.socialType =: socialType")
                    .setParameter("socialType", SocialType.DEFAULT)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.ofNullable(user);
        }catch(NoResultException e){
            return Optional.empty();
        }
    }


}
