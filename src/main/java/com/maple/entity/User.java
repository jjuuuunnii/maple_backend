package com.maple.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq")
    @Column(name = "user_id")
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Letter> letters = new ArrayList<>();

    private String name;
    private String email;
    private String password;        //소셜로그인의 경우 랜덤으로 부여

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;

    private String socialId;
    private String refreshToken;
    private String tree;
    private String character;
    private int timeFromSignup;     //회원가입 한날부터 1일

    public User(){}
}

