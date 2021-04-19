package com.backend.teamtalk.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //authenticationManager 도 Bean 에 등록하자
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()   //jwt 토큰 사용하니까 csrf 보안 토큰은 disable
                .httpBasic().disable()  //bearer 방식을 사용할 거니까 disable.
                .formLogin().disable()  //안써
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //jwt 인증이므로 세션은 사용하지 않는다.
                .and()
                .authorizeRequests()    //요청에 대한 사용 권한
//                .antMatchers("/admin").authenticated()  //test용
//                .antMatchers("/user").authenticated()
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
                //우리가 만든 필터를 UsernamePasswordAuthenticationFilter 전에 넣자.
    }

}
