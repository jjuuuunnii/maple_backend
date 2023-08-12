package com.maple.entity;

import com.maple.oauth2.userinfo.OAuth2UserInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private SocialType socialType;  //일반 유저의 경우 null
    private String socialId;        //일반 유저의 경우 null
    private String refreshToken;
    private String tree;
    private String character;
    private int timeFromSignup;     //회원가입 한날부터 1일

    public User(){}

    public void setDefaultTreeAndCharacter(){
        this.tree = "Maple tree";
        this.character = "Maple Character";
    }
    public void updateTreeAndCharacter(String tree, String character){
        this.tree = tree;
        this.character = character;
    }

    public void updatePassword(){
        this.password = UUID.randomUUID().toString();

    }

    public static User toEntity(String name, String email, String password){
        User user = User.builder()
                .name(name)
                .email(email)
                .password(password)
                .build();
        user.setDefaultTreeAndCharacter();
        user.timeFromSignup = 1;
        return user;
    }
}

