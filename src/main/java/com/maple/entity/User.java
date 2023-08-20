package com.maple.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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


    /**
     * TODO 서버가 닫힐때, 30일이 이하로 남아있을때의 구현이 필요
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq")
    @Column(name = "user_id")
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Letter> letters = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Mission> missions = new ArrayList<>();

    private String name;
    private String email;
    private String password;        //소셜로그인의 경우 랜덤으로 부여

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType;  //일반 유저의 경우 null
    private String refreshToken;
    private String tree;
    private String character;
    private boolean todayMissionStatus;
    private int timeFromSignup;     //회원가입 한날부터 1일

    @JsonIgnore
    private static final int LAST_DAY =30;

   /* @JsonIgnore
    private static final int START_DAY =30;*/           // TODO 구현 필요

    public User(){}

    public void setDefaultTreeAndCharacter(){
        this.tree = "Maple Tree";
        this.character = "Maple Character";
    }


    public void updateTreeAndCharacter(String tree, String character){
        this.tree = tree;
        this.character = character;
    }

    public void updatePassword(){
        this.password = UUID.randomUUID().toString();
    }

    public boolean isLettersOverFive(){
        return (letters.size()>=5);
    }

    public int letterCnt(){
        return letters.size();
    }

    public static User toEntity(String name, String email, String password, SocialType socialType){
        User user = User.builder()
                .name(name)
                .email(email)
                .password(password)
                .missions(new ArrayList<>())
                .socialType(socialType)
                .build();

        for (int nowDate = 1; nowDate <= LAST_DAY; nowDate++) {
            Mission mission = Mission.builder()
                    .missionStatus(false)
                    .nowDate(nowDate)
                    .build();
            user.getMissions().add(mission);
            mission.setUser(user);
        }
        user.setDefaultTreeAndCharacter();
        user.timeFromSignup = 1;
        return user;
    }

}

