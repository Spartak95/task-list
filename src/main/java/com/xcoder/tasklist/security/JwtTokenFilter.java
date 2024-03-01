package com.xcoder.tasklist.security;

import com.xcoder.tasklist.domain.exception.ResourceNotFoundException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {
    private final JwtTokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        String bearerToken = ((HttpServletRequest) servletRequest).getHeader("Authorization");

        if (bearerToken != null && tokenProvider.validateToken(bearerToken)) {
            try {
                Authentication authentication = tokenProvider.getAuthentication(bearerToken);

                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (ResourceNotFoundException ignored) {
            }
        }
    }
}
