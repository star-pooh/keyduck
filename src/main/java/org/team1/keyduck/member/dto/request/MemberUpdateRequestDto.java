package org.team1.keyduck.member.dto.request;

import lombok.Getter;
import org.team1.keyduck.member.entity.Address;

@Getter
public class MemberUpdateRequestDto {

    private String name;

    private String email;

    private Address address;

}
