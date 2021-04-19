package com.backend.teamtalk.service;

import com.backend.teamtalk.domain.User;
import com.backend.teamtalk.dto.SignupRequestDto;
import com.backend.teamtalk.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  //error: configuration 에 bean 으로 등록 해야 한다.

    //create user
    public void createUser(SignupRequestDto requestDto) {
        userRepository.save(
                User.builder()
                        .username(requestDto.getUsername())
                        .password(passwordEncoder.encode(requestDto.getPassword())) //비밀번호 그냥 넣으면 안돼
                        .tech(requestDto.getTech())
                        .roles(Collections.singletonList("ROLE_USER"))
                        .build()
                        //1개의 엘리먼트가 들어있는 리스트를 사용할 때는 Arrays.asList 대신 Collections.singletonList 를 사용할 것
        );

    }


//    @Transactional(readOnly = true)
//    public Optional<User> getMyUserWithAuthorities() {      //권한 검증
//        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
//    }
}
