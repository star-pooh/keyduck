package org.team1.keyduck.auth.entity;

import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.team1.keyduck.member.entity.MemberRole;

@Getter
public class AuthMember implements UserDetails {

    private final Long id;
    private final MemberRole memberRole;

    public AuthMember(Long id, MemberRole memberRole) {
        this.id = id;
        this.memberRole = memberRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + memberRole.name()));
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }
}
