package org.team1.keyduck.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.team1.keyduck.member.entity.Address;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;

@Getter
@AllArgsConstructor
public class MemberUpdateResponseDto {

    private Long id;

    private String name;

    private String email;

    private MemberRole memberRole;

    private Address address;


    public static MemberUpdateResponseDto of(Member member) {
        return new MemberUpdateResponseDto(member.getId(), member.getName(), member.getEmail(),
            member.getMemberRole(), member.getAddress());
    }
}
