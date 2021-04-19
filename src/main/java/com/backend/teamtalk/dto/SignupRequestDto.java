package com.backend.teamtalk.dto;

import lombok.Getter;


@Getter
public class SignupRequestDto {

    private String username;
    private String password;
    private String passwordCheck;   //형원님과 변수명 맞추기
    private String tech;
}
