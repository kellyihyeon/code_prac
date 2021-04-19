package com.backend.teamtalk.domain;

import com.backend.teamtalk.dto.BoardRequestDto;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@ToString
@Getter
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
                    property = "id")   //무한순환 테스트 중 (, property = "id")
public class Board {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @ManyToOne //db column. 로그인 후 보게 될 user별 board  //(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<Pin> pins = new ArrayList<>(); //무슨 차이?


    //constructor
    //create board -> 인자에 추후 user 추가
    public Board(BoardRequestDto boardRequestDto, User user) {
        this.title = boardRequestDto.getTitle();
        this.user = user;

    }

    //update board
    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
    }



}
