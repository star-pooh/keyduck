package org.team1.keyduck.email.dto;

import lombok.Getter;

@Getter
public class EmailEvent {

    private final Long memberId;
    private final MemberEmailRequestDto emailRequestDto;

    public EmailEvent(Long memberId, MemberEmailRequestDto emailRequestDto) {
        this.memberId = memberId;
        this.emailRequestDto = emailRequestDto;
    }
}
