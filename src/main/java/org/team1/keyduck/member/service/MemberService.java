package org.team1.keyduck.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.DataNotMatchException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.member.dto.request.MemberUpdatePasswordRequestDto;
import org.team1.keyduck.member.dto.request.MemberUpdateRequestDto;
import org.team1.keyduck.member.dto.response.MemberUpdateResponseDto;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberUpdateResponseDto updateMember(MemberUpdateRequestDto requestDto, Long id) {

        Member member = memberRepository.findById(id).orElseThrow(() -> new DataNotFoundException(
            ErrorCode.USER_NOT_FOUND));

        member.updateMember(requestDto);

        return MemberUpdateResponseDto.of(member);
    }

    @Transactional
    public void updatePassword(MemberUpdatePasswordRequestDto requestDto, Long id) {

        Member member = memberRepository.findById(id).orElseThrow(() -> new DataNotFoundException(
            ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(requestDto.getBeforePassword(), member.getPassword())) {
            throw new DataNotMatchException(ErrorCode.INVALID_INPUT_VALUE);
        }

        String encodedModifyPassword = passwordEncoder.encode(requestDto.getModifyPassword());

        member.updatePassword(encodedModifyPassword);
    }

    @Transactional
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new DataNotFoundException(
            ErrorCode.USER_NOT_FOUND));

        member.deleteMember();
    }
}
