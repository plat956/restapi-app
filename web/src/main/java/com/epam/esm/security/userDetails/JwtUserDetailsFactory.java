package com.epam.esm.security.userDetails;

import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

final class JwtUserDetailsFactory {
    private JwtUserDetailsFactory() {
    }

    static JwtUserDetails produce(User user) {
        return new JwtUserDetails(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getBirthday(),
                user.getLogin(),
                user.getPassword(),
                user.getStatus() == User.Status.ACTIVE,
                rolesToGrantedAuthorities(user.getRoles())
        );
    }

    private static List<? extends GrantedAuthority> rolesToGrantedAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .toList();
    }
}
