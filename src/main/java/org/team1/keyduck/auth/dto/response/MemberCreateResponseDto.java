package org.team1.keyduck.auth.dto.response;

import lombok.Getter;
import org.team1.keyduck.member.entity.Address;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;

@Getter
public class MemberCreateResponseDto {

    private final Long id;

    private final String name;

    private final String email;

    private final MemberRole memberRole;

    private final Address address;

    private MemberCreateResponseDto(Long id, String name, String email, MemberRole memberRole,
            Address address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.memberRole = memberRole;
        this.address = address;
    }

    public static MemberCreateResponseDto of(Member member) {
        return new MemberCreateResponseDto(member.getId(), member.getName(), member.getEmail(),
                member.getMemberRole(), member.getAddress());
    }
}
