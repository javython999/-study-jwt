package com.study.jwt.config.jwt;

public interface JwtProperties {
    Integer EXPIRES = 1000 * 60 * 10;
    String SIGN = "JWT_SIGN";
    String PREFIX = "Bearer ";
}
