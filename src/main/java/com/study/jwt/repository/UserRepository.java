package com.study.jwt.repository;

import com.study.jwt.model.JwtUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<JwtUser, Integer> {
    JwtUser findByUsername(String username);
}
