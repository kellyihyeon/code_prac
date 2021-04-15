package com.backend.teamtalk.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
public class SignupRequestDto {

    private String username;
    private String password;
}
