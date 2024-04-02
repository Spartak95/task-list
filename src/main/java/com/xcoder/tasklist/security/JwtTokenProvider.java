package com.xcoder.tasklist.security;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.crypto.SecretKey;

import com.xcoder.tasklist.domain.user.Role;
import com.xcoder.tasklist.domain.user.User;
import com.xcoder.tasklist.exception.AccessDeniedException;
import com.xcoder.tasklist.service.UserService;
import com.xcoder.tasklist.web.dto.auth.JwtResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final UserService userService;
    private final JwtProperties jwtProperties;
    private final UserDetailsService userDetailsService;
    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
    }

    public String createAccessToken(Long userId, String username, Set<Role> roles) {
        Date nowDate = new Date();
        Date expiryDate = new Date(nowDate.getTime() + jwtProperties.getAccess());

        // jwt payload
        Claims claims = Jwts.claims()
            .subject(username)
            .add("id", userId)
            .add("roles", resolveRoles(roles))
            .build();

        return Jwts.builder()
            .claims(claims)
            .issuedAt(nowDate)
            .expiration(expiryDate)
            .signWith(key)
            .compact();
    }

    private List<String> resolveRoles(Set<Role> roles) {
        return roles.stream()
            .map(Enum::name)
            .toList();
    }

    public String createRefreshToken(Long userId, String username) {
        Date nowDate = new Date();
        Date expiryDate = new Date(nowDate.getTime() + jwtProperties.getRefresh());
        Claims claims = Jwts.claims()
            .subject(username)
            .add("id", userId)
            .build();

        return Jwts.builder()
            .claims(claims)
            .issuedAt(nowDate)
            .expiration(expiryDate)
            .signWith(key)
            .compact();
    }

    public JwtResponse refreshUserTokens(String refreshToken) {

        if (!validateToken(refreshToken)) {
            throw new AccessDeniedException();
        }

        JwtResponse jwtResponse = new JwtResponse();
        Long userId = Long.valueOf(getId(refreshToken));
        User user = userService.getById(userId);
        jwtResponse.setId(userId);
        jwtResponse.setUsername(user.getUsername());
        jwtResponse.setAccessToken(createAccessToken(userId, user.getUsername(), user.getRoles()));
        jwtResponse.setRefreshToken(createRefreshToken(userId, user.getUsername()));

        return jwtResponse;
    }

    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token);

        return !claims.getPayload().getExpiration().before(new Date());
    }

    private String getId(String refreshToken) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(refreshToken)
            .getPayload()
            .get("id", String.class);
    }

    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUsername(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }
}
