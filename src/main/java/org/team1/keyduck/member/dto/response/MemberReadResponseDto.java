package org.team1.keyduck.member.dto.response;

import lombok.Getter;
import org.team1.keyduck.member.entity.Address;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;

@Getter
public class MemberReadResponseDto {

    private final Long id;

    private final String name;

    private final String email;

    private final MemberRole memberRole;

    private final Address address;

    private final Long myPoint;

    private MemberReadResponseDto(Long id, String name, String email, MemberRole memberRole,
            Address address, Long myPoint) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.memberRole = memberRole;
        this.address = address;
        this.myPoint = myPoint;
    }

    public static MemberReadResponseDto of(Member member, Long deposit) {
        return new MemberReadResponseDto(member.getId(), member.getName(), member.getEmail(),
                member.getMemberRole(), member.getAddress(), deposit);
    }
}
