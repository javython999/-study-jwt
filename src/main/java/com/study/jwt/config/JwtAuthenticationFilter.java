package com.study.jwt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter가 있음
// /login으로 username, password를 POST 요청하면 동작함
// 현재 .formLogin().disable() 설정으로 인해 UsernamePasswordAuthenticationFilter가 작동안함
// 그래서 다시 SecurityConfig에 등록해주어야함
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;


    // /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // username, password 받아서 authenticationManager로 로그인 시도
        // authenticationManager로 로그인 시도시 PrincipalDetailsService의 loadUserByUsername 메서드 실행
        // PrincipalDetails를 세션에 담는다. (권한 관리를 위해서)
        // JWT 생성후 응답해주면 된다.

        return super.attemptAuthentication(request, response);
    }
}
