package com.maple.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ConsolationLetter {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consolationLetter_id_seq")
    @SequenceGenerator(name = "consolationLetter_id_seq", sequenceName = "consolationLetter_id_seq")
    @Column(name="consolationLetter_id")
    private Long id;

    private String senderName;
    private String content;

    public ConsolationLetter(){}
}
