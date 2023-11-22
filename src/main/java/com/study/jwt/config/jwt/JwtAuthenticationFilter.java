package com.study.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.jwt.auth.PrincipalDetails;
import com.study.jwt.model.JwtUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

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

        // 1. username, password 받아서
        try {
            System.out.println(request.getInputStream().toString());

            ObjectMapper mapper = new ObjectMapper();
            JwtUser user = mapper.readValue(request.getInputStream(), JwtUser.class);
            System.out.println(user);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            // PrincipalDetailsService.loadByUsername 메서드 실행됨
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // authentication 객체가 세션의 시큐리티세션영역에 저장됨 => 로그인이 되었다는 뜻
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            System.out.println(principalDetails.getUser().getUsername());

            // JWT를 사용하면 세션을 사용할 이유는 없지만
            // authentication 객체를 저장하는 이유는 권한 관리를 security가 대신 해주기 때문이다.

            return authentication;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    // attemptAuthentication 실행후 인증이 정상적으로 되었을 경우 successfulAuthentication() 실행됨
    // JWT를 만들어서 request 사용자에게 JWT를 response 해주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String token = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRES))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SIGN));

        response.addHeader("Authorization", JwtProperties.PREFIX + token);
    }
}
