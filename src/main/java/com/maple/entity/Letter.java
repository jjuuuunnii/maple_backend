package com.maple.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name = "letter")
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "letter_id_seq")
    @SequenceGenerator(name = "letter_id_seq", sequenceName = "letter_id_seq")
    @Column(name="letter_Id")
    private Long id;

    private String senderName;
    private String content;
    private int createdAt;      // 유저 입장에서 유저가 받은 편지 날짜, 유저의 회원가입 기준으로 1,2,3,,,,
    private LocalDateTime localDateTime;  // 유저가 받은 편지의 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User user;

    public void setLetterUser(User user, Letter letter){
        this.user = user;
        user.getLetters().add(letter);  //양방향 연관관계매핑
    }

    public Letter(){}
}
