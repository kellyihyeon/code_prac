package com.backend.teamtalk.domain;

import com.backend.teamtalk.dto.SignupRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@ToString
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

    @Builder.Default    //default 는 user 로 설정 (admin 필요 없음)
    @ElementCollection  //Entity 가 아닌 단순한 형태의 객체 집합을 관리할 것이다.
    private List<String> roles = new ArrayList<>();

    @Column(nullable = true)
    private String tech;    //어떻게 지정할지 미정


    public User(SignupRequestDto signupRequestDto) {
        this.username = signupRequestDto.getUsername();
        this.password = signupRequestDto.getPassword();
        this.tech = signupRequestDto.getTech();
    }



}
