package com.study.jwt.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        req.setCharacterEncoding("UTF-8");

        System.out.println("필터3");

        // id, pw 정상으로 들어와서 로그인 완료시 jwt 토큰 생성후 응답으로 보내준다.
        // 요청할 때마다 header에 Authorization에 jwt 토큰을 가지고 온다.
        // 토큰이 넘어오면 토큰이 유효한지 검증
        if("POST".equals(req.getMethod())) {
            String headerAuth = req.getHeader("Authorization");
            System.out.println("headerAuth : " + headerAuth);

            if("hello".equals(headerAuth)) {
                chain.doFilter(req, res);
            } else {
                PrintWriter out = res.getWriter();
                out.println("인증 안됨");
            }
        }
    }

}
