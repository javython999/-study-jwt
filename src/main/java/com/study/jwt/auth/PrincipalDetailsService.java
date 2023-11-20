package com.study.jwt.auth;

import com.study.jwt.model.JwtUser;
import com.study.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // /login으로 요청이 오면 동작 (스프링 시큐리티 기본 login url)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(":!!!");
        JwtUser user = userRepository.findByUsername(username);
        return new PrincipalDetails(user);
    }
}
