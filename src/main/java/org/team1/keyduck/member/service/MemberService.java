package org.team1.keyduck.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.team1.keyduck.common.exception.DuplicateDataException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.member.dto.request.MemberCreateRequestDto;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void createMember(MemberCreateRequestDto requestDto) {

        if (memberRepository.existsByEmail(requestDto.getEmail())) {
            throw new DuplicateDataException(ErrorCode.DUPLICATE_EMAIL);
        }
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        Member member = Member.builder().name(requestDto.getName())
            .memberRole(requestDto.getMemberRole()).email(requestDto.getEmail())
            .password(encodedPassword).build();

        memberRepository.save(member);
    }
}
