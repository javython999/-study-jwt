package com.study.jwt.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.study.jwt.auth.PrincipalDetails;
import com.study.jwt.model.JwtUser;
import com.study.jwt.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 시큐리티가 가지고 있는 필터중 BasicAuthenticationFilter가 있다
 * 권한이나 인증이 필요한 특정  URL을 요청시 BasicAuthenticationFilter를 거치게 되어있다.
 * 권한이나 인증이 필요한 URL이 아니면 BasicAuthenticationFilter를 안 거친다.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final UserRepository userRepository;
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이 필요한 요청");


        // header에 jwt 존재 확인
        String jwtHeader = request.getHeader("Authorization");
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }


        // JWT 검증 -> 정상적인 사용자인지 판단
        String token = jwtHeader.replace("Bearer ", "");

        String username = JWT.require(Algorithm.HMAC512("JWT_TOKEN"))
                .build()
                .verify(token)
                .getClaim("username")
                .asString();

        // 서명이 정상적으로 됨
        if(username != null) {
            JwtUser user = userRepository.findByUsername(username);

            PrincipalDetails principalDetails = new PrincipalDetails(user);

            // JWT 서명이 정상이면 Authentication 객체를 만들어 준다.
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());


            // 강제로 시큐리티 세션에 접근해 Authentication 객체를 저장.
            SecurityContextHolder.getContext().setAuthentication(authentication);

            chain.doFilter(request, response);
        }
    }
}

