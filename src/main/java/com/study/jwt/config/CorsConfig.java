package com.study.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 서버가 응답시 json을 자바스크립트가 처리할 수 있게 할지 설정 (false시 자바스크립트를 통해 서버에 요청시 응답이 안온다)
        config.setAllowCredentials(true);

        // 모든 IP에 응답을 허용.
        config.addAllowedOrigin("*");

        // 모든 Header에 응답을 허용.
        config.addAllowedHeader("*");

        // 모든 post, get, put, delete, patch등 http Method의 요청을 허용.
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/api/**", config);
        return new CorsFilter(source);
    }
}
