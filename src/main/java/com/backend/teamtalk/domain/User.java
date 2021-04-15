package com.backend.teamtalk.domain;

import com.backend.teamtalk.dto.SignupRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@ToString
@Setter //테스트용
@NoArgsConstructor
@Getter
@Entity
public class User {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    @Column(nullable = true)
    private List<Board> boards = new ArrayList<>();

    @Column(nullable = true)
    private String imgUrl;


    public User(SignupRequestDto signupRequestDto) {
        this.username = signupRequestDto.getUsername();
        this.password = signupRequestDto.getPassword();
    }



}
