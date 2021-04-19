package com.backend.teamtalk.controller;

import com.backend.teamtalk.config.JwtTokenProvider;
import com.backend.teamtalk.domain.User;
import com.backend.teamtalk.dto.SignupRequestDto;
import com.backend.teamtalk.repository.UserRepository;
import com.backend.teamtalk.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    /**
     * username check: 중복체크 버튼 클릭시 / 회원가입 버튼 클릭시 둘 다 적용하는 것이 좋다.
     *
     * @param requestDto
     * @return
     */
    //create user
    @PostMapping("/api/signup")
    public ResponseEntity<String> createUser(@RequestBody SignupRequestDto requestDto) {
        //username duplication check
        Optional<User> existedUser = userRepository.findByUsername(requestDto.getUsername());
        if (existedUser.isPresent()) {
            log.info("status 409, username is existed");
            return new ResponseEntity<>("conflict", HttpStatus.CONFLICT);   //409로 처리 해놨음
        }

        //password matching check
        if (!requestDto.getPassword().equals(requestDto.getPasswordCheck())) {
            log.info("status 400, password is not match");
            return new ResponseEntity<>("bad request", HttpStatus.BAD_REQUEST);
        }

        userService.createUser(requestDto);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }


    // login
    @PostMapping("/api/login")
    public Map<String, String> login(@RequestBody Map<String, String> user) {    //username, password
        //username 대조
        User member = userRepository.findByUsername(user.get("username"))
                .orElseThrow(() -> new IllegalArgumentException("There is nobody by that name."));

        //password 대조
        if (!passwordEncoder.matches((user.get("password")), member.getPassword())) {
            //로그인 시 입력한 비밀번호와 db에 저장된 비밀번호가 일치하는지?
            throw new IllegalArgumentException("The password is incorrect.");
        }

        String jwt = jwtTokenProvider.createToken(member.getUsername(), member.getRoles());

        Map<String, String> result = new LinkedHashMap<>();
        result.put("token", jwt);
        return result;

//        return jwtTokenProvider.createToken(member.getUsername(), member.getRoles());

        //header 에 token 넣기
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(JwtAuthenticationFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        //body 에 넣기
//        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);

    }


    //회원가입 임시 테스트용으로 만들어 봄
    @GetMapping("/api/users")
    public List<User> allUsers() {
        List<User> allUsers = userRepository.findAll();
        return allUsers;
    }

    //delete user
    @DeleteMapping("api/users/{user_id}")
    public String deleteUser(@PathVariable Long user_id) {
        //user 찾아와야 하는데 귀찮으니까 일단 삭제
        userRepository.deleteById(user_id);
        return "delete user: success.";
    }

//    @GetMapping("/user")
//    @PreAuthorize("hasAnyRole('USER','ADMIN')")
//    public ResponseEntity<User> getMyUserInfo() {
//        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
//    }
}
