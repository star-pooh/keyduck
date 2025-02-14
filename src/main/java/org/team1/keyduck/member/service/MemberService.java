package org.team1.keyduck.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.member.dto.request.MemberUpdateRequestDto;
import org.team1.keyduck.member.dto.response.MemberUpdateResponseDto;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberUpdateResponseDto updateMember(MemberUpdateRequestDto requestDto, Long id) {

        Member member = memberRepository.findById(id).orElseThrow(() -> new DataNotFoundException(
            ErrorCode.USER_NOT_FOUND));

        member.updateMember(requestDto);

        return MemberUpdateResponseDto.of(member);
    }
}
