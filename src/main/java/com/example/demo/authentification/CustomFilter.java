package com.example.demo.authentification;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

@Component
public class CustomFilter implements Filter {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService appUserService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();


    public CustomFilter(JwtTokenUtil jwtTokenUtil, CustomUserDetailsService appUserService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.appUserService = appUserService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String requestURI = httpRequest.getRequestURI();

        System.out.println("Request URI: " + requestURI);
        if (pathMatcher.match("/auth/**", requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        String authorizationHeader = httpRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpResponse.getWriter().write("{\"error\": \"Missing or invalid Authorization header\"}");
            return;
        }

        String token = authorizationHeader.substring(7);
        if (!jwtTokenUtil.validateToken(token)) {
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpResponse.getWriter().write("{\"error\": \"Invalid or expired token\"}");
            return;
        }


        var appUser = appUserService.findById(Long.parseLong(jwtTokenUtil.extractId(token)));

        if (appUser == null) {
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpResponse.getWriter().write("{\"error\": \"User not found\"}");
            return;
        }

        UserDetails userDetails = appUserService.loadUserByUsername(appUser.getEmail());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }
}
