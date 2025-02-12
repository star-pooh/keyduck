package org.team1.keyduck.auth.entity;

import java.util.Collection;
import java.util.InputMismatchException;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.team1.keyduck.member.entity.MemberRole;

@Getter
public class AuthMember extends User {

    public AuthMember(Long id, String password,
        Collection<? extends GrantedAuthority> authorities) {
        super(String.valueOf(id), password, authorities);
    }

    public MemberRole getUserRole() {
        return this.getAuthorities().stream()
            .map(authority -> MemberRole.valueOf(authority.getAuthority()))
            .findFirst()
            .orElseThrow(() -> new InputMismatchException("유효하지 않은 권한입니다."));
    }
}
