package com.backend.teamtalk.domain;

import com.backend.teamtalk.dto.CardRequestDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Card {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Column(nullable = true)
    private String description;

    @Column(nullable = true)
    private String comment;

    @ManyToOne
    @JsonBackReference //순환참조 막기
    private Pin pin;


    //create card
    public Card(CardRequestDto requestDto, Pin pin) {
        this.title = requestDto.getTitle();
        //comment, description은 상세조회 페이지에서 작성
        this.pin = pin;
    }


    //update card
    public void update(CardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.description = requestDto.getDescription();
    }
}
