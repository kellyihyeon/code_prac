package com.backend.teamtalk.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider  {     //토큰을 생성하고 검증하는 컴포넌트 | 실제 이 컴포넌트를 이용하는 것은 인증 작업을 진행하는 Filter

    //secret key test -> F
//    private final ConfigUtil configUtil;
//    private String secretKey = configUtil.getProperty("key");

    private String secretKey = "weareteamtalk";     // 아... 이거 설정파일에서 안불러와지나...

    //set: 30 min
    private long tokenValidTime = 30 * 60 * 1000L;

    private final UserDetailsService userDetailsService;

//    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    //encoding
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    //create token | 로그인 요청한 유저가 유효한 녀석이면 ok 하면서 토큰 내려주기
    public String createToken(String username, List<String> roles) {
        //페이로드에 세팅 ( name, value)
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roles);
        log.info("claims = {}", claims);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)   //토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + tokenValidTime)) //30분 동안 유효
                .signWith(SignatureAlgorithm.HS256, secretKey)  //hs256 알고리즘으로 암호화, 서명 부분에 넣을 secret key 세팅
                .compact();
    }

    //header 에서 토큰 받고, 그 토큰이 유효한 토큰이면 토큰으로부터 유저 인증 정보 조회하기
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //jwt 토큰에서 회원 정보 추출 -> 왜 추출하는데
    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    //request 의 header 에서 token 값 가져오기.
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");//value 꺼내기 == 토큰


//        String bearerToken = request.getHeader(JwtAuthenticationFilter.AUTHORIZATION_HEADER);
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//         return null;

    }


    //토큰 유효성 테스트
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());    //뭔데 이게
        } catch (Exception e) {
            return false;
        }
    }

}

