package com.xcoder.tasklist.security;

import java.util.Collection;
import java.util.Set;

import com.xcoder.tasklist.domain.user.Role;
import com.xcoder.tasklist.domain.user.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtEntityFactory {

    public static JwtEntity create(User user) {
        return new JwtEntity(user.getId(), user.getUsername(), user.getName(), user.getPassword(),
                             mapToGrantedAuthorities(user.getRoles()));
    }

    private static Collection<? extends GrantedAuthority> mapToGrantedAuthorities(Set<Role> roles) {
        return roles.stream()
            .map(Enum::name)
            .map(SimpleGrantedAuthority::new)
            .toList();
    }
}
