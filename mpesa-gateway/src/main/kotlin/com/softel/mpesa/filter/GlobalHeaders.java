package com.softel.mpesa.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Order(2)
@Component
public class GlobalHeaders extends OncePerRequestFilter {

    private static final List<String> EXCLUDE_IN_URL = Arrays.asList(".css", ".html", ".js");
    private static final List<String> ALLOWED_HOSTS = Arrays.asList(
        "http://localhost",
        "http://localhost:4200",
        "http://127.0.0.1:4200",
        "http://127.0.0.1", 
        "http://68.183.217.137",
        "http://68.183.217.137:4200",
        "http://vukacommunications.com",
        "https://vukacommunications.com"

    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String origin = request.getHeader("Origin");
        //if (ALLOWED_HOSTS.contains(origin)) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "*");
        //}
        //response.setHeader("Access-Control-Allow-Origin", origin);

        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("X-Frame-Options", "deny");
        response.setHeader("Content-Security-Policy", "script-src 'none'");
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return EXCLUDE_IN_URL.stream().anyMatch(exclude -> request.getServletPath().contains(exclude));
    }

}
