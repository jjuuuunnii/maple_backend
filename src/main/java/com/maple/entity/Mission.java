package com.maple.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;



@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@Table(name = "mission")
public class Mission {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mission_id_seq")
    @SequenceGenerator(name = "mission_id_seq", sequenceName = "mission_id_seq")
    @Column(name = "mission_id")
    private Long id;

    private boolean missionStatus;
    private int nowDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User user;

    public Mission(){}
}
