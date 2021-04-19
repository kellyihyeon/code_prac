package com.backend.teamtalk.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 검증이 끝난 jwt 로부터 유저 정보를 받아와서 UsernamePasswordAuthenticationFilter 로 전달해야 한다.
 * 우리가 만든 이 필터를 WebSecurityConfig 클래스에 등록하자.
 */


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

//    public static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //header 에서 jwt 받아오기
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
        //이 토큰이 유효한지?
        if (token != null && jwtTokenProvider.validateToken(token)) {   //그렇다면
            //인증 정보 받아와서
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            //SecurityContext에 넣자
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}
