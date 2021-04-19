package com.backend.teamtalk.config;

import com.backend.teamtalk.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * spring security 는 UserDetails 객체를 통해 권한 정보를 관리한다.
 * User 클래스에서 바로 UserDetails 를 구현해도 되지만 그건 싫고
 * Entity 와 UserDetails 를 분리해서 관리할 것이다.
 */


public class CustomUserDetails implements UserDetails {

    private final User user;

    // 객체 생성용
    public CustomUserDetails(User user) {
        this.user = user;
    }

    //로그인 한 user 의 id 를 가져오기 위해
    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());      //stream, 람다 공부
        return simpleGrantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * spring security 는 getUsername 메서드를 통해서 사용할 username 을 가져간다.
     * username 으로 userId가 아닌 username 으로 사용할 것이기 때문에 그대로 세팅
     * @return
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
